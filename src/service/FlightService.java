package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FlightService {
    public static void main(String[] args) {
        try {

            String apiUrl = "https://sky-scanner3.p.rapidapi.com/flights/search-multi-city";

            //Här är bara ett exempel, och den kommer söka flyggg baserat på detta info.
            String jsonBody = """
                {
                    "market": "US",
                    "locale": "en-US",
                    "currency": "USD",
                    "adults": 1,
                    "children": 0,
                    "infants": 0,
                    "cabinClass": "economy",
                    "stops": ["direct", "1stop", "2stops"],
                    "sort": "cheapest_first",
                    "flights": [
                        {"fromEntityId": "BCN", "toEntityId": "NUE", "departDate": "2025-01-03"},
                        {"fromEntityId": "PARI", "toEntityId": "MSYA", "departDate": "2025-01-20"}
                    ]
                }
            """;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("x-rapidapi-host", "sky-scanner3.p.rapidapi.com")
                    .header("x-rapidapi-key", "ca90664ac7msh43de15acd4241b8p1b2c8ejsn35c18291a913")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}