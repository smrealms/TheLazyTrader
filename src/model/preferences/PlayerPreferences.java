package model.preferences;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.prefs.Preferences;

import settings.Settings;

import model.Race;

public class PlayerPreferences
{
	private static NavigableMap<Integer, Integer> relations = new TreeMap<Integer, Integer>();

	static
	{
		for (int raceId : Race.getRaces().keySet()) {
			relations.put(raceId, Integer.parseInt(Preferences.userNodeForPackage(PlayerPreferences.class).get(Integer.toString(raceId), Integer.toString(Settings.DEFAULT_RACE_RELATIONS))));
		}
	}

	/**
	 * @return the relations
	 */
	public static NavigableMap<Integer, Integer> getRelations()
	{
		return relations;
	}

	/**
	 * @param relations
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
