package controller.pathfinding;

import java.util.ArrayList;
import java.util.List;

import settings.Settings;

import model.Good;
import model.Sector;

public class DiCalculator
{
	public static Sector[] generateAllDistanceIndexes(Sector[] sectors)
	{
		return generateDistanceIndexesForGood(sectors, -1);
	}

	public static Sector[] generateDistanceIndexesForGood(Sector[] sectors, int goodId)
	{
//		long t1 = System.nanoTime();
		for (int i = 0; i < sectors.length; i++)
		{
			if (sectors[i] != null)
			{
				if (sectors[i].hasPort())
				{
					sectors = generateDistanceIndexesForPort(sectors, i, goodId);
				}
			}
		}
//		long t2 = System.nanoTime();
//		System.out.println("Time taken:" + ((t2-t1)/1000000000.0));
		return sectors;
	}

	public static Sector[] generateDistanceIndexesForPort(Sector[] sectors, int sectorId)
	{
		return generateDistanceIndexesForPort(sectors, sectorId, -1);
	}

	public static Sector[] generateDistanceIndexesForPort(Sector[] sectors, int sectorId, int goodIdOnly)
	{
		for (int goodId : Good.getNames().keySet()) {
			if (goodIdOnly == -1 || goodIdOnly == goodId) {
				if (sectors[sectorId].getPort().getGoodStatus(goodId) == Good.BUYS)
					sectors[sectorId].getPort().setGoodDistance(goodId, findNearestGoodWithStatus(sectors, sectorId, goodId, Good.SELLS));
				else if (sectors[sectorId].getPort().getGoodStatus(goodId) == Good.SELLS)
					sectors[sectorId].getPort().setGoodDistance(goodId, findNearestGoodWithStatus(sectors, sectorId, goodId, Good.BUYS));
			}
		}
		return sectors;
	}

	private static int findNearestGoodWithStatus(Sector[] sectors, int sectorId, int goodId, int status)
	{
//		Iterator<Distance> nearest = Pathfinding.findDistanceToX(new Good(goodId, status), sectors, sectorId, Long.MIN_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, true).values().iterator();
//		if(nearest.hasNext())
//			return nearest.next().getDistance();
//		return -1;
		Sector checkSector;
		int distanceIndex = 0;
		boolean[] visitedSectors = new boolean[sectors.length];
		
		List<List<Integer>> distanceQ = new ArrayList<List<Integer>>();
		for(int i=0;i<=Settings.TURNS_WARP_SECTOR_EQUIVALENCE;i++)
			distanceQ.add(new ArrayList<Integer>());
		//Warps first as a slight optimisation due to how visitedSectors is set.
		for (int i = 0; i < sectors[sectorId].numWarps(); i++)
		{
			distanceQ.get(Settings.TURNS_WARP_SECTOR_EQUIVALENCE).add(sectors[sectorId].getWarps().get(i).getTargetSector());
		}
		for (int i = 0; i < sectors[sectorId].numConnections(); i++)
		{
			int c = sectors[sectorId].getConnections().get(i).getTargetSector();
			visitedSectors[c] = true;
			distanceQ.get(0).add(c);
		}
		List<Integer> q;
		int maybeWarps=0;
		while(maybeWarps<=Settings.TURNS_WARP_SECTOR_EQUIVALENCE)
		{
			distanceIndex++;
			distanceQ.add(new ArrayList<Integer>());
			
			if((q = distanceQ.remove(0)).isEmpty())
			{
				maybeWarps++;
				continue;
			}
			maybeWarps=0;
			
			for (int checkSectorId : q)
			{
				checkSector = sectors[checkSectorId];
				visitedSectors[checkSector.getSectorID()] = true; // This is here for warps, because they are delayed visits if we set this before the actual visit we'll get sectors marked as visited long before they are actually visited - causes problems when it's quicker to walk to the warp exit than to warp there.
//				 We still need to mark walked sectors as visited before we go to each one otherwise we get a huge number of paths being checked twice (up then left, left then up are essentially the same but if we set up-left as visited only when we actually check it then it gets queued up twice - nasty)

				if (checkSector.hasPort())
				{
					if (checkSector.getPort().getGoodStatus(goodId) == status)
					{
						return distanceIndex;
					}
				}
				//Warps first as a slight optimisation due to how visitedSectors is set.
				for (int i = 0; i < checkSector.numWarps(); i++)
				{
					int c = checkSector.getWarps().get(i).getTargetSector();
					if (visitedSectors[c])
						continue;
					distanceQ.get(Settings.TURNS_WARP_SECTOR_EQUIVALENCE).add(c);
				}
				for (int i = 0; i < checkSector.numConnections(); i++)
				{
					int c = checkSector.getConnections().get(i).getTargetSector();
					if (visitedSectors[c])
						continue;
					visitedSectors[c] = true;
					distanceQ.get(0).add(c);
				}
			}
		}
		return -1;
	}
}
