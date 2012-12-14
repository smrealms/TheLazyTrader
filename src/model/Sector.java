package model;

import java.util.ArrayList;
import java.util.Iterator;

public class Sector
{
	private int sectorId;
	private int galaxyId;
	private ArrayList<Connection> connections = new ArrayList<Connection>();
	private ArrayList<Connection> warps = new ArrayList<Connection>();
	private Port port;
	private boolean planet;
	private ArrayList<Location> locations = new ArrayList<Location>();

	public static String getBBCode(int sectorId) {
		return "[sector=" + sectorId + "]";
	}

	public Sector(int _sectorId)
	{
		this.sectorId = _sectorId;
		this.port = null;
	}

	public void setSectorID(int _sectorId)
	{
		this.sectorId = _sectorId;
	}

	public void setGalaxyID(int _galaxyId)
	{
		this.galaxyId = _galaxyId;
	}

	public void setConnections(ArrayList<Connection> exitSectorIds)
	{
		this.connections = exitSectorIds;
	}

	public void addConnection(String type, int _sectorId)
	{
		this.connections.add(new Connection(type, _sectorId));
	}

	public void setWarps(ArrayList<Connection> exitSectorIds)
	{
		this.warps = exitSectorIds;
	}

	public void addWarp(String type, int _sectorId)
	{
		this.warps.add(new Connection(type, _sectorId));
	}

	public void setPort(int portLevel)
	{
		this.port = new Port(portLevel);
	}

	public void setPort(Port p)
	{
		this.port = p;
	}

	public void setPlanet(int i)
	{
		this.planet = (i == 1 ? true : false);
	}

	public void setPlanet(boolean b)
	{
		this.planet = b;
	}

	public void addLocation(Location l)
	{
		locations.add(l);
	}

	public boolean hasConnections()
	{
		if (this.connections != null)
		{
			return this.connections.size() > 0;
		}
		return false;
	}

	public boolean hasWarps()
	{
		if (this.connections != null)
		{
			return this.connections.size() > 0;
		}
		return false;
	}

	public int getSectorID()
	{
		return this.sectorId;
	}

	public int getGalaxyID()
	{
		return this.galaxyId;
	}

	public ArrayList<Connection> getConnections()
	{
		return this.connections;
	}

	public ArrayList<Connection> getWarps()
	{
		return this.warps;
	}

	public int numConnections()
	{
		return this.connections.size();
	}

	public int numWarps()
	{
		return this.warps.size();
	}

	public boolean hasPort()
	{
		if (this.port == null)
			return false;
		if (this.port.getPortLevel() == 0)
			return false;
		return true;
	}

	public Port getPort()
	{
		return this.port;
	}

	public boolean hasPlanet()
	{
		return this.planet;
	}

	public boolean hasLocation()
	{
		return this.locations.size() > 0;
	}

	public ArrayList<Location> getLocations()
	{
		return this.locations;
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof Sector)
			return this.getSectorID()==((Sector)obj).getSectorID();
		return super.equals(obj);
	}

	public boolean hasX(Object x)
	{
		if (x == Port.class)
		{
			return this.hasPort();
		}
		if (x == Location.class || (x instanceof String && x.equals("Locations")))
		{
			return this.hasLocation();
		}
		if (x instanceof Sector)
		{
			return this.equals(x);
		}
		if (x instanceof Location)
		{
			if (!this.hasLocation())
				return false;
			return this.locations.contains(x);
		}

		if (this.hasPort())
			if (this.port.hasX(x))
				return true;

		if (this.hasLocation())
		{
			for (Iterator<Location> iter = this.locations.iterator(); iter.hasNext();)
			{
				Location l = iter.next();
				if (l.hasX(x))
					return true;
			}
		}
		return false;
	}
}