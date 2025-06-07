package controller;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import misc.Configurator;

/**
 * Entry point of the application.
 * This class is responsible for initializing and starting the Javalin web server,
 * configuring static file handling, enabling CORS settings, and registering API routes.
 *
 * @author Emil
 */
public class App {
    /**
     * Main method that starts the Javalin server on port 7000.
     * It configures static file serving, applies global CORS headers, and sets up the main route.
     * Also registers API endpoints by calling the SearchController.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.staticFiles.add(staticFileConfig -> {
                staticFileConfig.directory = "/public";
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