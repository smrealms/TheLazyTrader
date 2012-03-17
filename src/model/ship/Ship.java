package model.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import model.HasRace;
import model.Race;

public class Ship implements HasRace
{
	private static NavigableMap<String, Ship> SHIPS = new TreeMap<String, Ship>();
	private static NavigableMap<String, NavigableMap<String, Ship>> SHIPS_BY_RACE = new TreeMap<String, NavigableMap<String, Ship>>();

	private String name;
	private int race;
	private int cost;
	private int turnRate;
	private int power;
	private int manu;
	private int hardpoints;
//	private int shields;
//	private int armour;
//	private int cargo;
//	private int combats;
//	private int scouts;
//	private int mines;
	private Map<ShipEquipment,Integer> equipment = new HashMap<ShipEquipment,Integer>();
	private ArrayList<ShipAbility> abilities = new ArrayList<ShipAbility>();
	private Restriction restriction = new Restriction("");


	public static void addShip(Ship ship)
	{
		SHIPS.put(ship.getName(), ship);

		NavigableMap<String, Ship> shipRaceMap = SHIPS_BY_RACE.get(ship.getRaceName());
		if (shipRaceMap == null)
			shipRaceMap = new TreeMap<String, Ship>();
		shipRaceMap.put(ship.getName(), ship);
		SHIPS_BY_RACE.put(ship.getRaceName(), shipRaceMap);
	}

	public static Ship getShip(String shipName)
	{
		return SHIPS.get(shipName);
	}

	public static NavigableMap<String, Ship> getShips()
	{
		return SHIPS;
	}

	public static NavigableMap<String, Ship> getShipsByRace(String raceName)
	{
		return SHIPS_BY_RACE.get(raceName);
	}

	public static NavigableMap<String, NavigableMap<String, Ship>> getShipsByRace()
	{
		return SHIPS_BY_RACE;
	}

	public Ship(String shipName)
	{
		name = shipName;
	}

//	/**
//	 * @return The armour that ship can have
//	 */
//	public int getArmour()
//	{
//		return this.armour;
//	}
//
//	/**
//	 * @param armour
//	 *            The armour to set
//	 */
//	public void setArmour(int _armour)
//	{
//		this.armour = _armour;
//	}
//
//	/**
//	 * @return the cargo that ship can have
//	 */
//	public int getCargo()
//	{
//		return this.cargo;
//	}
//
//	/**
//	 * @param cargo
//	 *            the cargo to set
//	 */
//	public void setCargo(int _cargo)
//	{
//		this.cargo = _cargo;
//	}
//
//	/**
//	 * @return the combats that ship can have
//	 */
//	public int getCombats()
//	{
//		return this.combats;
//	}
//
//	/**
//	 * @param combats
//	 *            the combats to set
//	 */
//	public void setCombats(int _combats)
//	{
//		this.combats = _combats;
//	}

	/**
	 * @return the cost of the ship
	 */
	public int getCost()
	{
		return this.cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(int _cost)
	{
		this.cost = _cost;
	}

	/**
	 * @param power
	 *            the power to set
	 */
	public void setPower(int _power)
	{
		this.power = _power;
	}

	/**
	 * @return the power that the ship can have
	 */
	public int getPower()
	{
		return this.power;
//		switch (this.hardpoints)
//		{
//			case 0:
//				return 0;
//			case 1:
//				return 5;
//			case 2:
//				return 9;
//			case 3:
//				return 13;
//			case 4:
//				return 16;
//			case 5:
//				return 19;
//			case 6:
//				return 22;
//			case 7:
//				return 24;
//			case 8:
//				return 26;
//			case 9:
//				return 28;
//			case 10:
//				return 30;
//			case 11:
//				return 31;
//			case 12:
//				return 32;
//			case 13:
//				return 33;
//			case 14:
//				return 34;
//			case 15:
//				return 35;
//			case 16:
//				return 36;
//			default:
//				return -1;
//		}
	}

	/**
	 * @return the hardpoints that ship can have
	 */
	public int getHardpoints()
	{
		return this.hardpoints;
	}

	/**
	 * @param hardpoints
	 *            the hardpoints to set
	 */
	public void setHardpoints(int _hardpoints)
	{
		this.hardpoints = _hardpoints;
	}

//	/**
//	 * @return the mines that ship can have
//	 */
//	public int getMines()
//	{
//		return this.mines;
//	}
//
//	/**
//	 * @param mines
//	 *            the mines to set
//	 */
//	public void setMines(int _mines)
//	{
//		this.mines = _mines;
//	}

	/**
	 * @return the name of ship
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

	/**
	 * @return the race of the ship
	 */
	public int getRace()
	{
		return this.race;
	}

	/**
	 * @return the race name of the ship
	 */
	public String getRaceName()
	{
		return Race.getName(this.getRace());
	}

	/**
	 * @param race
	 *            the race to set
	 */
	public void setRace(int _race)
	{
		this.race = _race;
	}

	/**
	 * @return the turnRate of the ship
	 */
	public int getTurnRate()
	{
		return this.turnRate;
	}

	/**
	 * @param turnRate
	 *            the turnRate to set
	 */
	public void setTurnRate(int _turnRate)
	{
		this.turnRate = _turnRate;
	}

//	/**
//	 * @return the scouts that the ship can have
//	 */
//	public int getScouts()
//	{
//		return this.scouts;
//	}
//
//	/**
//	 * @param scouts
//	 *            the scouts to set
//	 */
//	public void setScouts(int _scouts)
//	{
//		this.scouts = _scouts;
//	}
//
//	/**
//	 * @return the shields that the ship can have
//	 */
//	public int getShields()
//	{
//		return this.shields;
//	}
//
//	/**
//	 * @param shields
//	 *            the shields to set
//	 */
//	public void setShields(int _shields)
//	{
//		this.shields = _shields;
//	}

	public void addEquipment(ShipEquipment equipmentName, int value)
	{
		this.equipment.put(equipmentName,value);
	}

	public boolean hasEquipment(String equipmentName)
	{
		return this.equipment.containsKey(equipmentName);
	}

	public Map<ShipEquipment,Integer> getEquipments()
	{
		return this.equipment;
	}

	public void addAbility(ShipAbility abil)
	{
		this.abilities.add(abil);
	}

	public boolean hasAbility(String abilName)
	{
		return this.abilities.contains(abilName);
	}

	public ArrayList<ShipAbility> getAbilities()
	{
		return this.abilities;
	}

	/**
	 * @return the manu
	 */
	public int getManu()
	{
		return this.manu;
	}

	/**
	 * @param manu
	 *            the manu to set
	 */
	public void setManu(int _manu)
	{
		this.manu = _manu;
	}

	/**
	 * @return the restriction
	 */
	public Restriction getRestriction()
	{
		return this.restriction;
	}

	/**
	 * @param restriction
	 *            the restriction to set
	 */
	public void setRestriction(Restriction _restriction)
	{
		this.restriction = _restriction;
	}

	public String toString()
	{
		return this.name;
	}

	public static void clearShips()
	{
		SHIPS.clear();
		SHIPS_BY_RACE.clear();
	}
}
