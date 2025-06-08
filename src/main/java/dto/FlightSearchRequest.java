package dto;

import java.util.List;

/**
 * Represents a request for searching flights.
 *
 * This class encapsulates all necessary parameters for flight search operations,
 * including departure and arrival locations, date, passenger details, cabin class, currency, and allowed stops.
 * It is used to communicate structured input to external flight APIs.
 *
 * Default values are applied for optional fields like cabin class, number of children or infants,
 * currency, and stops.
 *
 * @author Mahyar Baghal Shirvan
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
    private List<Integer> stops; // Uppdaterad: Integer istället för String

    /**
     * Constructs a new FlightSearchRequest with mandatory parameters.
     * Default values are set for optional parameters to simplify use.
     *
     * @param fromLocation The origin Sky ID (e.g., "PARI").
     * @param toLocation The destination Sky ID (e.g., "LOND").
     * @param departDate The departure date in YYYY-MM-DD format.
     */
    public FlightSearchRequest(String fromLocation, String toLocation, String departDate) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.departDate = departDate;
        this.adults = 1;                // Default: 1 vuxen
        this.children = 0;              // Default: inga barn
        this.infants = 0;               // Default: inga spädbarn
        this.cabinClass = "economy";    // Default: ekonomi-klass
        this.currency = "USD";          // Default: amerikanska dollar
        this.stops = List.of(0, 1, 2);  // Default: tillåt alla antal stopp
    }

    // Getters (om du vill lägga till setters också, säg till)

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

    public List<Integer> getStops() {
        return stops;
    }
}
