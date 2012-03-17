package controller.fileaccess;
//package controller.fileaccess;
//
//
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.util.Iterator;
//import java.util.Scanner;
//
//import model.Sector;
//import model.SectorList;
//
//
//public class SMRFileParser extends UniverseParser
//{
//	public static final String[] variableNames = {"[Sector", "Right", "Left", "Up", "Down", "Warp", "Port Level", "Port Race", "Buys", "Sells","Planet", "Locations"};
//	public static final String[] patterns = {"\\[Sector", "Right", "Left", "Up", "Down", "Warp", "Port Level", "Port Race", "Buys", "Sells","Planet", "Locations"};
//	private FileReader mapFile;
//	
//	public SMRFileParser(String mapFileLocation) throws FileNotFoundException
//	{
//		this.mapFile=new FileReader(mapFileLocation);
//	}
//	
//	public Sector[] parseMap()
//	{
//		Scanner sc = new Scanner(mapFile);
//		String line;
//		SectorList sl = new SectorList();
//		while(sc.hasNext())
//		{
//			line = sc.nextLine().trim();
//			for(int i=0;i<variableNames.length;i++)
//			{
//				if(line.startsWith(variableNames[i]))
//					addVariable(sl,i,line);
//			}
//		}
//		
//		Sector[] sectors = new Sector[sl.size()+1]; // 0 will not be used as an index so we need extra
//		Iterator iter = sl.iterator();
//		while(iter.hasNext())
//		{
//			Sector sec = (Sector)iter.next();
//			sectors[sec.getSectorID()] = sec;
//		}
//		return sectors;
//	}
//	public static SectorList addVariable(SectorList sl, int i, String line)
//	{
//		Sector sec;
//		Scanner sc;
//		line = line.replaceAll(patterns[i], "");
//		line = line.replaceAll("=", "");
//		line = line.replaceAll("\\[", "");
//		line = line.replaceAll("\\]", "");
//		switch(i)
//		{
//			case 0:
//				sl.add(new Sector(Integer.parseInt(line)));
//			break;
//			
//			case 1:
//				sec = sl.removeLast();
//				sec.addConnection( variableNames[i],Integer.parseInt(line) );
//				sl.add(sec);
//			break;
//				
//			case 2:
//				sec = sl.removeLast();
//				sec.addConnection( variableNames[i],Integer.parseInt(line) );
//				sl.add(sec);
//			break;
//				
//			case 3:
//				sec = sl.removeLast();
//				sec.addConnection( variableNames[i],Integer.parseInt(line) );
//				sl.add(sec);
//			break;
//				
//			case 4:
//				sec = sl.removeLast();
//				sec.addConnection( variableNames[i],Integer.parseInt(line) );
//				sl.add(sec);
//			break;
//				
//			case 5:
//				sc = new Scanner(line);
//				sc.useDelimiter(",");
//				while(sc.hasNext())
//				{
//					sec = sl.removeLast();
//					sec.addWarp(variableNames[i],Integer.parseInt(sc.next().trim()) );
//					sl.add(sec);
//				}
//			break;
//				
//			case 6:
//				sec = sl.removeLast();
//				sec.setPortLevel( Integer.parseInt(line) );
//				sl.add(sec);
//			break;
//				
//			case 7:
//				sec = sl.removeLast();
//				sec.setPortRace( Integer.parseInt(line) );
//				sl.add(sec);
//			break;
//				
//			case 8:
//				sc = new Scanner(line);
//				sc.useDelimiter(",");
//				while(sc.hasNext())
//				{
//					Scanner sc2 = new Scanner(sc.next().trim());
//					sc2.useDelimiter(":");
//					sec = sl.removeLast();
//					sec.addPortBuy( Integer.parseInt(sc2.next()),Integer.parseInt( (sc2.hasNext() ? sc2.next() : "0") ) );
//					sl.add(sec);
//
//				}
//			break;
//				
//			case 9:
//				sc = new Scanner(line);
//				sc.useDelimiter(",");
//				while(sc.hasNext())
//				{
//					Scanner sc2 = new Scanner(sc.next().trim());
//					sc2.useDelimiter(":");
//					sec = sl.removeLast();
//					sec.addPortSell( Integer.parseInt(sc2.next()),Integer.parseInt((sc2.hasNext()?sc2.next():"0")) );
//					sl.add(sec);
//				}
//			break;
//				
//			case 10:
//				sc = new Scanner(line);
//				sc.useDelimiter(",");
//				while(sc.hasNext())
//				{
//					sec = sl.removeLast();
//					sec.setPlanet( Integer.parseInt(sc.next().trim()) );
//					sl.add(sec);
//				}
//			break;
//				
//			case 11:
//				sc = new Scanner(line);
//				sc.useDelimiter(",");
//				while(sc.hasNext())
//				{
//					sec = sl.removeLast();
//					sec.addLocation( sc.next().trim() );
//					sl.add(sec);
//				}
//			break;
//		}
//		return sl;
//	}
//}
