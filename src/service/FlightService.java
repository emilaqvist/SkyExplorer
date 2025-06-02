package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.dto.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FlightService {
    private final String apiKey;
    private final HttpClient httpClient;
    private final Gson gson;

    private String rawResponse;
    private static final String API_URL = "https://sky-scanner3.p.rapidapi.com/flights/search-multi-city";
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
                throw new FlightSearchException("Sökförfrågan kan inte vara null", 400); // Bad Request
            }

            if (searchRequest.getFromLocation() == null || searchRequest.getToLocation() == null) {
                throw new FlightSearchException("Avrese- och destinationsplats måste anges", 400); // Bad Request
            }
            String jsonBody = createRequestBody(searchRequest);
            HttpRequest request = buildHttpRequest(jsonBody);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            this.rawResponse = response.body();

            if (response.statusCode() == 200) {
                try {
                    return parseAndLimitFlightResponse(response.body());
                }catch (NullPointerException e) {

                    throw new FlightSearchException("Inga flygdata hittades för den angivna rutten. Kontrollera stadsnamnen.", 404);
                }

            } else if (response.statusCode() == 400) {
                throw new FlightSearchException("Ogiltig förfrågan till flygAPI: " , 400); // Bad Request
            } else if (response.statusCode() == 404) {
                throw new FlightSearchException("Flygresursen kunde inte hittas: " , 404); // Not Found
            } else {
                throw new FlightSearchException("Fel vid sökning av flyg. Statuskod: " , 500); // Server Error
            }
        } catch (FlightSearchException e) {
            throw e;
        } catch (Exception e) {
            throw new FlightSearchException("Fel vid sökning av flyg: " + e.getMessage(), e, 500); // Server Error
        }
    }


    /**
     * Builds an HTTP request for searching flights.
     *
     * @param jsonBody the request body in JSON format.
     * @return the constructed HttpRequest.
     * @author Mahyar
     */
    private HttpRequest buildHttpRequest(String jsonBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("x-rapidapi-host", "sky-scanner3.p.rapidapi.com")
                .header("x-rapidapi-key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
    }


    /**
     * Creates the JSON request body for the flight search request.
     *
     * @param searchRequest the search request parameters.
     * @return the request body as a JSON string.
     * @author Mahyar
     */
    private String createRequestBody(FlightSearchRequest searchRequest) {
        Map<String, Object> requestMap = Map.of(
                "market", "US",
                "locale", "en-US",
                "currency", searchRequest.getCurrency(),
                "adults", searchRequest.getAdults(),
                "children", searchRequest.getChildren(),
                "infants", searchRequest.getInfants(),
                "cabinClass", searchRequest.getCabinClass(),
                "stops", searchRequest.getStops(),
                "sort", "cheapest_first",
                "flights", List.of(Map.of(
                        "fromEntityId", searchRequest.getFromLocation(),
                        "toEntityId", searchRequest.getToLocation(),
                        "departDate", searchRequest.getDepartDate()
                ))
        );

        return gson.toJson(requestMap); //Gson
    }


    /**
     * Parses the API response and limits the results to a maximum number.
     *
     * @param responseBody the JSON response from the API.
     * @return a list of parsed and limited flight results.
     * @author Mahyar
     */
    private List<FlightResult> parseAndLimitFlightResponse(String responseBody) {
        FlightResponse flightResponse = gson.fromJson(responseBody, FlightResponse.class); // Konvertera JSON till objekt
        return flightResponse.getData().getItineraries().stream()
                .limit(MAX_RESULTS)
                .map(this::convertToFlightResult)
                .collect(Collectors.toList());
    }

    //fanns för att testa raw respons
    public String getRawResponse() {
        return rawResponse;
    }


    /**
     * Converts an Itinerary object to a FlightResult object.
     *
     * @param itinerary the itinerary to convert.
     * @return a FlightResult object containing structured flight details.
     * @author Mahyar
     */
    private FlightResult convertToFlightResult(Itinerary itinerary) {
        Leg leg = itinerary.getLegs().get(0);

        return FlightResult.builder()

                .price(itinerary.getPrice().getRaw())
                .formattedPrice(itinerary.getPrice().getFormatted())
                .departureCity(leg.getOrigin().getCity())
                .arrivalAirport(leg.getDestination().getName())
                .departureAirport(leg.getOrigin().getName())
                .arrivalCity(leg.getDestination().getCity())
                .departureTime(leg.getDeparture())
                .arrivalTime(leg.getArrival())
                .duration(leg.getDurationInMinutes())
                .stops(leg.getStopCount())
                .airline(leg.getCarriers().getMarketing().get(0).getName())
                .build();
    }
}
