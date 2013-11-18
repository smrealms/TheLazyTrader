package view;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


import model.Good;
import model.Location;
import model.Race;
import model.Weapon;
import model.preferences.ParsingPreferences;
import model.ship.Ship;

import controller.fileaccess.DatabaseIniParser;
import controller.fileaccess.SMRIniParser;
import controller.fileaccess.UniverseParser;

public class FileLocate
{

	private static UniverseParser universeParser = null;

	static
	{
		if (ParsingPreferences.getSMRFileParserPath() != null)
		{
			try
			{
				universeParser = new SMRIniParser(ParsingPreferences.getSMRFileParserPath());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static UniverseParser getUniverseParser()
	{
		return universeParser;
	}

	public static void setUniverseParser(UniverseParser _parser)
	{
		universeParser = _parser;
	}

	public static boolean hasUniverseParser()
	{
		return universeParser != null;
	}

	private static ArrayList<FileLocateListener> fileLocateListeners = new ArrayList<FileLocateListener>();

	public static boolean openUniverseFile(Component parent)
	{
		JFileChooser jfc = new JFileChooser();
		jfc.setFileFilter(new FileNameExtensionFilter("SMR 1.5 Sectors File (.smr)", "smr"));
		try
		{
			jfc.setCurrentDirectory(new File(new File(".").getCanonicalPath()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		int retVal = jfc.showOpenDialog(parent);
		if (retVal == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				ParsingPreferences.setSMRFileParserPath(jfc.getSelectedFile().getAbsolutePath());
				new DatabaseIniParser(ParsingPreferences.getSMRFileParserPath()).doParse();
				FileLocate.setUniverseParser(new SMRIniParser(ParsingPreferences.getSMRFileParserPath()));
				fireUniverseFound();
				return true;
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "There was an error opening the file.", "Error Opening File", JOptionPane.ERROR_MESSAGE);
			}
		}
		return false;
	}

	public static void closeUniverseFile()
	{
		ParsingPreferences.setSMRFileParserPath(null);
		Weapon.clearWeapons();
		Ship.clearShips();
		Location.clearLocations();
		Good.clearGoods();
		Race.clearRaces();
		universeParser = null;
		fireUniverseLost();
	}

	public static File askForSaveRoutesFile(Component parent)
	{
		return askForSaveRoutesFile(parent, null);
	}

	public static File askForSaveRoutesFile(Component parent, File saveRoutesFile)
	{
		JFileChooser jfc = new JFileChooser();
		jfc.setFileFilter(new FileNameExtensionFilter("Standard Text File (.txt)", "txt"));
		try
		{
			if (saveRoutesFile != null)
				jfc.setCurrentDirectory(saveRoutesFile);
			else
				jfc.setCurrentDirectory(new File(new File(".").getCanonicalPath()));
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}
		int retVal = jfc.showSaveDialog(parent);
		if (retVal == JFileChooser.APPROVE_OPTION)
		{
			return jfc.getSelectedFile();
		}
		return null;
	}

	public static void addFileLocateListener(FileLocateListener fle)
	{
		fileLocateListeners.add(fle);
		if (FileLocate.getUniverseParser() == null)
			fle.universeLost();
		else
			fle.universeLocated();
	}

	private static void fireUniverseFound()
	{
		for (FileLocateListener fll : fileLocateListeners) {
			fll.universeLocated();
		}
	}

	private static void fireUniverseLost()
	{
		for (FileLocateListener fll : fileLocateListeners) {
			fll.universeLost();
		}
	}
}
