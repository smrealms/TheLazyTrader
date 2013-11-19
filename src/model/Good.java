package model;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Good
{
	public static final int NOTHING = 0;
	// public static final int WOOD = 1;
	// public static final int FOOD = 2;
	// public static final int ORE = 3;
	// public static final int PRECIOUS_METALS = 4;
	// public static final int SLAVES = 5;
	// public static final int TEXTILES = 6;
	// public static final int MACHINERY = 7;
	// public static final int CIRCUITRY = 8;
	// public static final int WEAPONS = 9;
	// public static final int COMPUTERS = 10;
	// public static final int LUXURY_ITEMS = 11;
	// public static final int NARCOTICS = 12;
	// public static final int COPPER = 18;
	// public static final int ENGINEERED_GOODS = 19;
	// public static final int MEDICINE = 22;
	// public static final int ANTIBIOTICS = 23;
	// public static final int BOOKS = 28;

	private static NavigableMap<Integer, String> GOOD_NAMES = new TreeMap<Integer, String>();
	private static NavigableMap<Integer, Integer> GOOD_VALUES = new TreeMap<Integer, Integer>();

	public static final int UNAVAILABLE = 0;
	public static final int SELLS = 1;
	public static final int BUYS = 2;
	public static final int ADD_BUY_SELL = 3;

	private int state = UNAVAILABLE;
	private final int goodId;
	private int distanceIndex = 0;
	
	static
	{
		addGood(Good.NOTHING,"Nothing",0);
	}

	public Good(int _goodId)
	{
		this.goodId = _goodId;
	}

	public Good(int _goodId, int _state)
	{
		this.goodId = _goodId;
		this.state = _state;
	}

	public Good(int _goodId, int _state, int _distance)
	{
		this.goodId = _goodId;
		this.state = _state;
		this.distanceIndex = _distance;
	}

	public void setState(int _state)
	{
		this.state = _state;
	}

	public void setDistanceIndex(int _distanceIndex)
	{
		this.distanceIndex = _distanceIndex;
	}

	public int getState()
	{
		return this.state;
	}

	public int getDistanceIndex()
	{
		return this.distanceIndex;
	}

	public static String getName(int _goodId)
	{
		return GOOD_NAMES.get(_goodId);
	}

	public static Map<Integer, String> getNames()
	{
		return GOOD_NAMES;
	}

	public static int getValue(int _goodId)
	{
		return GOOD_VALUES.get(_goodId);
	}

	public static void addGood(Integer key, String name, Integer value)
	{
		GOOD_NAMES.put(key, name);
		GOOD_VALUES.put(key, value);
	}

	public static int getId(String goodName)
	{
		for (int key : GOOD_NAMES.keySet()) {
			if (GOOD_NAMES.get(key).equals(goodName))
				return key;
		}
		return -1;
	}

	/**
	 * @return the goodId
	 */
	public int getGoodId()
	{
		return this.goodId;
	}

	public boolean matchesState(int _state)
	{
		if (_state == ADD_BUY_SELL)
			return this.state != UNAVAILABLE;
		return _state == this.state;
	}
	
	@Override
	public String toString()
	{
		return Good.getName(this.goodId);
	}

	public static void clearGoods()
	{
		GOOD_NAMES.clear();
		GOOD_VALUES.clear();
		addGood(Good.NOTHING,"Nothing",0);
	}
}
