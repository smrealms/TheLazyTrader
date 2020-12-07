package settings;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class VersionUtil {
	private static final String POM_PROPERTIES_PATH = "app.properties";
	private static Properties properties;

	public static String getVersion() {
		if ( properties == null ) {
			try	{
				ClassLoader classLoader = ClassLoader.getSystemClassLoader();
				URL pomProps = classLoader.getResource( POM_PROPERTIES_PATH );
				if (pomProps == null) {
					return "dev";
				}
				properties = new Properties();
				properties.load( pomProps.openStream() );
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}
		return properties.getProperty( "version" );
	}
}
