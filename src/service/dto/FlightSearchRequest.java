package service.dto;

import java.util.List;


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

    public FlightSearchRequest(String fromLocation, String toLocation, String departDate) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.departDate = departDate;
        this.adults = 1;
        this.children = 0;
        this.infants = 0;
        this.cabinClass = "economy";
        this.currency = "USD";
        this.stops = List.of("direct", "1stop", "2stops");
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
