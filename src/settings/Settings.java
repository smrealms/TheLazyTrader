package settings;

public class Settings
{
	public final static int NUMBER_OF_PROCESSORS = Runtime.getRuntime().availableProcessors();
	public final static double TURNS_PER_SECTOR = 1;
	public final static double TURNS_PER_WARP = 5;
	public final static int TURNS_WARP_SECTOR_EQUIVALENCE = (int)Math.round(TURNS_PER_WARP-TURNS_PER_SECTOR/TURNS_PER_SECTOR);
	public final static double TURNS_PER_TRADE = 1;// + 0.4/* Maintenance */+ 0.3/* Mercs */;
//	public final static int DEFAULT_DISTANCE_LIMIT = 100;
	public static final String ABOUT = "Route generation and general tools for SMR 1.6. (<a href=\"http://www.smrealms.de\">http://www.smrealms.de</a>)<br />" + "v%s<br /><br />" + "Created by Page";
//	public static final int DEFAULT_TRIM_TO_BEST_X_ROUTES = 200;
	public static final int DEFAULT_RACE_RELATIONS = 0;
	public static final boolean DEFAULT_USE_RELATIONS_FACTOR = true;
	public static final int MAX_MONEY_RELATIONS = 1000;
}
