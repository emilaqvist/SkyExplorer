package misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configurator {
    private static final Properties properties = new Properties();

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

    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
