package model;

public class Galaxy
{
	private int galaxyId;
	private int width;
	private int height;
	private int startSectorId;
	private int endSectorId;
	private String type;
	private String name;

	public Galaxy(int _galaxyId)
	{
		this.galaxyId = _galaxyId;
	}

	public int getGalaxyId()
	{
		return this.galaxyId;
	}

	public void setGalaxyId(int _galaxyId)
	{
		this.galaxyId = _galaxyId;
	}

	public int getHeight()
	{
		return this.height;
	}

	public void setHeight(int _height)
	{
		this.height = _height;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String _name)
	{
		this.name = _name;
	}

	public String getType()
	{
		return this.type;
	}

	public void setType(String _type)
	{
		this.type = _type;
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setWidth(int _width)
	{
		this.width = _width;
	}

	public int getEndSectorId()
	{
		return this.endSectorId;
	}

	public void setEndSectorId(int _endSectorId)
	{
		this.endSectorId = _endSectorId;
	}

	public int getStartSectorId()
	{
		return this.startSectorId;
	}

	public void setStartSectorId(int _startSectorId)
	{
		this.startSectorId = _startSectorId;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
