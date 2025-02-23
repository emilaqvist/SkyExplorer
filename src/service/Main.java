package service;

import service.dto.FlightResult;
import service.dto.FlightSearchRequest;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Använd din API-nyckel här
            String apiKey = "ca90664ac7msh43de15acd4241b8p1b2c8ejsn35c18291a913";
            FlightService flightService = new FlightService(apiKey);

            // Skapa en sökförfrågan
            FlightSearchRequest request = new FlightSearchRequest(
                    "BCN",  // Från Barcelona
                    "ARN",  // Till Stockholm
                    "2025-02-23"  // Avgångsdatum
            );

            // Sök efter flyg
            List<FlightResult> flights = flightService.searchFlights(request);

            // Skriv ut resultaten
            System.out.println("Hittade " + flights.size() + " flyg:");
            flights.forEach(flight -> {
                System.out.println("\n=========================");
                System.out.printf("Flygbolag: %s\n", flight.getAirline());
                System.out.printf("Pris: %s\n", flight.getFormattedPrice());
                System.out.printf("Från: %s till %s\n", flight.getDepartureCity(), flight.getArrivalCity());
                System.out.printf("Avgång: %s\n", flight.getDepartureTime());
                System.out.printf("Ankomst: %s\n", flight.getArrivalTime());
                System.out.printf("Längd: %d minuter\n", flight.getDuration());
                System.out.printf("Antal stopp: %d\n", flight.getStops());
                System.out.printf("Funkar, arrival airport får jag %s\n", flight.getArrivalAirport());
                //adda Departure också
            });

        } catch (FlightSearchException e) {
            System.err.println("Fel vid sökning av flyg: " + e.getMessage());
            e.printStackTrace();
        }
    }
}