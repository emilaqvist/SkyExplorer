package misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Konfigurationsklass för att läsa in vår propertiesklass där vi lagrar våra topphemliga API nycklar.
 * @author Emil
 * @author Mahyar
 */
public class Configurator {
    private static final Properties properties = new Properties();
//Då man startar klasssen så skall detta undertill laddas därmed statiskt.
    static{
        try(InputStream inputStream = Configurator.class.getClassLoader().getResourceAsStream("application.properties")){
            if (inputStream == null){

                System.err.println("application.properties filen kunde ej hittas!");

                System.err.println("appliction.properties filen kunde ej hittas");

            }else {
                properties.load(inputStream);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Hämtar property
     * @param key Namnet kopplat till den API nyckel vi vill åt.
     * @return API nyckeln hämtat från application.properties.
     */
    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
