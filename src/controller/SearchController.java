package controller;

import com.google.gson.Gson;
import io.javalin.Javalin;
import service.FlightSearchException;
import service.FlightService;
import service.LocationMapper;
import service.WeatherService;
import service.dto.FlightSearchRequest;

import java.util.HashMap;
import java.util.Map;

public class SearchController {
    private static FlightService flightService;
    private static WeatherService weatherService;
    private static final Gson gson = new Gson();

    public static void registerRoutes(Javalin app, String rapidApiKey){

        flightService = new FlightService(rapidApiKey);
        weatherService = new WeatherService();

        app.get("/search", context -> {
            String from = context.queryParam("from");
            String destination = context.queryParam("destination");
            String departDate = context.queryParam("departDate");
            String returnDate = context.queryParam("returnDate");

            if (from == null || destination == null || departDate == null ||
                    from.isEmpty() || destination.isEmpty() || departDate.isEmpty()) {
                context.status(400).result("{\"error\":\"Saknar obligatoriska parametrar: from, destination och departDate\"}");
                return;
            }
            String fromFlightId = LocationMapper.getFlightId(from);
            String destinationFlightId = LocationMapper.getFlightId(destination);

            FlightSearchRequest request = new FlightSearchRequest(fromFlightId,destinationFlightId,departDate);

            try{
                var flights = flightService.searchFlights(request);
                String weatherJson = weatherService.getWeatherData(destination);

                //Kombinera

                Map<String,Object> combined = new HashMap<>();
                combined.put("flights",flights);
                combined.put("weather",gson.fromJson(weatherJson,Object.class));

                //Returnera ihopslagna
                context.result(gson.toJson(combined));
            }catch (FlightSearchException e){
                System.err.println("Raw flight API response: " + flightService.getRawResponse());
                context.status(500).result("{\"error\":\"" + e.getMessage() + "\"}");
            }
        });

    }
}
