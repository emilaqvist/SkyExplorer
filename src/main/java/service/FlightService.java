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

/**
 * A service class responsible for performing flight searches using an external flight API.
 * It handles request construction, response parsing, and mapping raw JSON data into
 * structured Java objects such as {@link FlightResult}.
 *
 * This class uses {@link HttpClient} for HTTP communication and Gson for JSON processing.
 *
 * @author Mahyar
 */
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
     * Searches for flights based on the provided search criteria.
     *
     * @param searchRequest an object containing the user's flight search parameters such as origin, destination, date, etc.
     * @return a list of {@link FlightResult} limited to a maximum number of results.
     * @throws FlightSearchException if the request is invalid, no data is found, or an error occurs during the API call.
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
     * Builds the HTTP GET request to query the flight API.
     *
     * @param searchRequest the search parameters.
     * @return a properly formatted {@link HttpRequest}.
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
     * Parses the API JSON response and extracts a list of flight results, limited to MAX_RESULTS.
     *
     * @param responseBody the raw JSON response body.
     * @return a list of {@link FlightResult} objects.
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
     * Converts a single flight JSON object into a {@link FlightResult} with all relevant details.
     *
     * @param flight the JSON object representing a flight.
     * @return a structured {@code FlightResult} object.
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