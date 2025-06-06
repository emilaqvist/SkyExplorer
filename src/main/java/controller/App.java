package controller;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import misc.Configurator;

/**
 * Main klassen till applikationen, den har som ansvarsområde att starta våran JavaLin server till webben.
 * Klassen initierar alltså servern, samt kallar på klasserna som skapar API routesen.
 * @author Emil
 */
public class App {
    /**
     * Main metoden till appen.
     * Startar javalin servern på port 7000, gör CORS inställningar och sedan gör ett call till register routes.
     * Funkar det så skrivs prints ut i loggen.
     * @param args
     */
    public static void main(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.staticFiles.add(staticFileConfig -> {
                staticFileConfig.directory = "/frontend/public";
                staticFileConfig.location = Location.CLASSPATH;
            });

        }).start(7000);

        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, x-rapidapi-key, x-rapidapi-host");
        });
        app.get("/",ctx -> {
            System.out.println("Användare till / --> index.html");
            ctx.redirect("/index.html");
        });

        String apiKey = Configurator.getProperty("FLIGHT_API_KEY");
        SearchController.registerRoutes(app,apiKey);


        System.out.println("Server igång lyssnar på port 7000");
        System.out.println("good");
    }
}