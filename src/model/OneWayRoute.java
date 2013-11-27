package model;

import model.preferences.PlayerPreferences;
import model.preferences.RoutePreferences;
import settings.Settings;

public class OneWayRoute extends Route implements Comparable<OneWayRoute> {
	private int sellSectorId, buySectorId;
	private int sellDi, buyDi;
	private Distance distance;
	private int goodId;
	private int sellPortRace;
	private int buyPortRace;

	public OneWayRoute(int _sellSectorId, int _buySectorId, int _sellPortRace, int _buyPortRace, int _sellDi, int _buyDi, Distance _distance, int _goodId) {
		this.sellSectorId = _sellSectorId;
		this.buySectorId = _buySectorId;
		this.sellDi = _sellDi;
		this.buyDi = _buyDi;
		this.distance = _distance;
		this.goodId = _goodId;
		this.sellPortRace = _sellPortRace;
		this.buyPortRace = _buyPortRace;
	}

	public void setSellSectorId(int value) {
		this.sellSectorId = value;
	}

	public void setBuySectorId(int value) {
		this.buySectorId = value;
	}

	public void setSellPortRace(int value) {
		this.sellPortRace = value;
	}

	public void setBuyPortRace(int value) {
		this.buyPortRace = value;
	}

	public void setSellDi(int value) {
		this.sellDi = value;
	}

	public void setBuyDi(int value) {
		this.buyDi = value;
	}

	public void setDistance(Distance value) {
		this.distance = value;
	}

	public void setGoodId(int value) {
		this.goodId = value;
	}

	public int getSellSectorId() {
		return this.sellSectorId;
	}

	public int getBuySectorId() {
		return this.buySectorId;
	}

	public int getSellPortRace() {
		return this.sellPortRace;
	}

	public int getBuyPortRace() {
		return this.buyPortRace;
	}

	public int getSellDi() {
		return this.sellDi;
	}

	public int getBuyDi() {
		return this.buyDi;
	}

	public Distance getDistance() {
		return this.distance;
	}

	public int getGoodId() {
		return this.goodId;
	}

	@Override
	public double getMoneyMultiplierSum() {
		// TODO sellDi stuff and check accuracy of formula
		double buyRelFactor = 1;
		double sellRelFactor = 1;
		if (RoutePreferences.useRelationsFactor()) {
			int relations = Math.min(PlayerPreferences.getRelationsForRace(this.buyPortRace), Settings.MAX_MONEY_RELATIONS);
			buyRelFactor = (relations + 350) / 8415.0;

			relations = Math.min(PlayerPreferences.getRelationsForRace(this.sellPortRace), Settings.MAX_MONEY_RELATIONS);
			sellRelFactor = (2 - (relations + 50)) / 850.0 * ((relations + 350)/57840);
		}
		int goodValue = Good.getValue(this.goodId);
		double buyPrice = goodValue * 0.6 * Math.pow(this.buyDi + .5, 1.8) * buyRelFactor;
		double sellPrice = goodValue * 0.7 * Math.pow(this.sellDi, 1.84) * sellRelFactor /*
																						* * (1 +
																						* (10 -
																						* $port_lvl) /
																						* 50)
																						*/;
		return buyPrice - sellPrice;
	}

	@Override
	public double getExpMultiplierSum() {
		return this.buyDi + this.sellDi;
	}

	@Override
	public double getTurnsForRoute() {
		double tradeTurns;
		if (this.goodId == Good.NOTHING)
			tradeTurns = 0;
		else
			tradeTurns = 2 * Settings.TURNS_PER_TRADE;
		return this.distance.getTurns() + tradeTurns;
	}

	@Override
	public int compareTo(OneWayRoute compare)
	{
		if (this.equals(compare))
			return 0;
		if (this.getOverallExpMultiplier() > compare.getOverallExpMultiplier())
			return 1;
		return -1;
	}
	
	@Override
	public boolean containsPort(int sectorID)
	{
		return this.sellSectorId == sectorID || this.buySectorId == sectorID;
	}

	@Override
	public OneWayRoute getForwardRoute()
	{
		return null;
	}

	@Override
	public OneWayRoute getReturnRoute()
	{
		return null;
	}

	@Override
	public String getRouteString(DisplayType displayType) {
		switch(displayType) {
			case TheLazyTradeBBCode:
				return Sector.getBBCode(this.sellSectorId) + " (" + Race.getBBCode(this.sellPortRace) + ") buy " + Good.getName(this.goodId) + " at " + this.sellDi + "x to sell at (Distance: " + this.distance.getDistance() + (this.distance.getNumWarps() > 0 ? " + " + this.distance.getNumWarps() + " warps) " : ") ") + Sector.getBBCode(this.buySectorId) + " (" + Race.getBBCode(this.buyPortRace) + ") at " + this.buyDi + "x";
			case TheLazyTrader:
			default:
				return this.sellSectorId + " (" + Race.getName(this.sellPortRace) + ") buy " + Good.getName(this.goodId) + " at " + this.sellDi + "x to sell at (Distance: " + this.distance.getDistance() + (this.distance.getNumWarps() > 0 ? " + " + this.distance.getNumWarps() + " warps) " : ") ") + this.buySectorId + " (" + Race.getName(this.buyPortRace) + ") at " + this.buyDi + "x";

		}
	}
}
