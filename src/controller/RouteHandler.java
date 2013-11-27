package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Map.Entry;

import view.FileLocate;
import view.FileLocateListener;

import controller.pathfinding.Pathfinding;
import controller.pathfinding.RouteGenerator;
import gnu.trove.map.TIntObjectMap;

import model.Distance;
import model.Route;
import model.Route.DisplayType;
import model.Universe;

public class RouteHandler implements FileLocateListener
{
	private static final DecimalFormat df = new DecimalFormat("#.00");

	private int numberOfRoutes;
	private Universe universe;
	private int startSector;
	private int endSector;
	private int maxDistance;
	private int maxNumberOfPorts;
	private int routesForPort;
	private Route.DisplayType displayType;
	private Map<Integer, Boolean> races;
	private Map<Integer, Boolean> goods;
	private int typeOfRoute;
	private boolean doSave = false;
	private File saveFile;

	private boolean distancesChangedSinceLastRun = true;
	private boolean routesChangedSinceLastRun = true;
	private TIntObjectMap<TIntObjectMap<Distance>> distances;
	private NavigableMap<Double, ArrayList<Route>>[] allRoutes;


	public RouteHandler(int _typeOfRoute, int _numberOfRoutes, int _startSector, int _endSector, int _maxDistance, int _maxNumberOfPorts, int _routesForPort, Route.DisplayType _displayType, Map<Integer, Boolean> _races, Map<Integer, Boolean> _goods)
	{
		this.typeOfRoute = _typeOfRoute;
		this.numberOfRoutes = _numberOfRoutes;
		this.startSector = _startSector;
		this.endSector = _endSector;
		this.maxDistance = _maxDistance;
		this.maxNumberOfPorts = _maxNumberOfPorts;
		this.routesForPort = _routesForPort;
		this.displayType = _displayType;

		this.races = new HashMap<Integer, Boolean>(_races.size());
		for (int raceId : _races.keySet()) {
			this.races.put(raceId, _races.get(raceId));
		}

		this.goods = new HashMap<Integer, Boolean>(_goods.size());
		for (int goodId : _goods.keySet()) {
			this.goods.put(goodId, _goods.get(goodId));
		}
		FileLocate.addFileLocateListener(this);
	}

	public String doInBackground(RouteSwingWorker parent)
	{
		try
		{
			if (this.numberOfRoutes < 1)
				return "Number of routes must be positive";
			if (this.maxNumberOfPorts < 1)
				return "Number of ports must be positive";
			if (this.universe == null)
				return "Universe not loaded properly.";

			if (this.distancesChangedSinceLastRun)
			{
//				long t1 = System.nanoTime();
				distances = Pathfinding.calculatePortToPortDistances(this.universe.getSectors(), startSector, endSector, maxDistance);
//				long t2 = System.nanoTime();
//				System.out.println("Time taken:" + ((t2-t1)/1000000000.0));
				
				this.distancesChangedSinceLastRun = false;
				this.routesChangedSinceLastRun = true;
				parent.publishProgress("Distances Generated");
			}
			else
				parent.publishProgress("Using cached distances");
			if (this.routesChangedSinceLastRun)
			{
//				long t1 = System.nanoTime();
				if (this.maxNumberOfPorts == 1)
					allRoutes = RouteGenerator.generateOneWayRoutes(this.universe.getSectors(), this.distances, this.goods, this.races, this.routesForPort);
				else
					allRoutes = RouteGenerator.generateMultiPortRoutes(this.maxNumberOfPorts, this.universe.getSectors(), this.goods, this.races, this.distances, this.routesForPort, this.numberOfRoutes);
//				long t2 = System.nanoTime();
//				System.out.println("Time taken:" + ((t2-t1)/1000000000.0));
				this.routesChangedSinceLastRun = false;
				parent.publishProgress("Routes Generated");
			}
			else
				parent.publishProgress("Using cached routes");
			if (this.doSave)
			{
				try
				{
					saveTopRoutes();
					return "Finished saving.";
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return "Saving file failed.";
				}
			}
			return getTopRoutesString();
		}
		catch (InterruptedException e)
		{
			return null;
		}
	}

	private String getTopRoutesString()
	{
		Iterator<Entry<Double, ArrayList<Route>>> iter = allRoutes[this.typeOfRoute].descendingMap().entrySet().iterator();
		String retRoutes = "";
		int routesSavedForDisplay = 0;
		while (routesSavedForDisplay < this.numberOfRoutes && iter.hasNext())
		{
			Entry<Double, ArrayList<Route>> e = iter.next();
			ArrayList<Route> rl = e.getValue();
			for (int rlIndex = 0; rlIndex < rl.size() && routesSavedForDisplay < this.numberOfRoutes; rlIndex++)
			{
				Route r = rl.get(rlIndex);
				retRoutes += "Exp: " + df.format(r.getOverallExpMultiplier()) + "\tMoney: $" + df.format(r.getOverallMoneyMultiplier()) + "\r\nRoute: " + r.getRouteString(this.displayType) + "\r\n\r\n";
				routesSavedForDisplay++;
			}
		}
		return retRoutes;
	}

	public void saveTopRoutes() throws IOException
	{
		FileWriter writeFile = new FileWriter(saveFile);

		Iterator<Entry<Double, ArrayList<Route>>> iter = allRoutes[this.typeOfRoute].descendingMap().entrySet().iterator();

		int routesSavedForDisplay = 0;
		while (routesSavedForDisplay < this.numberOfRoutes && iter.hasNext())
		{
			Entry<Double, ArrayList<Route>> e = iter.next();
			ArrayList<Route> rl = e.getValue();
			for (int rlIndex = 0; rlIndex < rl.size() && routesSavedForDisplay < this.numberOfRoutes; rlIndex++)
			{
				Route r = rl.get(rlIndex);
				writeFile.write("Exp: " + df.format(r.getOverallExpMultiplier()) + "\tMoney: $" + df.format(r.getOverallMoneyMultiplier()) + "\r\nRoute: " + r.getRouteString(this.displayType) + "\r\n\r\n");
				routesSavedForDisplay++;
			}
			writeFile.flush();
		}
		writeFile.close();
	}

	/**
	 * @return the endSector
	 */
	public int getEndSector()
	{
		return this.endSector;
	}

	/**
	 * @param _endSector
	 *            the endSector to set
	 */
	public void setEndSector(int _endSector)
	{
		if (this.endSector == _endSector)
			return;
		this.endSector = _endSector;
		this.distancesChangedSinceLastRun = true;
	}

	/**
	 * @return the goods
	 */
	public Map<Integer, Boolean> getGoods()
	{
		return this.goods;
	}

	/**
	 * Cannot be set to null.
	 * 
	 * @param _goods
	 *            the goods to set
	 */
	public void setGoods(Map<Integer, Boolean> _goods)
	{
		if (_goods == null)
			return;
		if (this.goods.size() != _goods.size())
		{
			goods = new HashMap<Integer, Boolean>(goods.size());
			this.routesChangedSinceLastRun = true;
		}
		for (int goodId : _goods.keySet()) {
			if (this.goods.get(goodId) != _goods.get(goodId))
			{
				goods.put(goodId, _goods.get(goodId));
				this.routesChangedSinceLastRun = true;
			}
		}
	}

	/**
	 * @return the maxDistance
	 */
	public int getMaxDistance()
	{
		return this.maxDistance;
	}

	/**
	 * @param _maxDistance
	 *            the maxDistance to set
	 */
	public void setMaxDistance(int _maxDistance)
	{
		if (this.maxDistance == _maxDistance)
			return;
		this.maxDistance = _maxDistance;
		this.distancesChangedSinceLastRun = true;
	}

	/**
	 * @return the maxNumberOfPorts
	 */
	public int getMaxNumberOfPorts()
	{
		return this.maxNumberOfPorts;
	}

	/**
	 * @param _maxNumberOfPorts
	 *            the maxNumberOfPorts to set
	 */
	public void setMaxNumberOfPorts(int _maxNumberOfPorts)
	{
		if (this.maxNumberOfPorts == _maxNumberOfPorts)
			return;
		this.maxNumberOfPorts = _maxNumberOfPorts;
		this.routesChangedSinceLastRun = true;
	}

	/**
	 * @return the numberOfRoutes
	 */
	public int getNumberOfRoutes()
	{
		return this.numberOfRoutes;
	}

	/**
	 * @param _numberOfRoutes
	 *            the numberOfRoutes to set
	 */
	public void setNumberOfRoutes(int _numberOfRoutes)
	{
		this.numberOfRoutes = _numberOfRoutes;
	}

	public void setDisplayType(DisplayType _displayType) {
		this.displayType = _displayType;
	}

	/**
	 * @return the races
	 */
	public Map<Integer, Boolean> getRaces()
	{
		return this.races;
	}

	/**
	 * @param _races
	 *            the races to set
	 */
	public void setRaces(Map<Integer, Boolean> _races)
	{
		if (_races == null)
			return;
		if (this.races.size() != _races.size())
		{
			races = new HashMap<Integer, Boolean>(races.size());
			this.routesChangedSinceLastRun = true;
		}
		for (int raceId : _races.keySet()) {
			if (this.races.get(raceId) != _races.get(raceId))
			{
				races.put(raceId, _races.get(raceId));
				this.routesChangedSinceLastRun = true;
			}
		}
	}

	/**
	 * @return the typeOfRoute
	 */
	public int getTypeOfRoute()
	{
		return this.typeOfRoute;
	}

	/**
	 * @param routeType
	 *            the typeOfRoute to set
	 */
	public void setTypeOfRoute(int routeType)
	{
		this.typeOfRoute = routeType;
	}

	/**
	 * @return the startSector
	 */
	public int getStartSector()
	{
		return this.startSector;
	}

	/**
	 * @param _startSector
	 *            the startSector to set
	 */
	public void setStartSector(int _startSector)
	{
		if (this.startSector == _startSector)
			return;
		this.startSector = _startSector;
		this.distancesChangedSinceLastRun = true;
	}

	/**
	 * @param _universe
	 *            the universe to set
	 */
	protected void setUniverse(Universe _universe)
	{
		this.universe = _universe;
		this.distancesChangedSinceLastRun = true;
	}

	/**
	 * @return the doSave
	 */
	public boolean isDoSave()
	{
		return this.doSave;
	}

	/**
	 * @param _doSave
	 *            the doSave to set
	 */
	public void setDoSave(boolean _doSave)
	{
		this.doSave = _doSave;
	}

	/**
	 * @return the saveFile
	 */
	public File getSaveFile()
	{
		return this.saveFile;
	}

	/**
	 * @param _saveFile
	 *            the saveFile to set
	 */
	public void setSaveFile(File _saveFile)
	{
		this.saveFile = _saveFile;
	}

	public void setRoutesForPort(int _routesForPort)
	{
		if(this.routesForPort==_routesForPort)
			return;
		this.routesChangedSinceLastRun = true;
		this.routesForPort = _routesForPort;
	}

	@Override
	public void universeLocated()
	{
		this.setUniverse(FileLocate.getUniverseParser().getUniverse());
	}

	@Override
	public void universeLost()
	{
		this.setUniverse(null);
	}
}
