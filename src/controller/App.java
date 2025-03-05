package controller;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {

        }).start(7000);

        app.before(context -> context.contentType("application/json"));

        WeatherController.registerRoutes(app);
        FlightController.registerRoutes(app);
        PlaceController.registerRoutes(app);

        System.out.println("Server igång lyssnar på port 7000");
        System.out.println("good");
    }
}
