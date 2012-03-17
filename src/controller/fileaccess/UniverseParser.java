package controller.fileaccess;

import java.io.File;
import java.net.URL;
import java.util.Map;

import model.Galaxy;
import model.Universe;

public abstract class UniverseParser
{
	public UniverseParser(URL mapFileLocation)
	{
	}

	public UniverseParser(String mapFileLocation)
	{
	}

	public UniverseParser(File mapFileLocation)
	{
	}

	protected UniverseParser()
	{
	}

	abstract public Universe getUniverse();

	abstract public void doParse();

	abstract public Map<Integer, Galaxy> getGalaxies();
}
