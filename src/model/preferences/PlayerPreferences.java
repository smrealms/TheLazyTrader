package model.preferences;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.prefs.Preferences;

import settings.Settings;

import model.Race;

public class PlayerPreferences
{
	private static final TIntIntMap relations = new TIntIntHashMap();

	static
	{
		for (int raceId : Race.getRaces().keys()) {
			relations.put(raceId, Integer.parseInt(Preferences.userNodeForPackage(PlayerPreferences.class).get(Integer.toString(raceId), Integer.toString(Settings.DEFAULT_RACE_RELATIONS))));
		}
	}

	/**
	 * @return the relations
	 */
	public static TIntIntMap getRelations()
	{
		return relations;
	}

	/**
	 * @param race
	 * @param _relations
	 *            the relations to set
	 */
	public static void setRelation(int race, int _relations)
	{
		Preferences.userNodeForPackage(PlayerPreferences.class).put(Integer.toString(race), Integer.toString(_relations));
		relations.put(race, _relations);
	}

	public static Integer getRelationsForRace(int raceId)
	{
		return relations.get(raceId);
	}
}
