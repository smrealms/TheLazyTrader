package model;

public class MultiplePortRoute extends Route
{
	private Route forwardRoute;
	private Route returnRoute;

	public MultiplePortRoute(Route _forwardRoute, Route _returnRoute)
	{
		this.forwardRoute = _forwardRoute;
		this.returnRoute = _returnRoute;
	}

	public void setForwardRoute(Route r)
	{
		this.forwardRoute = r;
	}

	public void setReturnRoute(Route r)
	{
		this.returnRoute = r;
	}

	@Override
	public Route getForwardRoute()
	{
		return this.forwardRoute;
	}

	@Override
	public Route getReturnRoute()
	{
		return this.returnRoute;
	}
}
