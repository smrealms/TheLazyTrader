package model;

import java.util.Iterator;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Race
{
	private static NavigableMap<Integer, Race> RACE_NAMES = new TreeMap<Integer, Race>();

	private String name;
	private int id;

	// public static final int NEUTRAL = 0;
	// public static final int ALSKANT = 1;
	// public static final int CREONTI = 2;
	// public static final int HUMAN = 3;
	// public static final int IKTHORNE = 4;
	// public static final int NIJARIN = 5;
	// public static final int SALVENE = 6;
	// public static final int THEVIAN = 7;
	// public static final int WQ_HUMAN = 8;
	// public static final String[] NAMES = {"Neutral",
	// "Alskant",
	// "Creonti",
	// "Human",
	// "Ik'Thorne",
	// "Nijarin",
	// "Salvene",
	// "Thevian",
	// "WQ Human"};
	public static String getName(int raceId)
	{
		Race r = RACE_NAMES.get(raceId);
		if(r==null)
			return "";
		return r.getName();
	}

	public static int getId(String raceName)
	{
		Iterator<Integer> iter = RACE_NAMES.keySet().iterator();
		while (iter.hasNext())
		{
			int raceId = iter.next();
			if (raceName.equals(getName(raceId)))
				return raceId;
		}
		return -1;
	}

	public static void addRace(Race r)
	{
		RACE_NAMES.put(r.getId(), r);
	}

	public static NavigableMap<Integer, Race> getRaces()
	{
		return RACE_NAMES;
	}

	public static Race getRace(int raceId)
	{
		return RACE_NAMES.get(raceId);
	}

	public static int getNumberOfRaces()
	{
		return RACE_NAMES.size();
	}

	public Race(String _name, int _id)
	{
		this.name = _name;
		this.id = _id;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return this.name;
	}

	public String toString()
	{
		return this.name;
	}

	public static void clearRaces()
	{
		RACE_NAMES.clear();
	}
}
