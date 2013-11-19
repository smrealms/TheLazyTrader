package model.ship;

import java.util.NavigableMap;
import java.util.TreeMap;

public class ShipEquipment
{
	String name;
	int cost;
	String description;

	private static final NavigableMap<String, ShipEquipment> SHIP_EQUIPMENTS = new TreeMap<String, ShipEquipment>();

	public static void addShipEquipment(ShipEquipment shipEquip)
	{
		SHIP_EQUIPMENTS.put(shipEquip.getName(), shipEquip);
	}

	public static ShipEquipment getShipEquipment(String shipEquipName)
	{
		return SHIP_EQUIPMENTS.get(shipEquipName);
	}

	public static NavigableMap<String, ShipEquipment> getShipEquipments()
	{
		return SHIP_EQUIPMENTS;
	}

	public ShipEquipment(String shipEquipName)
	{
		this.name = shipEquipName;
	}

	/**
	 * @return the cost
	 */
	public int getCost()
	{
		return this.cost;
	}

	/**
	 * @param _cost
	 *            the cost to set
	 */
	public void setCost(int _cost)
	{
		this.cost = _cost;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @param _description
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
	 * @param _name
	 *            the name to set
	 */
	public void setName(String _name)
	{
		this.name = _name;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
