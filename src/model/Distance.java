package model;

import gnu.trove.list.array.TIntArrayList;

import settings.Settings;

public class Distance implements Comparable<Distance>
{
	private int distance = -1; //First sector added will be the start and a distance of 0
	private int numWarps = 0;
	private TIntArrayList path = new TIntArrayList();

	public Distance(int _startSectorId)
	{
		this.addToPath(_startSectorId);
	}

	private Distance(int _distance, int _numWarps, TIntArrayList _path)
	{
		this.distance = _distance;
		this.numWarps = _numWarps;
		this.path = _path;
	}

	public void setDistance(int _distance)
	{
		this.distance = _distance;
	}

	public void setNumWarps(int _numWarps)
	{
		this.numWarps = _numWarps;
	}

	protected void incrementDistance()
	{
		this.distance++;
	}

	protected void incrementNumWarps()
	{
		this.numWarps++;
	}

	public int getDistance()
	{
		return this.distance;
	}

	public int getNumWarps()
	{
		return this.numWarps;
	}

	@Override
	public Distance clone()
	{
		return new Distance(this.distance, this.numWarps, new TIntArrayList(this.path));
	}

	public double getTurns()
	{
		return this.distance * Settings.TURNS_PER_SECTOR + this.numWarps * Settings.TURNS_PER_WARP;
	}

	/**
	 * @return the endSectorId
	 */
	public int getEndSectorId()
	{
		return this.path.get(this.path.size()-1);
	}

	/**
	 * @return the startSectorId
	 */
	public int getStartSectorId()
	{
		return this.path.get(0);
	}

	@Override
	public int compareTo(Distance d)
	{
		if (this.equals(d))
			return 0;
		else if (d.getTurns() > this.getTurns())
			return -1;
		return 1;
	}

	/**
	 * @return the path
	 */
	public TIntArrayList getPath()
	{
		return this.path;
	}

	/**
	 * @param nextSector
	 *            the nextSector to set
	 */
	public void addToPath(int nextSector)
	{
		this.incrementDistance();
		this.path.add(nextSector);
	}

	public void addWarpToPath(int nextSector)
	{
		this.incrementNumWarps();
		this.path.add(nextSector);
	}
}