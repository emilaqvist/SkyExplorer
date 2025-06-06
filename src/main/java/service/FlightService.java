package service;

import com.google.gson.*;
import dto.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FlightService {
    private final String apiKey;
    private final HttpClient httpClient;
    private final Gson gson;

    private String rawResponse;
    private static final String API_URL = "https://google-flights2.p.rapidapi.com/api/v1/searchFlights";

    private static final int MAX_RESULTS = 10;


    public FlightService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new GsonBuilder().setPrettyPrinting().create(); // Gson
    }


    /**
     * Searches for flights based on the given search request parameters.
     *
     * @param searchRequest the flight search request containing user-specified search criteria.
     * @return a list of flight results limited to MAX_RESULTS.
     * @throws FlightSearchException if an error occurs while searching for flights.
     * @author Mahyar
     */
    public List<FlightResult> searchFlights(FlightSearchRequest searchRequest) throws FlightSearchException {
        try {
            if (searchRequest == null) {
                throw new FlightSearchException("Sökförfrågan kan inte vara null", 400);
            }

            if (searchRequest.getFromLocation() == null || searchRequest.getToLocation() == null) {
                throw new FlightSearchException("Avrese- och destinationsplats måste anges", 400);
            }

            HttpRequest request = buildHttpRequest(searchRequest);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            this.rawResponse = response.body();

            if (response.statusCode() == 200) {
                try {
                    return parseAndLimitFlightResponse(response.body());

                } catch (NullPointerException e) {
                    throw new FlightSearchException("Inga flygdata hittades för den angivna rutten. Kontrollera stadsnamnen.", 404);
                }
            } else if (response.statusCode() == 400) {
                throw new FlightSearchException("Ogiltig förfrågan till flygAPI: " + response.body(), 400);
            } else if (response.statusCode() == 404) {
                throw new FlightSearchException("Flygresursen kunde inte hittas: " + response.body(), 404);
            } else {
                throw new FlightSearchException("Fel vid sökning av flyg. Statuskod: " + response.statusCode() + " Body: " + response.body(), 500);
            }
        } catch (FlightSearchException e) {
            throw e;
        } catch (Exception e) {
            throw new FlightSearchException("Fel vid sökning av flyg: " + e.getMessage(), e, 500);
        }
    }


    /**
     * Builds an HTTP request for searching flights.
     *
     * @param jsonBody the request body in JSON format.
     * @return the constructed HttpRequest.
     * @author Mahyar
     */
    private HttpRequest buildHttpRequest(FlightSearchRequest searchRequest) {
        String queryParams = String.format(
                "?departure_id=%s&arrival_id=%s&outbound_date=%s&travel_class=%s&adults=%d&show_hidden=1&currency=%s&language_code=en-US&country_code=US",
                searchRequest.getFromLocation(),
                searchRequest.getToLocation(),
                searchRequest.getDepartDate(),
                searchRequest.getCabinClass().toUpperCase(),
                searchRequest.getAdults(),
                searchRequest.getCurrency()
        );

        String fullUrl = API_URL + queryParams;

        System.out.println("Sending request to: " + fullUrl);

        return HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .header("x-rapidapi-host", "google-flights2.p.rapidapi.com")
                .header("x-rapidapi-key", apiKey)
                .GET()
                .build();
    }


    /**
     * Creates the JSON request body for the flight search request.
     *
     * @param searchRequest the search request parameters.
     * @return the request body as a JSON string.
     * @author Mahyar
     */
    /*private String createRequestBody(FlightSearchRequest searchRequest) {
        Map<String, Object> requestMap = Map.of(
                "market", "US",
                "locale", "en-US",
                "currency", searchRequest.getCurrency(),
                "adults", searchRequest.getAdults(),
                "children", searchRequest.getChildren(),
                "infants", searchRequest.getInfants(),
                "cabinClass", searchRequest.getCabinClass(),
                "stops", searchRequest.getStops(),
                "sort", "", // tom sträng
                "flights", List.of(Map.of(
                        "originSkyId", searchRequest.getFromLocation(),
                        "destinationSkyId", searchRequest.getToLocation(),
                        "departDate", searchRequest.getDepartDate()
                ))
        )

        String json = gson.toJson(requestMap);
        System.out.println("SKICKAD REQUESTBODY:\n" + json);

        return json;
    }*/



    /**
     * Parses the API response and limits the results to a maximum number.
     *
     * @param responseBody the JSON response from the API.
     * @return a list of parsed and limited flight results.
     * @author Mahyar
     */
    private List<FlightResult> parseAndLimitFlightResponse(String responseBody) {
        JsonObject root = gson.fromJson(responseBody, JsonObject.class);

        JsonArray topFlights = root
                .getAsJsonObject("data")
                .getAsJsonObject("itineraries")
                .getAsJsonArray("topFlights");

        return topFlights
                .asList()
                .stream()
                .limit(MAX_RESULTS)
                .map(flightJson -> convertToFlightResult(flightJson.getAsJsonObject()))
                .collect(Collectors.toList());
    }



    /**
     * Converts an Itinerary object to a FlightResult object.
     *
     * @param itinerary the itinerary to convert.
     * @return a FlightResult object containing structured flight details.
     * @Mahyar
     */
    private FlightResult convertToFlightResult(JsonObject flight) {
        int duration = flight.getAsJsonObject("duration").get("raw").getAsInt();
        int price = flight.get("price").getAsInt();
        String formattedPrice = "$" + price;

        JsonArray segmentsJson = flight.getAsJsonArray("flights");
        List<FlightSegment> segments = new ArrayList<>();

        for (JsonElement el : segmentsJson) {
            JsonObject seg = el.getAsJsonObject();

            FlightSegment segment = FlightSegment.builder()
                    .from(seg.getAsJsonObject("departure_airport").get("airport_name").getAsString())
                    .to(seg.getAsJsonObject("arrival_airport").get("airport_name").getAsString())
                    .departureTime(seg.getAsJsonObject("departure_airport").get("time").getAsString())
                    .arrivalTime(seg.getAsJsonObject("arrival_airport").get("time").getAsString())
                    .airline(seg.get("airline").getAsString())
                    .flightNumber(seg.get("flight_number").getAsString())
                    .build();

            segments.add(segment);
        }

        JsonObject firstSegment = segmentsJson.get(0).getAsJsonObject();

        return FlightResult.builder()
                .price(price)
                .formattedPrice(formattedPrice)
                .departure(flight.get("departure_time").getAsString())
                .arrival(flight.get("arrival_time").getAsString())
                .duration(duration)
                .stops(flight.getAsJsonArray("flights").size() - 1)
                .airline(firstSegment.get("airline").getAsString())
                .flightNumber(firstSegment.get("flight_number").getAsString())
                .departureAirport(firstSegment.getAsJsonObject("departure_airport").get("airport_name").getAsString())
                .arrivalAirport(firstSegment.getAsJsonObject("arrival_airport").get("airport_name").getAsString())
                .departureCity(firstSegment.getAsJsonObject("departure_airport").get("airport_code").getAsString())
                .arrivalCity(firstSegment.getAsJsonObject("arrival_airport").get("airport_code").getAsString())
                .segments(segments)
                .build();
    }
}