package model;

public abstract class Route
{
	public double getOverallExpMultiplier()
	{
		return (this.getForwardRoute().getMultiplierSum() + this.getReturnRoute().getMultiplierSum()) / this.getTurnsForRoute();
	}

	public double getOverallMoneyMultiplier()
	{
		return (this.getForwardRoute().getOverallMoneyMultiplier() + this.getReturnRoute().getOverallMoneyMultiplier()) / 2;
	}

	public double getTurnsForRoute()
	{
		return this.getForwardRoute().getTurnsForRoute() + this.getReturnRoute().getTurnsForRoute();
	}

	public double getMultiplierSum()
	{
		return this.getForwardRoute().getMultiplierSum() + this.getReturnRoute().getMultiplierSum();
	}

	public abstract Route getForwardRoute();

	public abstract Route getReturnRoute();

	public String getRouteString()
	{
		return this.getForwardRoute().getRouteString() + "\r\n" + this.getReturnRoute().getRouteString();
	}
}
