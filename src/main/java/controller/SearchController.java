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
 * The SearchController handles API endpoints for flight search, weather data,
 * and city attractions. It serves as a central controller for combining results
 * into a unified response.
 *
 * This controller integrates multiple services and returns a "mashup"
 * containing flight, weather, and attraction information.
 *
 * @author Emil
 * @author Mahyar
 * @author Amer
 */
public class SearchController {
    private static FlightService flightService;
    private static WeatherService weatherService;
    private static final Gson gson = new Gson();

    /**
     * Registers all routes for the Javalin server.
     * This includes:
     * - /api/v1/flights: for flights and weather
     * - /api/v1/cities: for general city description
     * - /api/v1/attractions: for tourist places and POIs
     *
     * @param app The Javalin application instance
     * @param rapidApiKey The API key used to access the flight API
     */
    public static void registerRoutes(Javalin app, String rapidApiKey){

        flightService = new FlightService(rapidApiKey);
        weatherService = new WeatherService();

        app.get("/api/v1/flights", context -> {
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
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Inga flyg hittades");
                    context.status(404).result(gson.toJson(errorResponse));
                    return;
                }

                Map<String,Object> combined = new HashMap<>();
                combined.put("flights", flights);

                try {
                    String weatherJson = weatherService.getWeatherData(destination);
                    combined.put("weathers", gson.fromJson(weatherJson, Object.class));
                } catch (Exception e) {
                    Map<String, String> weatherError = new HashMap<>();
                    weatherError.put("error", "Kunde inte hämta väderdata: " + e.getMessage());
                    combined.put("weathers", weatherError);
                }

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


        app.get("/api/v1/cities", context -> {
            String city = context.queryParam("city");

            if (city == null || city.isEmpty()) {
                context.status(400).result("{\"error\": \"Stad måste anges\"}");
                return;
            }

            try {
                CityService cityService = new CityService();
                CityInfo cityInfo = cityService.getCityInfo(city);
                if (cityInfo == null) {
                    context.status(404).result("{\"error\": \"Ingen information hittades för staden\"}");
                    return;
                }
                context.result(gson.toJson(cityInfo));
            } catch (Exception e) {
                context.status(500).result("{\"error\": \"Kunde inte hämta information om staden\"}");
            }
        });


        app.get("/api/v1/attractions", context -> {
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

                if (attractions == null || attractions.isEmpty()) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Inga attraktioner hittades för staden");
                    context.status(404).result(gson.toJson(errorResponse));
                    return;
                }

                context.status(200).result(gson.toJson(attractions));
            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Fel vid hämtning av attraktioner: " + e.getMessage());
                context.status(500).result(gson.toJson(errorResponse));
            }
        });
    }
}