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
 * CityService ansvarar för att hämta information om en stad från svenska Wikipedia
 * genom att använda Wikimedia REST API.
 *
 * @author Amer Sabaredzovic
 */
public class CityService {

    // HTTP-klient som används för att skicka HTTP-förfrågningar
    private final HttpClient httpClient;

    // Konstruktor som initierar HTTP-klienten
    public CityService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Hämtar information om en stad från svenska Wikipedia.
     *
     * @param cityName Namnet på staden att hämta information om
     * @return Ett CityInfo-objekt som innehåller stadens titel, beskrivning, bild och Wikipedia-länk
     * @throws Exception Om något går fel vid API-anropet eller JSON-hanteringen
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
        if (response.statusCode() != 200) {
            throw new Exception("Wikimedia REST API gav fel kod: " + response.statusCode());
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