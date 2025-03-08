package service.dto;

import java.util.List;


/**
 * Represents a request for searching flights.
 * This class encapsulates the necessary parameters needed
 * to perform a flight search, such as departure and arrival locations,
 * travel dates, number of passengers, and cabin class.
 * @author Mahyar
 */

public class FlightSearchRequest {
    private String fromLocation;
    private String toLocation;
    private String departDate;
    private int adults;
    private int children;
    private int infants;
    private String cabinClass;
    private String currency;
    private List<String> stops;


    /**
     * Constructs a new FlightSearchRequest with mandatory parameters.
     * Default values are set for optional parameters.
     *
     * @param fromLocation The departure location entity ID.
     * @param toLocation The destination location entity ID.
     * @param departDate The departure date in YYYY-MM-DD format.
     * @author Mahyar
     */
    public FlightSearchRequest(String fromLocation, String toLocation, String departDate) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.departDate = departDate;
        this.adults = 1; //Default, vi satte den till 1
        this.children = 0; //Default, vi satte den till 0
        this.infants = 0; //Default, vi satte den till0
        this.cabinClass = "economy"; //Default, vi satte den till economy
        this.currency = "USD"; //Default, vi satte den till USD
        this.stops = List.of("direct", "1stop", "2stops"); //Default, vi satte den till alla stops
    }

    // Getters and Setters
    public String getFromLocation() {
        return fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public String getDepartDate() {
        return departDate;
    }

    public int getAdults() {
        return adults;
    }

    public int getChildren() {
        return children;
    }

    public int getInfants() {
        return infants;
    }


    public String getCabinClass() {
        return cabinClass;
    }


    public String getCurrency() {
        return currency;
    }


    public List<String> getStops() {
        return stops;
    }

}
