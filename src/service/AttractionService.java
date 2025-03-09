package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import misc.Configurator;
import service.dto.Attraction;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AttractionService {



    private final HttpClient httpClient;
    private final String apiHost;
    private final String baseUrl;
    private final String apiKey;

    public AttractionService() {
        this.httpClient = HttpClient.newHttpClient();
        this.apiKey = Configurator.getProperty("ATTRACTION_PLACES");
        this.apiHost = "opentripmap-places-v1.p.rapidapi.com";
        this.baseUrl = "https://opentripmap-places-v1.p.rapidapi.com/en/places";
    }

    public List<Attraction> getAttractionsForCity(String city) throws Exception {
        // hämta koordinates för att hämta attractions, asså places
        double[] coordinates = getCoordinates(city);

        // använder koordinates för att hämta platcer
        List<Attraction> attractions = getPlacesNearby(coordinates[0], coordinates[1], 100000, 40);

        // hämtar detaljer om platsen
        for (Attraction attraction : attractions) {
            getPlaceDetails(attraction);
        }

        return attractions;
    }

    private double[] getCoordinates(String city) throws Exception {
        // Koda stadsnamnet för URL
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

        // Bygg URL för att hämta koordinater, det här var för att kunna skriva koordinates på staden
        String url = baseUrl + "/geoname?name=" + encodedCity;

        // Skapa HTTP-request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", apiHost)
                .GET()
                .build();

        // Skicka request och ta emot svar
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Fel vid hämtning av koordinater. Statuskod: " + response.statusCode());
        }

        JsonObject rootObject = JsonParser.parseString(response.body()).getAsJsonObject();

        // Extrahera lat och lon, dvs latitud och longitud från JSON svaret, de används för att ge exakt position på jorden, kordinates helt enkelt
        double lat = rootObject.get("lat").getAsDouble();
        double lon = rootObject.get("lon").getAsDouble();


        //returnerar de som en array
        return new double[] { lat, lon };
    }

    private List<Attraction> getPlacesNearby(double lat, double lon, int radius, int limit) throws Exception {
        // Bygg URL för att hämta platser i närheten, fick använda US locale allt annat blev fel
        String url = String.format(Locale.US,
                "%s/radius?radius=%d&lon=%.6f&lat=%.6f&rate=2&format=json&limit=%d",
                baseUrl, radius, lon, lat, limit
        );

        // Skapa HTTP-request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", apiHost)
                .GET()
                .build();

        // Skicka request och ta emot svar
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Fel vid hämtning av platser. Statuskod: " + response.statusCode());
        }

        // Gson
        JsonArray placesArray = JsonParser.parseString(response.body()).getAsJsonArray();

        // Här skapas lista med platser
        List<Attraction> attractions = new ArrayList<>();

        for (JsonElement placeElement : placesArray) {
            JsonObject placeObject = placeElement.getAsJsonObject();

            Attraction attraction = new Attraction();
            attraction.setXid(placeObject.get("xid").getAsString());
            attraction.setName(placeObject.get("name").getAsString());
            attraction.setKinds(placeObject.get("kinds").getAsString());

            // Filtrera bort platser, annars var det mycket mer onödiga platser
            if (isInterestingPlace(attraction.getKinds())) {
                attractions.add(attraction);
            }
        }

        return attractions;
    }

    private boolean isInterestingPlace(String kinds) {
        // Filtrera platser,  man kan filtrera mer om man vill genom att ta bort kategorierna
        String[] interestingCategories = {
                "museums", "historic", "cultural", "architecture", "tourist_facilities",
                "monuments", "castles", "churches", "theatres", "gardens", "natural",
                "amusements", "interesting_places", "bridges"
        };

        for (String category : interestingCategories) {
            if (kinds.contains(category)) {
                return true;
            }
        }

        return false;
    }

    private void getPlaceDetails(Attraction attraction) throws Exception {
        // Bygg URL för att hämta detaljer om en plats
        String url = baseUrl + "/xid/" + attraction.getXid();

        // Skapa HTTP-request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", apiHost)
                .GET()
                .build();

        // Skicka request och ta emot svar
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        if (response.statusCode() != 200) {
            return;
        }

        // Tolka JSON svar med Gson
        JsonObject rootObject = JsonParser.parseString(response.body()).getAsJsonObject();

        // Uppdatera attraction objektet med detaljerad information, om JSON svaret har wikioedia extracts asså text så skrivs det
        if (rootObject.has("wikipedia_extracts") && rootObject.getAsJsonObject("wikipedia_extracts").has("text")) {
            attraction.setDescription(rootObject.getAsJsonObject("wikipedia_extracts").get("text").getAsString());
        }

        //den kollar på om den har en bild, om det finns en bild så hämtas den från source
        if (rootObject.has("preview") && rootObject.getAsJsonObject("preview").has("source")) {
            attraction.setImageUrl(rootObject.getAsJsonObject("preview").get("source").getAsString());
        }

        //koordinates som behövdes också spara för att visa den på kartan
        if (rootObject.has("point")) {
            JsonObject point = rootObject.getAsJsonObject("point");
            attraction.setLatitude(point.get("lat").getAsDouble());
            attraction.setLongitude(point.get("lon").getAsDouble());
        }
    }
}