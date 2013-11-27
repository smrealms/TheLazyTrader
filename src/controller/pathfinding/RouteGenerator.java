package controller.pathfinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.RouteSwingWorker;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntObjectProcedure;
import java.util.Set;

import settings.Settings;

import model.Distance;
import model.Good;
import model.MultiplePortRoute;
import model.OneWayRoute;
import model.Port;
import model.Route;
import model.Sector;

public class RouteGenerator
{
	private static final ExecutorService executor = Executors.newFixedThreadPool(Settings.NUMBER_OF_PROCESSORS);
	public static final int EXP_ROUTE = 0;
	public static final int MONEY_ROUTE = 1;
	static NavigableMap<Double, ArrayList<Route>> expRoutes;
	static NavigableMap<Double, ArrayList<Route>> moneyRoutes;
	static RouteSwingWorker publishProgressTo;
	static double[] dontAddWorseThan;
	static int totalTasks;
	private static int tasksCompleted;

	synchronized public static NavigableMap<Double, ArrayList<Route>>[] generateMultiPortRoutes(int maxNumPorts, Sector[] sectors, Map<Integer, Boolean> goods, Map<Integer, Boolean> races, TIntObjectMap<TIntObjectMap<Distance>> distances, int routesForPort, int numberOfRoutes) throws InterruptedException
	{
		return findMultiPortRoutes(maxNumPorts, findOneWayRoutes(sectors, distances, routesForPort, goods, races), numberOfRoutes);
	}

	private static NavigableMap<Double, ArrayList<Route>>[] findMultiPortRoutes(final int maxNumPorts, final TIntObjectMap<OneWayRoute[]> routeLists, final int numberOfRoutes) throws InterruptedException
	{
		dontAddWorseThan = new double[]{ 0.0, 0.0 };
		expRoutes = new TreeMap<Double, ArrayList<Route>>();
		moneyRoutes = new TreeMap<Double, ArrayList<Route>>();
		final Collection<Callable<Object>> runs = new ArrayList<Callable<Object>>();
		totalTasks = 0;
		routeLists.forEachEntry(new TIntObjectProcedure<OneWayRoute[]>() {
			@Override
			public boolean execute(final int sectorId, final OneWayRoute[] owrs) {
				runs.add(new Callable<Object>()
				{
					@Override
					public Object call()
					{
						startRoutesToContinue(maxNumPorts, sectorId, owrs, routeLists);
						RouteGenerator.publishProgress();
						return null;
					}
				});
				if (totalTasks % 10 == 0 && totalTasks > numberOfRoutes)
				{
					runs.add(new Callable<Object>()
					{
						@Override
						public Object call()
						{
							trimRoutes(numberOfRoutes);
							return null;
						}
					});
				}
				totalTasks++;
				return true;
			}
		});
		tasksCompleted = 0;
		executor.invokeAll(runs);
		// SLOW System.gc(); // Get rid of anything we can before going idle.
		NavigableMap<Double, ArrayList<Route>>[] allRoutes = new TreeMap[2];
		allRoutes[EXP_ROUTE] = expRoutes;
		allRoutes[MONEY_ROUTE] = moneyRoutes;
		return allRoutes;
	}

	protected static void publishProgress()
	{
		if (publishProgressTo == null)
			return;
		tasksCompleted++;
		publishProgressTo.publishProgressToBar(tasksCompleted, totalTasks);
	}

	/**
	 * Works by pass by reference so will update higher levels, hacky but works.
	 * 
	 * @param startSectorId
	 * @param forwardRoutes
	 * @param routeLists
	 * @param expRoutes
	 */
	static void startRoutesToContinue(int maxNumPorts, int startSectorId, OneWayRoute[] forwardRoutes, TIntObjectMap<OneWayRoute[]> routeLists)
	{
		maxNumPorts--;
		for (OneWayRoute currentStepRoute : forwardRoutes) {
			int currentStepBuySector = currentStepRoute.getBuySectorId();
			if (currentStepBuySector > startSectorId) // Not already checked
			// && currentStepRoute.getGoodId()!=Good.NOTHING) // Don't start with nothing. // We can start with nothing, as long as we don't do 2 nothings in a row.
			{
				getContinueRoutes(maxNumPorts, startSectorId, currentStepRoute, routeLists.get(currentStepBuySector), routeLists, currentStepRoute.getGoodId() == Good.NOTHING);
			}
		}
	}

	/**
	 * Works by pass by reference so will update higher levels, hacky but works.
	 * 
	 * @param startSectorId
	 * @param routeToContinue
	 * @param forwardRoutes
	 * @param routeLists
	 * @param allRoutes
	 */
	private static void getContinueRoutes(int maxNumPorts, int startSectorId, Route routeToContinue, OneWayRoute[] forwardRoutes, TIntObjectMap<OneWayRoute[]> routeLists, boolean lastGoodIsNothing) {
		maxNumPorts--;
		// if (forwardRoutes==null)
		// return; // Should never be null as it's always going to have at very least Good.NOTHING
		for (OneWayRoute currentStepRoute : forwardRoutes) {
			int currentStepBuySector = currentStepRoute.getBuySectorId();
			if (currentStepBuySector >= startSectorId) // Not already checked or back to start
			{
				boolean newGoodIsNothing = Good.NOTHING == currentStepRoute.getGoodId();
				if (lastGoodIsNothing && newGoodIsNothing)
					continue; // Don't do two nothings in a row
				if (currentStepBuySector == startSectorId) // Route returns to start
				{
					MultiplePortRoute mpr = new MultiplePortRoute(routeToContinue, currentStepRoute);
					addExpRoute(mpr);
					addMoneyRoute(mpr);
				}
				else if (maxNumPorts > 0 && !routeToContinue.containsPort(currentStepBuySector))
				{
					MultiplePortRoute mpr = new MultiplePortRoute(routeToContinue, currentStepRoute);
					getContinueRoutes(maxNumPorts, startSectorId, mpr, routeLists.get(currentStepBuySector), routeLists, newGoodIsNothing);
				}
			}
		}
	}

	private static TIntObjectMap<OneWayRoute[]> findOneWayRoutes(Sector[] sectors, TIntObjectMap<TIntObjectMap<Distance>> distances, int routesForPort, Map<Integer, Boolean> goods, Map<Integer, Boolean> races)
	{
		boolean nothingAllowed = goods.get(Good.NOTHING);
		int[] goodNameKeys = Good.getNames().keys();
		TIntObjectMap<OneWayRoute[]> routes = new TIntObjectHashMap<OneWayRoute[]>();
		for (TIntObjectIterator<TIntObjectMap<Distance>> iter = distances.iterator(); iter.hasNext();) {
			iter.advance();
			int currentSectorId = iter.key();
			Port currentPort = sectors[currentSectorId].getPort();
			Boolean raceAllowed = races.get(currentPort.getPortRace());
			if (raceAllowed == null) {
				System.err.println("Error with Race ID: "+currentPort.getPortRace());
				continue;
			}
			if (!raceAllowed) {
				continue;
			}
			ArrayList<OneWayRoute> rl = new ArrayList<OneWayRoute>(15);

			for (TIntObjectIterator<Distance> d = iter.value().iterator(); d.hasNext();) {
				d.advance();
				int targetSectorId = d.key();
				Port targetPort = sectors[targetSectorId].getPort();
				raceAllowed = races.get(targetPort.getPortRace());
				if (raceAllowed == null) {
					System.err.println("Error with Race ID: "+targetPort.getPortRace());
					continue;
				}
				if (!raceAllowed) {
					continue;
				}
				if(routesForPort!=-1 && currentSectorId != routesForPort && targetSectorId != routesForPort) {
					continue;
				}
				Distance distance = d.value();

				if (nothingAllowed) {
					rl.add(new OneWayRoute(currentSectorId, targetSectorId, currentPort.getPortRace(), targetPort.getPortRace(), currentPort.getGoodDistance(Good.NOTHING), targetPort.getGoodDistance(Good.NOTHING), distance, Good.NOTHING));
				}

				for (int goodId : goodNameKeys) {
					if (goods.get(goodId))
					{
						if (currentPort.getGoodStatus(goodId) == Good.SELLS && targetPort.getGoodStatus(goodId) == Good.BUYS)
						{
							rl.add(new OneWayRoute(currentSectorId, targetSectorId, currentPort.getPortRace(), targetPort.getPortRace(), currentPort.getGoodDistance(goodId), targetPort.getGoodDistance(goodId), distance, goodId));
						}
					}
				}
			}
			routes.put(sectors[currentSectorId].getSectorID(), rl.toArray(new OneWayRoute[0]));
		}
		return routes;
	}

	synchronized public static NavigableMap<Double, ArrayList<Route>>[] generateOneWayRoutes(Sector[] sectors, TIntObjectMap<TIntObjectMap<Distance>> distances, Map<Integer, Boolean> goods, Map<Integer, Boolean> races, int routesForPort)
	{
		TIntObjectMap<OneWayRoute[]> sectorRoutes = findOneWayRoutes(sectors, distances, routesForPort, goods, races);
		dontAddWorseThan = new double[]{ 0.0, 0.0 };
		expRoutes = new TreeMap<Double, ArrayList<Route>>();
		moneyRoutes = new TreeMap<Double, ArrayList<Route>>();
		for (OneWayRoute[] routes : (OneWayRoute[][]) sectorRoutes.values()) {
			for(OneWayRoute owr : routes) {
				Route fakeReturn = new OneWayRoute(owr.getBuySectorId(), owr.getSellSectorId(), owr.getBuyPortRace(), owr.getSellPortRace(), 0, 0, owr.getDistance(), Good.NOTHING);
				Route mpr = new MultiplePortRoute(owr, fakeReturn);
				addExpRoute(mpr);
				addMoneyRoute(mpr);
			}
		}
		NavigableMap<Double, ArrayList<Route>>[] allRoutes = new TreeMap[2];
		allRoutes[EXP_ROUTE] = expRoutes;
		allRoutes[MONEY_ROUTE] = moneyRoutes;
		return allRoutes;
	}

	private static void addExpRoute(Route r)
	{
		ArrayList<Route> rl;
		double overallMultiplier = r.getOverallExpMultiplier();
		if (overallMultiplier > dontAddWorseThan[EXP_ROUTE])
			synchronized (expRoutes)
			{
				rl = expRoutes.get(overallMultiplier);
				if (rl == null) {
					rl = new ArrayList<Route>();
				}
				rl.add(r);
				expRoutes.put(overallMultiplier, rl);
			}
	}

	private static void addMoneyRoute(Route r)
	{
		ArrayList<Route> rl;
		double overallMultiplier = r.getOverallMoneyMultiplier();
		if (overallMultiplier > dontAddWorseThan[MONEY_ROUTE])
			synchronized (moneyRoutes)
			{
				rl = moneyRoutes.get(overallMultiplier);
				if (rl == null) {
					rl = new ArrayList<Route>();
				}
				rl.add(r);
				moneyRoutes.put(overallMultiplier, rl);
			}
	}

	/**
	 * @param _publishProgressTo
	 *            the publishProgressTo to set
	 */
	public static void setPublishProgressTo(RouteSwingWorker _publishProgressTo)
	{
		RouteGenerator.publishProgressTo = _publishProgressTo;
	}

	public static void trimRoutes(int trimToBestXRoutes)
	{
//		boolean trimmed = false;
		synchronized (expRoutes)
		{
			if (expRoutes.size() > trimToBestXRoutes)
			{
				Iterator<Double> iter = expRoutes.descendingKeySet().iterator();
				for (int i = 1; i < trimToBestXRoutes; i++)
				{
					iter.next();
				}
				dontAddWorseThan[EXP_ROUTE] = iter.next();

				while (iter.hasNext())
				{
					iter.next();
					iter.remove();
				}
//				trimmed = true;
			}
		}
		synchronized (moneyRoutes)
		{
			if (moneyRoutes.size() > trimToBestXRoutes)
			{
				Iterator<Double> iter = moneyRoutes.descendingKeySet().iterator();
				for (int i = 1; i < trimToBestXRoutes; i++)
				{
					iter.next();
				}
				dontAddWorseThan[MONEY_ROUTE] = iter.next();
				while (iter.hasNext())
				{
					iter.next();
					iter.remove();
				}
//				trimmed = true;
			}
		}
//		if (trimmed)
//			System.gc();
	}
}