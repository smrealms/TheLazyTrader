package model.preferences;

import java.util.prefs.Preferences;

public class GeneralPreferences
{
	private static boolean askForMemory = Boolean.parseBoolean(Preferences.userNodeForPackage(ParsingPreferences.class).get("askForMemory", "false"));
	private static int memoryToAllocate = Integer.parseInt(Preferences.userNodeForPackage(ParsingPreferences.class).get("memoryToAllocate", "0"));

	/**
	 * @return the askForMemory
	 */
	public static boolean askForMemory()
	{
		return askForMemory;
	}

	/**
	 * @param askForMemory
	 *            the askForMemory to set
	 */
	public static void setAskForMemory(boolean _askForMemory)
	{
		Preferences.userNodeForPackage(ParsingPreferences.class).put("askForMemory", Boolean.toString(_askForMemory));
		GeneralPreferences.askForMemory = _askForMemory;
	}

	/**
	 * @return the memoryToAllocate
	 */
	public static int getMemoryToAllocate()
	{
		return memoryToAllocate;
	}

	/**
	 * @param memoryToAllocate
	 *            the memoryToAllocate to set
	 */
	public static void setMemoryToAllocate(int _memoryToAllocate)
	{
		Preferences.userNodeForPackage(ParsingPreferences.class).put("memoryToAllocate", Integer.toString(_memoryToAllocate));
		GeneralPreferences.memoryToAllocate = _memoryToAllocate;
	}

}
