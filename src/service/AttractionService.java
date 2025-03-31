package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import misc.Configurator;
import model.dto.Attraction;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Service class responsible for retrieving tourist attractions and points of interest
 * for a specified city using the OpenTripMap API.
 *
 * @author Mahyar
 * @author Emil
 */

public class AttractionService {

    private final HttpClient httpClient;
    private final String wikipediaApiUrl = "https://en.wikipedia.org/w/api.php";
    /**
     * Constructs a new AttractionService instance.
     * Initializes the HTTP client and retrieves the API key from the application configuration.
     */
    public AttractionService() {
        this.httpClient = HttpClient.newHttpClient();
    }


    /**
     * Retrieves a list of attractions for a specified city.
     *
     * @param city The name of the city to find attractions for
     * @return A list of Attraction objects containing details about points of interest
     * @throws Exception If an error occurs during any step of the process, including API communication errors
     */
    public List<Attraction> getAttractionsForCity(String city) throws Exception {
        // hämta koordinates för att hämta attractions, asså places
        List<Attraction> attractions = new ArrayList<>();

        String searchUrl = String.format(
                "%s?action=query&list=search&srsearch=%s&format=json",
                wikipediaApiUrl,
                URLEncoder.encode(city + " tourist attractions", StandardCharsets.UTF_8)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(searchUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Kunde inte hämta från Wikipedia" + response.statusCode());
        }

        JsonObject rootObject = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray searchResults = rootObject.getAsJsonObject("query").getAsJsonArray("search");

        for (var element : searchResults) {
            JsonObject searchResult = element.getAsJsonObject();
            String title = searchResult.get("title").getAsString();

            Attraction attraction = new Attraction();
            attraction.setName(title);

            getPlaceDetails(attraction);

            attractions.add(attraction);
        }
        return attractions;
    }

    /**
     * Retrieves detailed information for a specific attraction.
     *
     * @param attraction The Attraction object to update with detailed information
     * @throws Exception If an error occurs during the API request or response parsing
     */
    private void getPlaceDetails(Attraction attraction) throws Exception {

        String detailsUrl = String.format(
                "%s?action=query&prop=extracts|pageimages&exintro=true&explaintext=true&piprop=original&titles=%s&format=json",
                wikipediaApiUrl,
                URLEncoder.encode(attraction.getName(), StandardCharsets.UTF_8)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(detailsUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        if (response.statusCode() != 200) {
            return;
        }

        JsonObject rootObject = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject pages = rootObject.getAsJsonObject("query").getAsJsonObject("pages");

        for (var entry : pages.entrySet()){
            JsonObject page = entry.getValue().getAsJsonObject();

            if (page.has("extract")) {
                attraction.setDescription(page.get("extract").getAsString());
            }

            if (page.has("original")) {
                attraction.setImageUrl(page.getAsJsonObject("original").get("source").getAsString());
            }
        }

    }
}