package model.ship;

public class Restriction implements Comparable<Object>
{
	private String name;

	public Restriction(String _name)
	{
		this.name = _name;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String _name)
	{
		this.name = _name;
	}

	public int compareTo(Object o)
	{
		if (this.equals(o))
			return 0;

		if (o.getClass().equals(this.getClass()))
		{
			Restriction sr = (Restriction) o;
			try
			{
				int thisName = Integer.parseInt(this.getName());
				int thatName = Integer.parseInt(sr.getName());
				if (thisName < thatName)
					return -1;
				return 1;
			}
			catch (NumberFormatException nfe)
			{
				return this.name.compareTo(sr.getName());
			}
		}
		if (o.getClass().equals(String.class))
			return this.name.compareTo((String) o);
		return -1;
	}

	public String toString()
	{
		if(this.name.equals("0"))
			return "";
		else if(this.name.equals("1"))
			return "Good";
		else if(this.name.equals("2"))
			return "Evil";
		return this.name;
	}
}
