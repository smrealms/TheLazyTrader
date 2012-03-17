import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Map.Entry;

import view.InterfaceContainer;

import model.Distance;
import model.Good;
import model.RouteList;
import model.Sector;
import model.Universe;
import Settings.Settings;
import controller.DiCalculator;
import controller.GoodFinder;
import controller.Pathfinding;
import controller.RouteGenerator;
import controller.fileaccess.UniverseParser;
import controller.fileaccess.SMRIniParser;



public class ExecuteGenerateRoutes
{
	public static void main(String[] args)
	{
		InterfaceContainer ic = new InterfaceContainer();
		try {
//			int goodId = Good.ANTIBIOTICS;
			long s0=System.currentTimeMillis();
//			UniverseParser smrfp = new SMRIniParser("H:\\Development\\Java\\RouteGenerator\\src\\sectors.smr");
//			smrfp.doParse();
//			Sector[] sectors = SMRFileParser.parseMap(new FileReader("H:\\Development\\Java\\RouteGenerator\\src\\sectors.smr"));
//			Universe uni = smrfp.getUniverse();
			long s01=System.currentTimeMillis();
			System.out.println("Parse: "+((s01-s0)/1000.0));
			
//			SMRFileWriter.writeMap(new FileWriter("H:\\Development\\Java\\RouteGenerator\\src\\sectorsDiSpecial.smr"), sectors);
//			boolean[] races = new boolean[Settings.HIGH_RACE_NUMBER];
//			races[Race.SALVENE]=true;
			long s,s2;
			//GoodFinder.findGood(sectors, goodId);
			s=System.currentTimeMillis();
//			Sector[] sectors = SMRFileParser.parseMap(new FileReader("H:\\Development\\Java\\RouteGenerator\\src\\sectorsDi.smr"));
			
			

			boolean[] races = new boolean[Settings.NUMBER_RACES];
			for(int i=0;i<Settings.NUMBER_RACES;i++)
			{
				races[i]=true;
			}
//			races[Race.ALSKANT]=true;
//			races[Race.NEUTRAL]=true;
//			races[Race.SALVENE]=true;
			boolean[] goods = new boolean[Settings.TOTAL_GOODS];
			for(int i=0;i<Settings.TOTAL_GOODS;i++)
			{
				goods[i]=true;
			}
//			goods[Good.NARCOTICS]=false;
//			Map<Integer, Map<Integer, Distance>> distances = Pathfinding.calculatePortToPortDistances(uni.getSectors(), 0, 255, 30, races);
			s2=System.currentTimeMillis();
			System.out.println("Calculate Distances: "+((s2-s)/1000.0));
////			DistanceFileWriter.writeDistances(new FileWriter("H:\\Development\\Java\\RouteGenerator\\src\\sectorsDi.distance"), distances);
//			NavigableMap<Double, RouteList> routes = RouteGenerator.generateRoutes(uni.getSectors(),goods,distances);
			
//			NavigableMap<Double, RouteList> routes = RouteGenerator.generateMultiPortRoutes(2, sectors, goods, distances);
			long s3=System.currentTimeMillis();
			System.out.println("Calculate Routes: "+((s3-s2)/1000.0));
			System.out.println("Overall Route Calculations: "+((s3-s)/1000.0));
//			saveRoutes(routes);
//			displayRoutes(routes);
			
//			Map<Integer, RouteList> routes = RouteGenerator.generateOneWayRoutes(sectors, 0, 100);
//			displayRoutes(routes);
//			GoodFinder.findGood(sectors, goodId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void displayRoutes(Map<Integer, RouteList> routes)
	{
		Iterator<Entry<Integer, RouteList>> iter = routes.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<Integer, RouteList> e = iter.next();
			System.out.println("Multiplier: "+e.getValue().get().getOverallExpMultiplier());
		}
	}

	private static void displayRoutes(NavigableMap<Double, RouteList> routes)
	{
		Iterator<Entry<Double, RouteList>> iter = routes.descendingMap().entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<Double, RouteList> e = iter.next();
			double multiplier = e.getKey();
			RouteList rl = e.getValue();
			while(!rl.isEmpty())
			{
				System.out.println("Multiplier: "+multiplier+"\r\nRoute: "+rl.removeLast().getRouteString()+"\r\n\r\n");
			}
		}
	}

	private static void saveRoutes(NavigableMap<Double, RouteList> routes)
	{
		FileWriter mapFile;
		try {
			 mapFile = new FileWriter("H:\\Development\\Java\\RouteGenerator\\src\\allRoutes.txt");
			
			Iterator<Entry<Double, RouteList>> iter =  routes.descendingMap().entrySet().iterator();
			while(iter.hasNext())
			{
				Entry<Double, RouteList> e = iter.next();
				double multiplier = e.getKey();
				RouteList rl = e.getValue();
				while(!rl.isEmpty())
				{
					mapFile.write("Multiplier: "+multiplier+"\r\nRoute: "+rl.removeLast().getRouteString()+"\r\n\r\n");
				}
				mapFile.flush();
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
}
