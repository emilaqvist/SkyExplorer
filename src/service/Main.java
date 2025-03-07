package service;
/*

//detta var en test klass i början för att testa API och ras sponse som vi fick
import misc.Configurator;
import service.dto.FlightResult;
import service.dto.FlightSearchRequest;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {

            String apiKey = Configurator.getProperty("FLIGHT_API_KEY");
            FlightService flightService = new FlightService(apiKey);


            FlightSearchRequest request = new FlightSearchRequest(
                    "BCN",  // Från Barcelona
                    "ARN",  // Stockholm
                    "2025-03-06"  // Avgångsdatum
            );

            // Sök efter flyg
            List<FlightResult> flights = flightService.searchFlights(request);

            // Skriver ut resultaten
            System.out.println("Hittade " + flights.size() + " flyg:");
            flights.forEach(flight -> {
                System.out.println("\n=========================");
                System.out.printf("Flygbolag: %s\n ", flight.getAirline());
                System.out.printf("Pris: %s\n ", flight.getFormattedPrice());
                System.out.printf("Från: %s till %s\n ", flight.getDepartureCity(), flight.getArrivalCity());
                System.out.printf("Avgång: %s\n ", flight.getDepartureTime().replace("T", " Time: "));
                System.out.printf("Ankomst: %s\n ", flight.getArrivalTime().replace("T", " Time: "));
                System.out.printf("Längd: %d minuter\n ", flight.getDuration());
                System.out.printf("Antal stopp: %d\n ", flight.getStops());
                System.out.printf("Departure Airport:  %s\n ", flight.getDepartureAirport());
                System.out.printf("Arrival Airport:  %s\n ", flight.getArrivalAirport());

            });

        } catch (FlightSearchException e) {
            System.err.println("Fel vid sökning av flyg: " + e.getMessage());
            e.printStackTrace();
        }
    }
}*/