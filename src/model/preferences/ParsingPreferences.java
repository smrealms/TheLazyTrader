package model.preferences;

import java.util.prefs.Preferences;

public class ParsingPreferences
{
	private static String SMRFileParserPath = Preferences.userNodeForPackage(ParsingPreferences.class).get("SMRFileParserPath", null);

	/**
	 * @return the sMRFileParserPath
	 */
	public static String getSMRFileParserPath()
	{
		return SMRFileParserPath;
	}

	/**
	 * @param fileParserPath
	 *            the SMRFileParserPath to set
	 */
	public static void setSMRFileParserPath(String fileParserPath)
	{
		if (fileParserPath == null)
			Preferences.userNodeForPackage(ParsingPreferences.class).remove("SMRFileParserPath");
		else
			Preferences.userNodeForPackage(ParsingPreferences.class).put("SMRFileParserPath", fileParserPath);
		SMRFileParserPath = fileParserPath;
	}
}
