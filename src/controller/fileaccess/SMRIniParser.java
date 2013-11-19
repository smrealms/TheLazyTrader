package controller.fileaccess;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

import model.Galaxy;
import model.Location;
import model.Port;
import model.Sector;
import model.Universe;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import controller.pathfinding.DiCalculator;
import java.util.ArrayList;

public class SMRIniParser extends UniverseParser implements Runnable
{
	private Ini mapFile;
	private Universe storedUni;

	public SMRIniParser(URL mapFileLocation) throws IOException
	{
		this.mapFile = new Ini(mapFileLocation);
		doParse();
	}

	public SMRIniParser(String mapFileLocation) throws IOException
	{
		this.mapFile = new Ini(new FileReader(mapFileLocation));
		doParse();
	}

	public SMRIniParser(File mapFileLocation) throws IOException
	{
		this.mapFile = new Ini(new FileReader(mapFileLocation));
		doParse();
	}

	synchronized public Universe getUniverse()
	{
		if (this.storedUni == null)
			run();
		return this.storedUni;
	}

	synchronized public Map<Integer, Galaxy> getGalaxies()
	{
		if (this.storedUni == null)
			run();
		return this.storedUni.getGalaxies();
	}

	public void doParse()
	{
		new Thread(this).start();
	}

	synchronized public void run()
	{
		new DatabaseIniParser(this.mapFile).run();
		this.storedUni = new Universe();
		parseGalaxies();
		parseSectors();
	}

	synchronized protected void parseGalaxies()
	{
		Section section;
		Scanner sc;
		int i;
		if ((section = mapFile.get("Galaxies")) != null)
		{
			i = 1;
			int sectorId = 1;
			String value;
			while ((value = section.get(Integer.toString(i))) != null)
			{
				i++;
				sc = new Scanner(value);
				sc.useDelimiter(",");

				Galaxy g = new Galaxy(i);
				g.setWidth(sc.nextInt());
				g.setHeight(sc.nextInt());
				g.setType(sc.next());
				g.setName(sc.next());
				g.setStartSectorId(sectorId);
				sectorId += g.getWidth() * g.getHeight();
				g.setEndSectorId(sectorId - 1);
				this.storedUni.addGalaxy(g);
			}
		}
	}

	synchronized protected void parseSectors()
	{
		Section section;
		Scanner sc;
		int i;

		ArrayList<Sector> sl = new ArrayList<Sector>();
		i = 1;
		int galaxyId = 1, endSectorId = 0;
		Galaxy g = this.storedUni.getGalaxy(galaxyId);
		if (g != null)
			endSectorId = g.getEndSectorId();
		while ((section = mapFile.get("Sector=" + i)) != null)
		{
			Sector sec = new Sector(i);
			if (g != null)
			{
				if (i <= endSectorId)
					sec.setGalaxyID(galaxyId);
				else
				{
					galaxyId++;
					g = this.storedUni.getGalaxy(galaxyId);
					endSectorId = g.getEndSectorId();
					sec.setGalaxyID(galaxyId);
				}
			}
			i++;

			if (section.containsKey("Right"))
				sec.addConnection("Right", Integer.parseInt(section.get("Right")));
			if (section.containsKey("Left"))
				sec.addConnection("Left", Integer.parseInt(section.get("Left")));
			if (section.containsKey("Up"))
				sec.addConnection("Up", Integer.parseInt(section.get("Up")));
			if (section.containsKey("Down"))
				sec.addConnection("Down", Integer.parseInt(section.get("Down")));
			if (section.containsKey("Warp"))
				sec.addWarp("Warp", Integer.parseInt(section.get("Warp")));
			if (section.containsKey("Port Level"))
			{
				Port p = new Port(Integer.parseInt(section.get("Port Level")));
				p.setPortRace(Integer.parseInt(section.get("Port Race")));

				if(section.containsKey("Buys"))
				{
					sc = new Scanner(section.get("Buys"));
					sc.useDelimiter(",");
					while (sc.hasNext())
					{
						p.addPortBuy(sc.nextInt());	
					}
				}

				if(section.containsKey("Sells"))
				{
					sc = new Scanner(section.get("Sells"));
					sc.useDelimiter(",");
					while (sc.hasNext())
					{
						p.addPortSell(sc.nextInt());
					}
				}
				sec.setPort(p);
			}
			if (section.containsKey("Planet"))
				sec.setPlanet(true);
			if (section.containsKey("Locations"))
			{
				sc = new Scanner(section.get("Locations"));
				sc.useDelimiter(",");
				while (sc.hasNext())
				{
					sec.addLocation(Location.getLocation(sc.next().trim()));
				}
			}

			sl.add(sec);
		}
		Sector[] sectors = new Sector[sl.size() + 1]; // 0 will not be used as
		// an index so we need
		// extra
		for (Sector sec : sl) {
			sectors[sec.getSectorID()] = sec;
		}
		DiCalculator.generateAllDistanceIndexes(sectors);
		this.storedUni.setSectors(sectors);
	}
}
