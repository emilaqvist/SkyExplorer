package service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.CityInfo;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Service class responsible for retrieving city information from the Swedish Wikipedia
 * using the Wikimedia REST API.
 * It returns key data such as title, description, image, and page URL.
 *
 * @author  Amer Sabaredzovic
 * @author Algot Olofsson
 */
public class CityService {

    // HTTP-klient som används för att skicka HTTP-förfrågningar
    private final HttpClient httpClient;

    /**
     * Constructor that initializes the HTTP client.
     */
    public CityService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Fetches basic information about a city from the Swedish Wikipedia.
     *
     * @param cityName The name of the city to retrieve information for.
     * @return A {@link CityInfo} object containing the title, short description,
     *         image URL (if available), and a link to the full Wikipedia page.
     * @throws Exception If the API request fails or the response format is unexpected.
     */
    public CityInfo getCityInfo(String cityName) throws Exception {
        // Skapa URL med korrekt kodning för stadens namn
        String url = String.format(
                "https://sv.wikipedia.org/api/rest_v1/page/summary/%s",
                URLEncoder.encode(cityName, StandardCharsets.UTF_8)
        );

        // Skapa HTTP GET-förfrågan
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        // Skicka förfrågan och hämta svaret som en sträng
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Kontrollera om API-svaret har statuskod 200 (OK)
        if (response.statusCode() == 404) {
            return null;
        } else if (response.statusCode() != 200) {
            throw new Exception("Wikimedia REST API gav felkod: " + response.statusCode());
        }

        // Parsar JSON-svaret till ett JsonObject
        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        // Extrahera relevant information från JSON-objektet
        String title = json.get("title").getAsString(); // Stadens namn/titel
        String description = json.get("extract").getAsString(); // Kort beskrivning
        String imageUrl = json.has("thumbnail") ? json.getAsJsonObject("thumbnail").get("source").getAsString() : ""; // Bild (om finns)
        String wikiUrl = json.getAsJsonObject("content_urls").getAsJsonObject("desktop").get("page").getAsString(); // Länk till Wikipedia-sidan

        // Returnera ett nytt CityInfo-objekt med all information
        return new CityInfo(title, description, imageUrl, wikiUrl);
    }
}