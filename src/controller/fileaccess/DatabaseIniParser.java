package controller.fileaccess;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import model.Good;
import model.Location;
import model.Race;
import model.Weapon;
import model.ship.Restriction;
import model.ship.Ship;
import model.ship.ShipAbility;
import model.ship.ShipEquipment;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;

public class DatabaseIniParser extends DatabaseParser implements Runnable
{
	private final Ini mapFile;

	public DatabaseIniParser(Ini mapFileLocation)
	{
		this.mapFile = mapFileLocation;
	}

	public DatabaseIniParser(URL mapFileLocation) throws IOException
	{
		this.mapFile = new Ini(mapFileLocation);
	}

	public DatabaseIniParser(String mapFileLocation) throws IOException
	{
		this.mapFile = new Ini(new FileReader(mapFileLocation));
	}

	public DatabaseIniParser(File mapFileLocation) throws IOException
	{
		this.mapFile = new Ini(new FileReader(mapFileLocation));
	}

	@Override
	public void doParse()
	{
		new Thread(this).start();
	}

	@Override
	synchronized public void run()
	{
		parseRaces();
		parseGoods();
		parseWeapons();
		parseShipEquipment();
		parseShipAbilities();
		parseShips();
		parseLocations();
	}

	synchronized protected void parseRaces()
	{
		Section section;
		if ((section = mapFile.get("Races")) != null)
		{
			for (String key : section.keySet()) {
				Race.addRace(new Race(key, Integer.parseInt(section.get(key))));
			}
		}
	}

	synchronized protected void parseGoods()
	{
		Section section;
		Scanner sc;
		if ((section = mapFile.get("Goods")) != null)
		{
			for (String key : section.keySet()) {
				sc = new Scanner(section.get(key));
				sc.useDelimiter(",");

				Good.addGood(Integer.parseInt(key), sc.next().trim(), sc.nextInt());
			}
		}
	}

	synchronized protected void parseShipEquipment()
	{
		Section section;
		Scanner sc;
		if ((section = mapFile.get("ShipEquipment")) != null)
		{
			for (String key : section.keySet()) {
				sc = new Scanner(section.get(key));
				sc.useDelimiter(",");
				ShipEquipment se = new ShipEquipment(key);
				se.setCost(sc.nextInt());
				if (sc.hasNext())
					se.setDescription(sc.next().trim());

				ShipEquipment.addShipEquipment(se);
			}
		}
	}

	synchronized protected void parseShipAbilities()
	{
		Section section;
		if ((section = mapFile.get("ShipAbilities")) != null)
		{
			for (String key : section.keySet()) {
				ShipAbility sa = new ShipAbility(key);
				sa.setDescription(section.get(key));

				ShipAbility.addShipAbility(sa);
			}
		}
	}

	synchronized protected void parseShips()
	{
		Section section;
		Scanner sc;
		if ((section = mapFile.get("Ships")) != null)
		{
			for (String key : section.keySet()) {
				sc = new Scanner(section.get(key));
				sc.useDelimiter(",");
				Ship s = new Ship(key);
				s.setRace(Race.getId(sc.next().trim()));
				s.setCost(sc.nextInt());
				s.setTurnRate(sc.nextInt());
				s.setHardpoints(sc.nextInt());
				s.setPower(sc.nextInt());
//				s.setManu(sc.nextInt());
//				s.setShields(sc.nextInt());
//				s.setArmour(sc.nextInt());
//				s.setCargo(sc.nextInt());
//				s.setCombats(sc.nextInt());
//				s.setScouts(sc.nextInt());
//				s.setMines(sc.nextInt());
				while (sc.hasNext())
				{
					String next = sc.next().trim();
					if (next.toUpperCase().startsWith("SHIPEQUIPMENT"))
					{
						Scanner sc2 = new Scanner(next.substring(next.indexOf("=") + 1, next.length()).trim());
						sc2.useDelimiter(";");
						while (sc2.hasNext())
						{
							String next2 = sc2.next().trim();
							int value = 0;
							try
							{
								value = Integer.parseInt(next2.substring(next2.indexOf("=") + 1, next2.length()));
							}
							catch(NumberFormatException nfe)
							{
							}
							s.addEquipment(ShipEquipment.getShipEquipment(next2.substring(0,next2.indexOf("="))), value);
						}
						sc2.close();
					}
					if (next.toUpperCase().startsWith("ABILITIES"))
					{
						Scanner sc2 = new Scanner(next.substring(next.indexOf("=") + 1, next.length()).trim());
						sc2.useDelimiter(";");
						while (sc2.hasNext())
						{
							s.addAbility(ShipAbility.getShipAbility(sc2.next().trim()));
						}
						sc2.close();
					}
					if (next.toUpperCase().startsWith("RESTRICTIONS"))
					{
						s.setRestriction(new Restriction(next.substring(next.indexOf("=") + 1, next.length()).trim()));
					}
				}
				Ship.addShip(s);
			}
		}
	}

	synchronized protected void parseWeapons()
	{
		Section section;
		Scanner sc;
		if ((section = mapFile.get("Weapons")) != null)
		{
			for (String key : section.keySet()) {
				sc = new Scanner(section.get(key));
				sc.useDelimiter(",");
				Weapon w = new Weapon(key);
				w.setRace(Race.getId(sc.next()));
				w.setCost(sc.nextInt());
				w.setShieldDamage(sc.nextInt());
				w.setArmourDamage(sc.nextInt());
				w.setAccuracy(sc.nextInt());
				w.setPowerLevel(sc.nextInt());

				String emp = sc.next();
				w.setEmpInPercent(emp.endsWith("%"));
				w.setEmpDamage(Integer.parseInt(emp.replaceAll("%", "")));

				w.addRestriction(new Restriction(sc.next().trim()));

//				while (sc.hasNext())
//				{
//					String next = sc.next().trim();
//					if (next.toUpperCase().startsWith("RESTRICTIONS"))
//					{
//						Scanner sc2 = new Scanner(next.substring(next.indexOf("=") + 1, next.length()).trim());
//						sc2.useDelimiter(";");
//						while (sc2.hasNext())
//						{
//							w.addRestriction(new Restriction(sc2.next().trim()));
//						}
//					}
//				}
				Weapon.addWeapon(w);
			}
		}
	}

	synchronized protected void parseLocations()
	{
		Section section;
		Scanner sc;
		for (String sectionName : mapFile.keySet()) {
			if (sectionName.toUpperCase().startsWith("LOCATIONS"))
			{
				if ((section = mapFile.get(sectionName)) != null)
				{
					for (String key : section.keySet()) {
						sc = new Scanner(section.get(key));
						sc.useDelimiter(",");
						Location l = new Location(key);
						while (sc.hasNext())
						{
							String next = sc.next().trim();
							if(next.indexOf("=")==-1)
								l.setType(next.trim());
							else
								l.setType(next.substring(0, next.indexOf("=")).trim());
							if (next.toUpperCase().startsWith("SHIPS"))
							{
								Scanner sc2 = new Scanner(next.substring(next.indexOf("=") + 1, next.length()).trim());
								sc2.useDelimiter(";");
								while (sc2.hasNext())
								{
									l.addShip(Ship.getShip(sc2.next().trim()));
								}
								sc2.close();
							}
							if (next.toUpperCase().startsWith("SHIPEQUIPMENT"))
							{
								Scanner sc2 = new Scanner(next.substring(next.indexOf("=") + 1, next.length()).trim());
								sc2.useDelimiter(";");
								while (sc2.hasNext())
								{
									l.addShipEquipment(ShipEquipment.getShipEquipment(sc2.next().trim()));
								}
								sc2.close();
							}
							if (next.toUpperCase().startsWith("WEAPONS"))
							{
								Scanner sc2 = new Scanner(next.substring(next.indexOf("=") + 1, next.length()).trim());
								sc2.useDelimiter(";");
								while (sc2.hasNext())
								{
									l.addWeapon(Weapon.getWeapon(sc2.next().trim()));
								}
								sc2.close();
							}
						}
						Location.putLocation(l);
					}
				}
			}
		}
	}
}
