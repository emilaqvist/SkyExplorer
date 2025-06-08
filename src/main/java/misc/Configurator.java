package misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for loading application configuration properties from the
 * {@code application.properties} file located in the classpath.
 *
 * This class is primarily used to retrieve API keys or other sensitive configuration
 * values from a central place.
 *
 *
 * <p>The properties are loaded once statically when the class is first accessed.</p>
 *
 * Example usage:
 *
 * String apiKey = Configurator.getProperty("FLIGHT_API_KEY");
 *
 *
 * @author Emil
 * @author Mahyar
 */
public class Configurator {
    private static final Properties properties = new Properties();
//Då man startar klasssen så skall detta undertill laddas därmed statiskt.
    static{

        try(InputStream inputStream = Configurator.class.getClassLoader().getResourceAsStream("application.properties")){
            if (inputStream == null){
                System.out.println("Försöker läsa: " + Configurator.class.getClassLoader().getResource("application.properties"));
                System.err.println("application.properties filen kunde ej hittas!");
            }else {
                properties.load(inputStream);
                System.out.println("application.properties laddades!");

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the value of a property based on the provided key.
     *
     * @param key The name of the property to look up (e.g., "FLIGHT_API_KEY").
     * @return The corresponding value from the {@code application.properties} file,
     *         or {@code null} if the key is not found.
     */
    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
