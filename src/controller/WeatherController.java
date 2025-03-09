/*
Användes innan vi kombinerade våra APIn (Flight & Väder), vi ansåg att det var smidigare att göra de kombinerat i en
SearchController istället.
 */
/*
package controller;

import io.javalin.Javalin;
import service.WeatherService;

public class WeatherController {

    private static WeatherService ws = new WeatherService();


    public static void registerRoutes(Javalin app) {
        app.get("/weather",context -> {
            String location = context.queryParam("location");
            if (location == null || location.isEmpty()){
                context.status(400).result("{\"error\":\"Saknar parameter location\"}");
            }else{
                String weatherData = ws.getWeatherData(location);
                context.result(weatherData);
            }
        });

    }
}

 */
