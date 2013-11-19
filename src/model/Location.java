package model;

import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.TreeMap;

import model.ship.Ship;
import model.ship.ShipEquipment;

public class Location
{
	private static final NavigableMap<String, Location> locationSingletons = new TreeMap<String, Location>();
	private static final NavigableMap<String, NavigableMap<String, Location>> locationSingletonsByType = new TreeMap<String, NavigableMap<String, Location>>();
	private final String locationName;
	private String type;
	private ArrayList<Ship> ships = new ArrayList<Ship>();
	private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	private ArrayList<ShipEquipment> shipEquipment = new ArrayList<ShipEquipment>();

	public static Location getLocation(String locName)
	{
		Location l = locationSingletons.get(locName.toUpperCase());
		if (l != null)
			return l;

		l = new Location(locName);
		putLocation(l);
		return l;
	}

	public static NavigableMap<String, Location> getLocations()
	{
		return locationSingletons;
	}

	public static void putLocation(Location loc)
	{
		locationSingletons.put(loc.getName().toUpperCase(), loc);
		if (loc.getType() == null)
		{
			System.err.println("Location type failed for: " + loc.getName());
			return;
		}
		NavigableMap<String, Location> locTypeMap = getLocationByType(loc.getType());
		if (locTypeMap == null)
			locTypeMap = new TreeMap<String, Location>();
		locTypeMap.put(loc.getName().toUpperCase(), loc);
		locationSingletonsByType.put(loc.getType().toUpperCase(), locTypeMap);
	}

	public static NavigableMap<String, Location> getLocationByType(String locType)
	{
		return locationSingletonsByType.get(locType.toUpperCase());
	}

	public static NavigableMap<String, NavigableMap<String, Location>> getLocationsByType()
	{
		return locationSingletonsByType;
	}

	public Location(String locName)
	{
		this.locationName = locName;
	}

	public String getName()
	{
		return locationName;
	}

	/**
	 * @return the shipEquipment
	 */
	public ArrayList<ShipEquipment> getShipEquipment()
	{
		return this.shipEquipment;
	}

	/**
	 * @param _shipEquipment
	 *            the shipEquipment to set
	 */
	public void setShipEquipment(ArrayList<ShipEquipment> _shipEquipment)
	{
		this.shipEquipment = _shipEquipment;
	}

	/**
	 * @param _shipEquipment
	 *            the shipEquipment to add
	 */
	public void addShipEquipment(ShipEquipment _shipEquipment)
	{
		this.shipEquipment.add(_shipEquipment);
	}

	public boolean hasShipEquipment()
	{
		return this.shipEquipment.isEmpty();
	}

	/**
	 * @return the ships
	 */
	public ArrayList<Ship> getShips()
	{
		return this.ships;
	}

	/**
	 * @param _ships
	 *            the ships to set
	 */
	public void setShips(ArrayList<Ship> _ships)
	{
		this.ships = _ships;
	}

	/**
	 * @param _ship
	 *            the ship to add
	 */
	public void addShip(Ship _ship)
	{
		this.ships.add(_ship);
	}

	public boolean hasShip()
	{
		return this.ships.isEmpty();
	}

	/**
	 * @return the weapons
	 */
	public ArrayList<Weapon> getWeapons()
	{
		return this.weapons;
	}

	/**
	 * @param _weapons
	 *            the weapons to set
	 */
	public void setWeapons(ArrayList<Weapon> _weapons)
	{
		this.weapons = _weapons;
	}

	/**
	 * @param _weapon
	 *            the weapon to add
	 */
	public void addWeapon(Weapon _weapon)
	{
		this.weapons.add(_weapon);
	}

	public boolean hasWeapon()
	{
		return this.weapons.isEmpty();
	}

	public boolean hasX(Object x)
	{
		if (x instanceof ShipEquipment)
		{
			return this.shipEquipment.contains(x);
		}
		if (x instanceof Ship)
		{
			return this.ships.contains(x);
		}
		if (x instanceof Weapon)
		{
			return this.weapons.contains(x);
		}
		if(x instanceof String)
		{
			String s = (String)x;
			return s.equals(this.getType());
		}
		return false;
	}

	@Override
	public String toString()
	{
		return this.locationName;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * @param _type
	 *            the type to set
	 */
	public void setType(String _type)
	{
		this.type = _type;
	}

	public static void clearLocations()
	{
		locationSingletons.clear();
		locationSingletonsByType.clear();
	}
}
