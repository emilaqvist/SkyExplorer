package service;


import misc.Configurator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherService {

    private static final String API_NYCKEL = Configurator.getProperty("TOMOROW_API_KEY");
    private static final String URL_BASE = "https://api.tomorrow.io/v4/weather/forecast";
    public String getWeatherData(String location) {

        try{
            if (API_NYCKEL == null || API_NYCKEL.isEmpty()){
                return "{\"error\": \"API-nyckel saknas. Kontrollera att den är satt i application.properties.\"}";
            }

            String encodedLocation = URLEncoder.encode(location.trim(), StandardCharsets.UTF_8.toString());
            String encodeFields = URLEncoder.encode("temperature,nederbordTyp",StandardCharsets.UTF_8.toString());

            String timeStepsValue = "1h";
            String urlString = URL_BASE + "?location=" + encodedLocation
                    + "&fields=" + encodeFields
                    + "&timesteps=" + timeStepsValue
                    + "&units=metric"
                    + "&apikey=" + API_NYCKEL;

            System.out.println("Färdigställd URL: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null){
                    response.append(inputLine);
                }
                bufferedReader.close();
                return response.toString();
            }else {
                return "{\"error\":\"GET request failade och returnerade http status: " + responseCode + "\"}";
            }



        }catch (Exception e){
            e.printStackTrace();
            return "{\"error\": \"Exception i VäderDatan: " + e.getMessage() + "\"}";
        }

    }
}
