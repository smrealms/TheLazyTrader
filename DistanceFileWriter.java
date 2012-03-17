package controller.fileaccess;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import model.Distance;

public class DistanceFileWriter
{
	
	public static void writeDistances(FileWriter mapFile, Map<Integer,Map<Integer,Distance>> distances)
	{
		try {
		int i=1;
		mapFile.write("SMR1.5 Distance File v 1.01\r\n");
		Iterator<Entry<Integer, Map<Integer, Distance>>> iter = distances.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<Integer, Map<Integer, Distance>> e = iter.next();
			Iterator<Entry<Integer, Distance>> dIter = e.getValue().entrySet().iterator();
			while(dIter.hasNext())
			{
				Entry<Integer, Distance> e2 = dIter.next();
				Distance d = e2.getValue();
				mapFile.write("[DistanceNumber="+i+"]\r\nFirstSector="+e.getKey()+"\r\nSecondSector="+e2.getKey()+"\r\nDistance="+d.getDistance()+","+d.getNumWarps()+"\r\n");
				i++;

			}
		}
			mapFile.flush();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
