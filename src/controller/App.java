package controller;

import io.javalin.Javalin;
import misc.Configurator;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {

        }).start(7000);

        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, x-rapidapi-key, x-rapidapi-host");
        });

        String apiKey = Configurator.getProperty("FLIGHT_API_KEY");
        //WeatherController.registerRoutes(app);
        SearchController.registerRoutes(app,apiKey);


        System.out.println("Server igång lyssnar på port 7000");
        System.out.println("good");
    }
}