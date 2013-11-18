package controller.fileaccess;


import java.io.FileWriter;
import java.io.IOException;
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
			for (Entry<Integer, Map<Integer, Distance>> e : distances.entrySet()) {
				for (Entry<Integer, Distance> e2 : e.getValue().entrySet()) {
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
