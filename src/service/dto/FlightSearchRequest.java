package service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
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

    private String departureAirport;  // Nytt fält för avreseflygplats
    private String arrivalAirport;    // Nytt fält för ankomstflygplats

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

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getInfants() {
        return infants;
    }

    public void setInfants(int infants) {
        this.infants = infants;
    }

    public String getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<String> getStops() {
        return stops;
    }

    public void setStops(List<String> stops) {
        this.stops = stops;
    }

    // Getters and Setters for new fields
    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }
}
