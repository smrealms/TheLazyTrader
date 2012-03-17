package model;

public class Connection
{
	public static final String WARP = "Warp";
	private String type;
	private int targetSector;

	public Connection(String _type, int _targetSector)
	{
		this.type = _type;
		this.targetSector = _targetSector;
	}

	public String getType()
	{
		return this.type;
	}

	public int getTargetSector()
	{
		return this.targetSector;
	}
}
