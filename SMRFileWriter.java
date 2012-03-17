package controller.fileaccess;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import model.Connection;
import model.Good;
import model.Sector;

public class SMRFileWriter
{
//	public static String[] variableNames = {"Sector", "Right", "Left", "Up", "Down", "Warp", "Port Level", "Port Race", "Buys", "Sells","Planet", "Locations"};
	
	public static void writeMap(FileWriter mapFile, Sector[] sectors)
	{
		try {
			boolean b;
			mapFile.write("SMR1.5 Sectors File v 1.01\r\n");
			for(Sector sec : sectors)
			{
				if(sec != null)
				{
					mapFile.write("[Sector="+sec.getSectorID()+"]\r\n");
					if(sec.hasConnections())
					{
						Iterator<Connection> iter = sec.getConnections().iterator();
						String warps = "Warp=";
						b = false;
						while(iter.hasNext())
						{
							Connection c = iter.next();
							if(c.getType().equals(Connection.WARP))
							{
								warps += c.getTargetSector()+",";
								b=true;
							}
							else
								mapFile.write(c.getType()+"="+c.getTargetSector()+"\r\n");
						}
						if(b)
						{
							mapFile.write(warps.substring(0, warps.length()-1)+"\r\n");
						}
					}
					
					if(sec.hasPort())
					{
						mapFile.write("Port Level="+sec.getPort().getPortLevel()+"\r\nPort Race="+sec.getPort().getPortRace()+"\r\n");
						Good[] goods = sec.getPort().getGoods();
						b = false;
						String temp="Buys=";
						for(Good g : goods)
						{
							if(g.state==Good.BUYS)
							{
								temp += g.goodId+":"+g.getDistanceIndex()+",";
								b=true;
							}
						}
						if(b)
						{
							mapFile.write(temp.substring(0, temp.length()-1)+"\r\n");
						}
						b=false;
						temp="Sells=";
						for(Good g : goods)
						{
							if(g.state==Good.SELLS)
							{
								temp += g.goodId+":"+g.getDistanceIndex()+",";
								b=true;
							}
						}
						if(b)
						{
							mapFile.write(temp.substring(0, temp.length()-1) +"\r\n");
						}
					}
					if(sec.hasPlanet())
					{
						mapFile.write("Planet=1\r\n");
					}
					if(sec.hasLocation())
					{
						Iterator<String> iter = sec.getLocations().iterator();
						String temp = "Location=";
						while(iter.hasNext())
						{
								temp += iter.next()+",";
						}
						mapFile.write(temp.substring(0, temp.length()-1) +"\r\n");
					}
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
