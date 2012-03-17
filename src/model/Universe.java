package model;

import java.util.Map;
import java.util.TreeMap;

public class Universe
{
	Map<Integer, Galaxy> galaxies = new TreeMap<Integer, Galaxy>();
	Sector[] sectors;

	public Map<Integer, Galaxy> getGalaxies()
	{
		return this.galaxies;
	}

	public void setGalaxies(Map<Integer, Galaxy> _galaxies)
	{
		this.galaxies = _galaxies;
	}

	public void addGalaxy(Galaxy galaxy)
	{
		this.galaxies.put(galaxy.getGalaxyId(), galaxy);
	}

	public Sector[] getSectors()
	{
		return this.sectors;
	}

	public void setSectors(Sector[] _sectors)
	{
		this.sectors = _sectors;
	}

	public Galaxy getGalaxy(int galaxyId)
	{
		return this.galaxies.get(galaxyId);
	}

	public void clearSectors()
	{
		galaxies.clear();
		sectors = new Sector[0];
	}
}
