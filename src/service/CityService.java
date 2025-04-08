package service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.dto.Attraction;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class CityService {

    private final HttpClient httpClient;

    public CityService() {
        this.httpClient = HttpClient.newHttpClient();
    }


    public Attraction getCityInfo(String cityName) throws Exception
    {
        String url = String.format(
                "https://sv.wikipedia.org/api/rest_v1/page/summary/%s",
                URLEncoder.encode(cityName, StandardCharsets.UTF_8)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Wikimedia REST API gav fel kod: " + response.statusCode());
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        String title = json.get("title").getAsString();
        String description = json.get("extract").getAsString();
        String imageUrl = json.has("thumbnail") ? json.getAsJsonObject("thumbnail").get("source").getAsString() : "";
        String wikiUrl = json.getAsJsonObject("content_urls").getAsJsonObject("desktop").get("page").getAsString();

        return new Attraction(title, description, imageUrl, wikiUrl);
    }

}
