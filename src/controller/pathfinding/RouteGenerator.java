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
	private static ExecutorService executor = Executors.newFixedThreadPool(Settings.NUMBER_OF_PROCESSORS);
	public static final int EXP_ROUTE = 0;
	public static final int MONEY_ROUTE = 1;
	static NavigableMap<Double, ArrayList<Route>> expRoutes;
	static NavigableMap<Double, ArrayList<Route>> moneyRoutes;
	static RouteSwingWorker publishProgressTo;
//	static int trimToBestXRoutes = Settings.DEFAULT_TRIM_TO_BEST_X_ROUTES;
	static double[] dontAddWorseThan = { 0, 0 };
	static int totalTasks;
	private static int tasksCompleted;

	// synchronized public static NavigableMap<Double, ArrayList<Route>>[]
	// generateRoutes(Sector[] sectors,boolean[] goodIds,Map<Integer, Boolean>
	// races,Map<Integer,Map<Integer,Distance>> distances) throws
	// InterruptedException
	// {
	// return
	// findTwoWayRoutes(findOneWayRoutes(sectors,distances,goodIds,races));
	// }

	synchronized public static NavigableMap<Double, ArrayList<Route>>[] generateMultiPortRoutes(long maxNumPorts, Sector[] sectors, Map<Integer, Boolean> goods, Map<Integer, Boolean> races, Map<Integer, Map<Integer, Distance>> distances, long routesForPort, long numberOfRoutes) throws InterruptedException
	{
		return findMultiPortRoutes(maxNumPorts, findOneWayRoutes(sectors, distances, routesForPort, goods, races), numberOfRoutes);
	}

	// private static NavigableMap<Double, ArrayList<Route>>[]
	// findTwoWayRoutes(final Map<Integer, ArrayList<OneWayRoute>> routeLists)
	// throws InterruptedException
	// {
	// expRoutes = new TreeMap<Double, ArrayList<Route>>();
	// moneyRoutes = new TreeMap<Double, ArrayList<Route>>();
	// Collection<Callable<Object>> runs = new ArrayList<Callable<Object>>();
	// Iterator<Entry<Integer, ArrayList<OneWayRoute>>> iter =
	// routeLists.entrySet().iterator();
	// totalTasks=0;
	// // int i=0;
	// while(iter.hasNext())
	// {
	// final Entry<Integer, ArrayList<OneWayRoute>> es = iter.next();
	// runs.add(new Callable<Object>(){
	// public Object call()
	// {
	// // System.out.println("Starting: " + es.getKey());
	// findLastRouteOnMultiPortRoute(es.getKey(),es.getValue(),routeLists);
	// RouteGenerator.publishProgress();
	// return null;
	// }
	// });
	// if(totalTasks%10==0)
	// {
	// runs.add(new Callable<Object>(){
	// public Object call()
	// {
	// trimRoutes();
	// return null;
	// }
	// });
	// }
	// totalTasks++;
	// // i++;
	// }
	// tasksCompleted=0;
	// executor.invokeAll(runs);
	//		
	// NavigableMap<Double, ArrayList<Route>>[] allRoutes = new TreeMap[2];
	// allRoutes[EXP_ROUTE]=expRoutes;
	// allRoutes[MONEY_ROUTE]=moneyRoutes;
	// return allRoutes;
	// }

	private static NavigableMap<Double, ArrayList<Route>>[] findMultiPortRoutes(final long maxNumPorts, final Map<Integer, ArrayList<OneWayRoute>> routeLists, final long numberOfRoutes) throws InterruptedException
	{
		expRoutes = new TreeMap<Double, ArrayList<Route>>();
		moneyRoutes = new TreeMap<Double, ArrayList<Route>>();
		Collection<Callable<Object>> runs = new ArrayList<Callable<Object>>();
		Iterator<Entry<Integer, ArrayList<OneWayRoute>>> iter = routeLists.entrySet().iterator();
		totalTasks = 0;
		// int i=0;
		while (iter.hasNext())
		{
			final Entry<Integer, ArrayList<OneWayRoute>> es = iter.next();
			runs.add(new Callable<Object>()
			{
				public Object call()
				{
//					System.out.println(tasksCompleted);
					startRoutesToContinue(maxNumPorts, es.getKey(), es.getValue(), routeLists);
					RouteGenerator.publishProgress();
					return null;
				}
			});
			if (totalTasks % 10 == 0 && totalTasks > numberOfRoutes)
			{
				runs.add(new Callable<Object>()
				{
					public Object call()
					{
						trimRoutes(numberOfRoutes);
						return null;
					}
				});
			}
			totalTasks++;
			// i++;
		}
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
	static void startRoutesToContinue(long maxNumPorts, int startSectorId, ArrayList<OneWayRoute> forwardRoutes, Map<Integer, ArrayList<OneWayRoute>> routeLists)
	{
		Iterator<OneWayRoute> forwardRouteIter = forwardRoutes.iterator();
		while (forwardRouteIter.hasNext())
		{
			OneWayRoute currentStepRoute = forwardRouteIter.next();
			int currentStepBuySector = currentStepRoute.getBuySectorId();
			if (currentStepBuySector > startSectorId) // Not already checked
			// && currentStepRoute.getGoodId()!=Good.NOTHING) // Don't start with nothing. // We can start with nothing, as long as we don't do 2 nothings in a row.
			{
				getContinueRoutes(maxNumPorts - 1, startSectorId, currentStepRoute, routeLists.get(currentStepBuySector), routeLists, currentStepRoute.getGoodId() == Good.NOTHING);
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
	private static void getContinueRoutes(long maxNumPorts, int startSectorId, Route routeToContinue, ArrayList<OneWayRoute> forwardRoutes, Map<Integer, ArrayList<OneWayRoute>> routeLists, boolean lastGoodIsNothing)// ,boolean[]
																																																						// visitedPorts)
	{
		// if (forwardRoutes==null)
		// return; // Should never be null as it's always going to have at very least Good.NOTHING
		Iterator<OneWayRoute> forwardRouteIter = forwardRoutes.iterator();
		while (forwardRouteIter.hasNext())
		{
			OneWayRoute currentStepRoute = forwardRouteIter.next();
			int currentStepBuySector = currentStepRoute.getBuySectorId();
			if (lastGoodIsNothing & (lastGoodIsNothing = Good.NOTHING == currentStepRoute.getGoodId()))
				continue; // Don't do two nothings in a row
			if (currentStepBuySector >= startSectorId) // Not already checked or back to start
			{
				if (currentStepBuySector == startSectorId) // Route returns to start
				{
					MultiplePortRoute mpr = new MultiplePortRoute(routeToContinue, currentStepRoute);
					addExpRoute(mpr);
					addMoneyRoute(mpr);
				}
				else if (maxNumPorts > 1 && !routeToContinue.containsPort(currentStepBuySector))
				{
					MultiplePortRoute mpr = new MultiplePortRoute(routeToContinue, currentStepRoute);
					getContinueRoutes(maxNumPorts - 1, startSectorId, mpr, routeLists.get(currentStepBuySector), routeLists, lastGoodIsNothing);
				}
			}
		}
	}

	// static void findLastRouteOnMultiPortRoute(int
	// startSectorId,ArrayList<OneWayRoute> forwardRoutes,Map<Integer,
	// ArrayList<OneWayRoute>> routeLists)
	// {
	// Iterator<OneWayRoute> forwardRouteIter = forwardRoutes.iterator();
	// while(forwardRouteIter.hasNext())
	// {
	// OneWayRoute forwardRoute = forwardRouteIter.next();
	// if(forwardRoute.getBuySectorId()>forwardRoute.getSellSectorId()) //Not
	// already checked
	// {
	// //Add one way routes in case they are best. // Will be Nothing on return
	// for one way.
	// // addExpRoute(forwardRoute,expRoutes);
	// // addMoneyRoute(forwardRoute,moneyRoutes);
	//				
	// ArrayList<OneWayRoute> returnRoutes =
	// routeLists.get(forwardRoute.getBuySectorId());
	// if(returnRoutes!=null) // If port has return routes
	// {
	// Iterator<OneWayRoute> returnRouteIter = returnRoutes.iterator();
	// while(returnRouteIter.hasNext())
	// {
	// OneWayRoute returnRoute = returnRouteIter.next();
	// if(returnRoute.getBuySectorId()==startSectorId) //Route returns to start
	// {
	// MultiplePortRoute mpr = new MultiplePortRoute(forwardRoute,returnRoute);
	// addExpRoute(mpr);
	// addMoneyRoute(mpr);
	// }
	// }
	// }
	// }
	// }
	// }

	private static Map<Integer, ArrayList<OneWayRoute>> findOneWayRoutes(Sector[] sectors, Map<Integer, Map<Integer, Distance>> distances, long routesForPort, Map<Integer, Boolean> goods, Map<Integer, Boolean> races)
	{
		int targetSectorId, currentSectorId;
		Distance distance;
		Map<Integer, ArrayList<OneWayRoute>> routes = new LinkedHashMap<Integer, ArrayList<OneWayRoute>>();
		Iterator<Integer> dKeyIter = distances.keySet().iterator();
		while (dKeyIter.hasNext())
		{
			currentSectorId = dKeyIter.next();
			Port currentPort = sectors[currentSectorId].getPort();
			Boolean raceAllowed = races.get(currentPort.getPortRace());
			if (raceAllowed==null)
			{
				System.err.println("Error with Race ID: "+currentPort.getPortRace());
				continue;
			}
			if(!raceAllowed)
				continue;
			Map<Integer, Distance> d = distances.get(currentSectorId);
			Iterator<Entry<Integer, Distance>> iter = d.entrySet().iterator();
			ArrayList<OneWayRoute> rl = new ArrayList<OneWayRoute>(15);
			while (iter.hasNext())
			{
				Entry<Integer, Distance> es = iter.next();
				targetSectorId = es.getKey();
				Port targetPort = sectors[targetSectorId].getPort();
				if (!races.get(targetPort.getPortRace()))
					continue;
				if(routesForPort!=-1 && currentSectorId != routesForPort && targetSectorId != routesForPort)
					continue;
				distance = es.getValue();

				if (goods.get(Good.NOTHING))
					rl.add(new OneWayRoute(currentSectorId, targetSectorId, currentPort.getPortRace(), targetPort.getPortRace(), currentPort.getGoodDistance(Good.NOTHING), targetPort.getGoodDistance(Good.NOTHING), distance, Good.NOTHING));

				Iterator<Integer> gIter = Good.getNames().keySet().iterator();
				while (gIter.hasNext())
				{
					int goodId = gIter.next();
					if (goods.get(goodId))
					{
						if (currentPort.getGoodStatus(goodId) == Good.SELLS && targetPort.getGoodStatus(goodId) == Good.BUYS)
						{
							rl.add(new OneWayRoute(currentSectorId, targetSectorId, currentPort.getPortRace(), targetPort.getPortRace(), currentPort.getGoodDistance(goodId), targetPort.getGoodDistance(goodId), distance, goodId));
						}
					}
				}
			}
			routes.put(sectors[currentSectorId].getSectorID(), rl);
		}
		return routes;
	}

	synchronized public static NavigableMap<Double, ArrayList<Route>>[] generateOneWayRoutes(Sector[] sectors, Map<Integer, Map<Integer, Distance>> distances, Map<Integer, Boolean> goods, Map<Integer, Boolean> races, long routesForPort)
	{
		int targetSectorId, currentSectorId;
		Distance distance;
		expRoutes = new TreeMap<Double, ArrayList<Route>>();
		moneyRoutes = new TreeMap<Double, ArrayList<Route>>();
		Iterator<Integer> dKeyIter = distances.keySet().iterator();
		while (dKeyIter.hasNext())
		{
			currentSectorId = dKeyIter.next();
			Port currentPort = sectors[currentSectorId].getPort();
			if (!races.get(currentPort.getPortRace()))
				continue;
			Map<Integer, Distance> d = distances.get(currentSectorId);
			Iterator<Entry<Integer, Distance>> iter = d.entrySet().iterator();
			while (iter.hasNext())
			{
				Entry<Integer, Distance> es = iter.next();
				targetSectorId = es.getKey();
				Port targetPort = sectors[targetSectorId].getPort();
				if (!races.get(targetPort.getPortRace()))
					continue;
				if(routesForPort!=-1 && currentSectorId != routesForPort && targetSectorId != routesForPort)
					continue;
				distance = es.getValue();

				Iterator<Integer> gIter = Good.getNames().keySet().iterator();
				while (gIter.hasNext())
				{
					int goodId = gIter.next();
					if (goods.get(goodId))
					{
						if (currentPort.getGoodStatus(goodId) == Good.SELLS && targetPort.getGoodStatus(goodId) == Good.BUYS)
						{
							Route owr = new OneWayRoute(currentSectorId, targetSectorId, currentPort.getPortRace(), targetPort.getPortRace(), currentPort.getGoodDistance(goodId), targetPort.getGoodDistance(goodId), distance, goodId);
							Route fakeReturn = new OneWayRoute(targetSectorId, currentSectorId, targetPort.getPortRace(), currentPort.getPortRace(), 0, 0, distance, Good.NOTHING);
							Route mpr = new MultiplePortRoute(owr, fakeReturn);
							addExpRoute(mpr);
							addMoneyRoute(mpr);
						}
					}
				}
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
				if (expRoutes.get(overallMultiplier) != null)
				{
					rl = expRoutes.get(overallMultiplier);
				}
				else
				{
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
				if (moneyRoutes.get(overallMultiplier) != null)
				{
					rl = moneyRoutes.get(overallMultiplier);
				}
				else
				{
					rl = new ArrayList<Route>();
				}
				rl.add(r);
				moneyRoutes.put(overallMultiplier, rl);
			}
	}

	/**
	 * @param publishProgressTo
	 *            the publishProgressTo to set
	 */
	public static void setPublishProgressTo(RouteSwingWorker _publishProgressTo)
	{
		RouteGenerator.publishProgressTo = _publishProgressTo;
	}

	public static void trimRoutes(long trimToBestXRoutes)
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
					expRoutes.remove(iter.next());
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
					moneyRoutes.remove(iter.next());
				}
//				trimmed = true;
			}
		}
//		if (trimmed)
//			System.gc();
	}
}