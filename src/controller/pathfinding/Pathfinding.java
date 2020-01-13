package controller.pathfinding;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.ArrayList;
import java.util.List;

import settings.Settings;

import model.Connection;
import model.Distance;
import model.Port;
import model.Sector;

public class Pathfinding
{
	public static TIntObjectMap<TIntObjectMap<Distance>> calculatePortToPortDistances(
			Sector[] sectors, long lowLimit, long highLimit, long distanceLimit)
	{
		TIntObjectMap<TIntObjectMap<Distance>> distances = new TIntObjectHashMap<TIntObjectMap<Distance>>();
		for (Sector sec : sectors)
		{
			if (sec != null)
			{
				if (sec.getSectorID() >= lowLimit
						&& sec.getSectorID() <= highLimit)
				{
					if (sec.hasPort())
					{
						distances.put(sec.getSectorID(), Pathfinding
								.findDistanceToOtherPorts(sectors, sec
										.getSectorID(), lowLimit, highLimit,
										distanceLimit));
					}
				}
			}
		}
		return distances;
	}

	public static TIntObjectMap<Distance> findDistanceToOtherPorts(Sector[] sectors, int sectorId, long lowLimit, long highLimit, long distanceLimit)
	{
		return findDistanceToX(Port.class, sectors, sectorId, lowLimit, highLimit, distanceLimit, false);
	}

	public static TIntObjectMap<Distance> findDistanceToX(Object x,
			Sector[] sectors, int sectorId, long lowLimit, long highLimit,
			long distanceLimit, boolean useFirst)
	{
		final int warpAddIndex = Settings.TURNS_WARP_SECTOR_EQUIVALENCE-1;

		Sector checkSector;
		TIntObjectMap<Distance> distances = new TIntObjectHashMap<Distance>();
		int sectorsTravelled = 0;
		boolean[] visitedSectors = new boolean[sectors.length];
		visitedSectors[sectorId] = true;

		List<List<Distance>> distanceQ = new ArrayList<List<Distance>>();
		for(int i=0;i<=Settings.TURNS_WARP_SECTOR_EQUIVALENCE;i++)
			distanceQ.add(new ArrayList<Distance>());
		//Warps first as a slight optimisation due to how visitedSectors is set.
		for (int i = 0; i < sectors[sectorId].numWarps(); i++)
		{
			int nextSector = sectors[sectorId].getWarps().get(i).getTargetSector();
			Distance d = new Distance(sectorId);
			d.addWarpToPath(nextSector);
			distanceQ.get(warpAddIndex).add(d);
		}
		for (int i = 0; i < sectors[sectorId].numConnections(); i++)
		{
			int nextSector = sectors[sectorId].getConnections().get(i).getTargetSector();
			visitedSectors[nextSector] = true;
			Distance d = new Distance(sectorId);
			d.addToPath(nextSector);
			distanceQ.get(0).add(d);
		}
		List<Distance> q;
		int maybeWarps=0;
		while(maybeWarps<=Settings.TURNS_WARP_SECTOR_EQUIVALENCE)
		{
			sectorsTravelled++;
			if (sectorsTravelled > distanceLimit)
				return distances;
			distanceQ.add(new ArrayList<Distance>());

			if((q = distanceQ.remove(0)).isEmpty())
			{
				maybeWarps++;
				continue;
			}
			maybeWarps=0;

			for (Distance distance : q) {
				visitedSectors[distance.getEndSectorId()] = true; // This is here for warps, because they are delayed visits if we set this before the actual visit we'll get sectors marked as visited long before they are actually visited - causes problems when it's quicker to walk to the warp exit than to warp there.
																// We still need to mark walked sectors as visited before we go to each one otherwise we get a huge number of paths being checked twice (up then left, left then up are essentially the same but if we set up-left as visited only when we actually check it then it gets queued up twice - nasty)
				checkSector = sectors[distance.getEndSectorId()];
				if (checkSector.getSectorID() >= lowLimit
						&& checkSector.getSectorID() <= highLimit)
				{
					if (checkSector.hasX(x))
					{
						distances.put(checkSector.getSectorID(), distance);
						if(useFirst)
							return distances;
					}
					//Warps first as a slight optimisation due to how visitedSectors is set.
					for (int i = 0; i < checkSector.numWarps(); i++)
					{
						Connection c = checkSector.getWarps().get(i);
						if (visitedSectors[c.getTargetSector()])
							continue;

						Distance cloneDistance = distance.clone();
						cloneDistance.addWarpToPath(c.getTargetSector());
						distanceQ.get(warpAddIndex).add(cloneDistance);
					}
					for (int i = 0; i < checkSector.numConnections(); i++)
					{
						Connection c = checkSector.getConnections().get(i);
						if (visitedSectors[c.getTargetSector()])
							continue;
						visitedSectors[c.getTargetSector()] = true;

						Distance cloneDistance = distance.clone();
						cloneDistance.addToPath(c.getTargetSector());
						distanceQ.get(0).add(cloneDistance);
					}
				}
			}
		}
		// SLOW System.gc();
		return distances;
	}
}
