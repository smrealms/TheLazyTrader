package model;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class Port
{
	private int portLevel;
	private int portRace;
	private final TIntObjectMap<Good> goods = new TIntObjectHashMap<Good>();

	public Port()
	{
		// for(int i=0;i<goods.length;i++)
		// {
		// goods[i]=new Good(i);
		// }
	}

	public Port(int _portLevel)
	{
		// Port();
		this.portLevel = _portLevel;
	}

	public void setPortLevel(int _portLevel)
	{
		this.portLevel = _portLevel;
	}

	public void setPortRace(int _portRace)
	{
		this.portRace = _portRace;
	}

	public void setGoodDistance(int goodId, int distanceIndex)
	{
		this.goods.get(goodId).setDistanceIndex(distanceIndex);
	}

	public void addPortBuy(int _goodId)
	{
		this.goods.put(_goodId, new Good(_goodId, Good.BUYS));
	}

	public void addPortBuy(int _goodId, int distance)
	{
		this.goods.put(_goodId, new Good(_goodId, Good.BUYS, distance));
	}

	public void addPortSell(int _goodId)
	{
		this.goods.put(_goodId, new Good(_goodId, Good.SELLS));
	}

	public void addPortSell(int _goodId, int distance)
	{
		this.goods.put(_goodId, new Good(_goodId, Good.SELLS, distance));
	}

	public int getGoodStatus(int _goodId)
	{
		Good g = this.goods.get(_goodId);
		if (g == null)
			return Good.UNAVAILABLE;
		return g.getState();
	}

	public int getPortLevel()
	{
		return this.portLevel;
	}

	public int getPortRace()
	{
		return this.portRace;
	}

	public int getGoodDistance(int _goodId)
	{
		Good g = this.goods.get(_goodId);
		if (g == null)
			return 0;
		return g.getDistanceIndex();
	}

	public TIntObjectMap<Good> getGoods()
	{
		return this.goods;
	}

	public boolean hasX(Object x)
	{
		if (x instanceof Good)
		{
			Good thisGood = this.goods.get(((Good) x).getGoodId());
			if(thisGood!=null)
				return thisGood.matchesState(((Good) x).getState());
		}
		if(x instanceof String)
		{
			String s = ((String)x).toUpperCase();
			if(s.equals("GOODS"))
				return !this.getGoods().isEmpty();
			if(s.equals("BOUGHT")||s.equals("SOLD")||s.equals("EITHER"))
			{
				for (Good g : (Good[]) this.getGoods().values()) {
					int state = g.getState();
					return (s.equals("BOUGHT")&&state==Good.BUYS) || (s.equals("SOLD")&&state==Good.SELLS) || (s.equals("EITHER")&&(state==Good.SELLS||state==Good.BUYS));
				}
			}
		}
		return false;
	}
}
