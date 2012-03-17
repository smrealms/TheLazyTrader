package model.ship;

import java.util.NavigableMap;
import java.util.TreeMap;

public class ShipAbility
{
	String name;
	String description;

	private static NavigableMap<String, ShipAbility> SHIP_ABILITIES = new TreeMap<String, ShipAbility>();

	public static void addShipAbility(ShipAbility shipAbil)
	{
		SHIP_ABILITIES.put(shipAbil.getName(), shipAbil);
	}

	public static ShipAbility getShipAbility(String shipAbilName)
	{
		return SHIP_ABILITIES.get(shipAbilName);
	}

	public static NavigableMap<String, ShipAbility> getShipAbilities()
	{
		return SHIP_ABILITIES;
	}

	public ShipAbility(String shipAbilName)
	{
		this.name = shipAbilName;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String _description)
	{
		this.description = _description;
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
}
