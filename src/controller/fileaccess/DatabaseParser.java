package controller.fileaccess;

import java.io.File;
import java.net.URL;

public abstract class DatabaseParser
{

	public DatabaseParser(URL mapFileLocation)
	{
	}

	public DatabaseParser(String mapFileLocation)
	{
	}

	public DatabaseParser(File mapFileLocation)
	{
	}

	protected DatabaseParser()
	{
	}

	abstract public void doParse();
}
