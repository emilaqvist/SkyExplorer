package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import service.dto.*;
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

    public List<FlightResult> searchFlights(FlightSearchRequest searchRequest) throws FlightSearchException {
        try {
            String jsonBody = createRequestBody(searchRequest);
            HttpRequest request = buildHttpRequest(jsonBody);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            this.rawResponse = response.body();

            if (response.statusCode() == 200) {
                return parseAndLimitFlightResponse(response.body());
            } else {
                throw new FlightSearchException("Failed to search flights. Status code: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new FlightSearchException("Error searching for flights", e);
        }
    }

    private HttpRequest buildHttpRequest(String jsonBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("x-rapidapi-host", "sky-scanner3.p.rapidapi.com")
                .header("x-rapidapi-key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
    }

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

    private FlightResult convertToFlightResult(Itinerary itinerary) {
        Leg leg = itinerary.getLegs().get(0);

        return FlightResult.builder()
                .id(itinerary.getId())
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
