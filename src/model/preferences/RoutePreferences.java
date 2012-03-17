package model.preferences;

import java.util.prefs.Preferences;

import settings.Settings;

public class RoutePreferences
{
	private static boolean useRelationsFactor = Boolean.parseBoolean(Preferences.userNodeForPackage(RoutePreferences.class).get("useRelationsFactor", Boolean.toString(Settings.DEFAULT_USE_RELATIONS_FACTOR)));

	/**
	 * @return the useRelationsFactor
	 */
	public static boolean useRelationsFactor()
	{
		return useRelationsFactor;
	}

	/**
	 * @param useRelationsFactor
	 *            the useRelationsFactor to set
	 */
	public static void setUseRelationsFactor(boolean _useRelationsFactor)
	{
		Preferences.userNodeForPackage(RoutePreferences.class).put("useRelationsFactor", Boolean.toString(useRelationsFactor));
		RoutePreferences.useRelationsFactor = _useRelationsFactor;
	}
}
