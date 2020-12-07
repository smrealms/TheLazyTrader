package settings;

import java.io.IOException;
import java.util.Properties;

public class VersionUtil {
	private static final String POM_PROPERTIES_PATH = "app.properties";
	private static Properties properties;

	public static String getVersion() {
		if ( properties == null ) {
			properties = new Properties();
			try	{
				ClassLoader classLoader = ClassLoader.getSystemClassLoader();
				properties.load( classLoader.getResource( POM_PROPERTIES_PATH ).openStream() );
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}
		return properties.getProperty( "version" );
	}
}
