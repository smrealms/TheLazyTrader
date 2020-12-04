package model;

import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.TreeMap;

import model.ship.Restriction;

public class Weapon implements HasRace
{
	private static final NavigableMap<String, Weapon> WEAPONS = new TreeMap<String, Weapon>();
	private static final NavigableMap<Integer, NavigableMap<String, Weapon>> WEAPONS_BY_POWER = new TreeMap<Integer, NavigableMap<String, Weapon>>();

	private String name;
	private int race;
	private int cost;
	private int shieldDamage;
	private int armourDamage;
	private int accuracy;
	private int powerLevel;
	private final ArrayList<Restriction> restrictions = new ArrayList<Restriction>();

	public Weapon(String _name)
	{
		this.name = _name;
	}

	public static void addWeapon(Weapon w)
	{
		WEAPONS.put(w.getName(), w);

		NavigableMap<String, Weapon> weaponPowerMap = WEAPONS_BY_POWER.get(w.getPowerLevel());
		if (weaponPowerMap == null)
			weaponPowerMap = new TreeMap<String, Weapon>();
		weaponPowerMap.put(w.getName(), w);
		WEAPONS_BY_POWER.put(w.getPowerLevel(), weaponPowerMap);
	}

	public static Weapon getWeapon(String weaponName)
	{
		return WEAPONS.get(weaponName);
	}

	public static NavigableMap<String, Weapon> getWeapons()
	{
		return WEAPONS;
	}

	public static NavigableMap<String, Weapon> getWeaponsByPower(int powerLevel)
	{
		return WEAPONS_BY_POWER.get(powerLevel);
	}

	public static NavigableMap<Integer, NavigableMap<String, Weapon>> getWeaponsByPower()
	{
		return WEAPONS_BY_POWER;
	}

	/**
	 * @return the armour
	 */
	public int getArmourDamage()
	{
		return this.armourDamage;
	}

	/**
	 * @param _armour
	 *            the armour to set
	 */
	public void setArmourDamage(int _armour)
	{
		this.armourDamage = _armour;
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

	/**
	 * @return the powerLevel
	 */
	public int getPowerLevel()
	{
		return this.powerLevel;
	}

	/**
	 * @param _powerLevel
	 *            the powerLevel to set
	 */
	public void setPowerLevel(int _powerLevel)
	{
		this.powerLevel = _powerLevel;
	}

	/**
	 * @return the race
	 */
	@Override
	public int getRace()
	{
		return this.race;
	}

	/**
	 * @param _race
	 *            the race to set
	 */
	public void setRace(int _race)
	{
		this.race = _race;
	}

	/**
	 * @return the restriction
	 */
	public ArrayList<Restriction> getRestrictions()
	{
		return this.restrictions;
	}

	/**
	 * @param _restriction
	 *            the restriction to set
	 */
	public void addRestriction(Restriction _restriction)
	{
		this.restrictions.add(_restriction);
	}

	/**
	 * @return the shield
	 */
	public int getShieldDamage()
	{
		return this.shieldDamage;
	}

	/**
	 * @param _shield
	 *            the shield to set
	 */
	public void setShieldDamage(int _shield)
	{
		this.shieldDamage = _shield;
	}

	/**
	 * @return the accuracy
	 */
	public int getAccuracy()
	{
		return this.accuracy;
	}

	/**
	 * @param _accuracy
	 *            the accuracy to set
	 */
	public void setAccuracy(int _accuracy)
	{
		this.accuracy = _accuracy;
	}

	@Override
	public String toString()
	{
		return this.name;
	}

	public static void clearWeapons()
	{
		WEAPONS.clear();
		WEAPONS_BY_POWER.clear();
	}
}
