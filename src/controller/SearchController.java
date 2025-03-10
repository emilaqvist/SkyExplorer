package controller;

import com.google.gson.Gson;
import io.javalin.Javalin;
import model.dto.FlightResult;
import service.*;
import model.dto.Attraction;
import model.dto.FlightSearchRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *SearchController hanterar endpoints för API för att söka på flyg, hämta väderdata samt sevärdheter.
 * Denna kontroller klass tar ihop många tjänster och returnerar en sk. mashup flyg väder och sevärdheter.
 * @author Emil
 * @author Mahyar
 **/
public class SearchController {
    private static FlightService flightService;
    private static WeatherService weatherService;
    private static final Gson gson = new Gson();

    /**
     * Registrerar API route för sökning av flyg, väderlek samt sevärdheter.
     * @param app Instansen av vår javalin applikation
     * @param rapidApiKey API nyckeln som hör till flyg APIt
     */
    public static void registerRoutes(Javalin app, String rapidApiKey){

        flightService = new FlightService(rapidApiKey);
        weatherService = new WeatherService();

        app.get("/search", context -> {
            String from = context.queryParam("from");
            String destination = context.queryParam("destination");
            String departDate = context.queryParam("departDate");
            String returnDate = context.queryParam("returnDate");

            Map<String, String> errors = new HashMap<>();
            if (from == null || from.isEmpty()) {
                errors.put("from", "Avreseplats måste anges");
            }

            if (destination == null || destination.isEmpty()) {
                errors.put("destination", "Destination måste anges");
            }

            if (departDate == null || departDate.isEmpty()) {
                errors.put("departDate", "Avresedatum måste anges");
            }

            if (!errors.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Saknar obligatoriska parametrar");
                errorResponse.put("details", errors);

                context.status(400).result(gson.toJson(errorResponse));
                return;
            }
            // Använd LocationMapper för att översätta platsnamn till flight, eftersom API vill ha forkortningar för flyggplatser
            String fromFlightId = LocationMapper.getFlightId(from);
            String destinationFlightId = LocationMapper.getFlightId(destination);

            FlightSearchRequest request = new FlightSearchRequest(fromFlightId,destinationFlightId,departDate);

            try{
                List<FlightResult> flights = flightService.searchFlights(request); //Bytte till tydligare definiering istället för tidigare med var
                String weatherJson = weatherService.getWeatherData(destination);

                //Kombinera

                Map<String,Object> combined = new HashMap<>();
                combined.put("flights",flights);
                combined.put("weather",gson.fromJson(weatherJson,Object.class));

                //Returnera ihopslagna
                context.result(gson.toJson(combined));
            } catch (FlightSearchException e) {
                System.err.println("Raw flight API response: " + flightService.getRawResponse());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", e.getMessage());
                context.status(e.getHttpStatus()).result(gson.toJson(errorResponse));
            }
        });

        app.get("/attractions", context -> {
            String city = context.queryParam("city");

            if (city == null || city.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Stad måste anges");
                context.status(400).result(gson.toJson(errorResponse));
                return;
            }

            try {
                AttractionService attractionService = new AttractionService();
                List<Attraction> attractions = attractionService.getAttractionsForCity(city);
                context.result(gson.toJson(attractions));
            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Fel vid hämtning av attraktioner: " + e.getMessage());
                context.status(500).result(gson.toJson(errorResponse));
            }
        });
    }
}