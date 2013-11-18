package controller;

import java.util.NavigableMap;
import java.util.TreeMap;

import model.Good;
import model.Race;
import model.Sector;

public class GoodFinder
{
	public static void findGood(Sector[] sectors, int goodId, int lowLimit, int highLimit)
	{
		NavigableMap<Integer, String> dS = new TreeMap<Integer, String>();
		NavigableMap<Integer, String> dB = new TreeMap<Integer, String>();
		for (Sector sec : sectors)
		{
			if (sec != null)
			{
				if (sec.getSectorID() >= lowLimit && sec.getSectorID() <= highLimit)
				{
					if (sec.hasPort())
					{
						if (sec.getPort().getGoodStatus(goodId) == Good.BUYS)
							dS.put(sec.getPort().getGoodDistance(goodId), sec.getSectorID() + " (" + Race.getName(sec.getPort().getPortRace()) + ") buys " + Good.getName(goodId) + " at distance index: " + sec.getPort().getGoodDistance(goodId));
						else if (sec.getPort().getGoodStatus(goodId) == Good.SELLS)
							dB.put(sec.getPort().getGoodDistance(goodId), sec.getSectorID() + " (" + Race.getName(sec.getPort().getPortRace()) + ") sells " + Good.getName(goodId) + " at distance index: " + sec.getPort().getGoodDistance(goodId));
					}
				}
			}
		}
		for (String s : dS.descendingMap().values()) {
			System.out.println(s);
		}
		System.out.println();
		System.out.println();
		for (String s : dB.descendingMap().values()) {
			System.out.println(s);
		}
	}

	public static void findGood(Sector[] sectors, int goodId, int lowLimit)
	{
		findGood(sectors, goodId, lowLimit, Integer.MAX_VALUE);
	}

	public static void findGood(Sector[] sectors, int goodId)
	{
		findGood(sectors, goodId, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
}
