package controller;

import com.google.gson.Gson;
import dto.CityInfo;
import io.javalin.Javalin;
import dto.FlightResult;
import service.*;
import dto.Attraction;
import dto.FlightSearchRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SearchController hanterar endpoints för API för att söka på flyg, hämta väderdata samt sevärdheter.
 * Denna kontroller klass tar ihop många tjänster och returnerar en sk. mashup flyg väder och sevärdheter.
 * @author Emil
 * @author Mahyar
 * @author Amer
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
            String fromFlightId = LocationMapper.getFlightId(from);
            String destinationFlightId = LocationMapper.getFlightId(destination);

            FlightSearchRequest request = new FlightSearchRequest(fromFlightId,destinationFlightId,departDate);

            try {
                System.out.println("Skickar flight request via FlightService...");
                System.out.println("DATE SOM SKICKAS TILL BACKEND: " + request.getDepartDate());
                List<FlightResult> flights = flightService.searchFlights(request);
                if (flights.isEmpty()) {
                    System.out.println("Inga flyg hittades från API:t!");
                }

                String weatherJson = weatherService.getWeatherData(destination);

                Map<String,Object> combined = new HashMap<>();
                combined.put("flights", flights);
                combined.put("weather", gson.fromJson(weatherJson, Object.class));

                Map<String, Object> searchInfo = new HashMap<>();
                searchInfo.put("departureCity", from);
                searchInfo.put("destinationCity", destination);
                searchInfo.put("departureDate", departDate);
                combined.put("searchInfo", searchInfo);

                context.result(gson.toJson(combined));

            } catch (FlightSearchException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", e.getMessage());
                context.status(e.getHttpStatus()).result(gson.toJson(errorResponse));
            }
        });


        app.get("/cityinfo", context -> {
            String city = context.queryParam("city");

            if (city == null || city.isEmpty()) {
                context.status(400).result("{\"error\": \"Stad måste anges\"}");
                return;
            }

            try {
                CityService cityService = new CityService();
                CityInfo cityInfo = cityService.getCityInfo(city);
                context.result(gson.toJson(cityInfo));
            } catch (Exception e) {
                context.status(500).result("{\"error\": \"Kunde inte hämta information om staden\"}");
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