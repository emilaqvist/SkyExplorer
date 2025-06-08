package dto;


import java.util.List;

/**
 * Represents the details of a flight search result.
 * Contains information such as price, departure and arrival data, duration, number of stops, airline, and a list of flight segments.
 *
 *
 * @author Mahyar Baghal Shirvan
 */
public class FlightResult {
    private int price;
    private String formattedPrice;
    private String departure;
    private String arrival;
    private int duration;
    private int stops;
    private String airline;
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private String departureCity;
    private String arrivalCity;
    private List<FlightSegment> segments;

    /**
     * Private constructor used by the Builder to instantiate a FlightResult.
     *
     * @param builder The builder object containing values for all fields.
     */

    private FlightResult(Builder builder) {
        this.price = (int) builder.price;
        this.formattedPrice = builder.formattedPrice;
        this.departureCity = builder.departureCity;
        this.arrivalCity = builder.arrivalCity;
        this.departure = builder.departure;
        this.arrival = builder.arrival;
        this.duration = builder.duration;
        this.stops = builder.stops;
        this.airline = builder.airline;
        this.departureAirport = builder.departureAirport;
        this.arrivalAirport = builder.arrivalAirport;
        this.flightNumber = builder.flightNumber;
        this.segments = builder.segments;
    }


    /**
     * Builder class for constructing FlightResult instances.
     * Provides fluent-style methods for setting optional fields.
     */
    // Builder class
    public static class Builder {
        private String id;
        private double price;
        private String formattedPrice;
        private String departureCity;
        private String arrivalCity;
        private String departure;
        private String arrival;
        private int duration;
        private int stops;
        private String airline;
        private String flightNumber;
        private List<FlightSegment> segments;

        private String departureAirport;
        private String arrivalAirport;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder formattedPrice(String formattedPrice) {
            this.formattedPrice = formattedPrice;
            return this;
        }

        public Builder departureCity(String departureCity) {
            this.departureCity = departureCity;
            return this;
        }

        public Builder arrivalCity(String arrivalCity) {
            this.arrivalCity = arrivalCity;
            return this;
        }

        public Builder departure(String departure) {
            this.departure = departure;
            return this;
        }

        public Builder arrival(String arrivalTime) {
            this.arrival = arrivalTime;
            return this;
        }

        public Builder flightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
            return this;
        }

        public Builder segments(List<FlightSegment> segments) {
            this.segments = segments;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder departureAirport(String departureAirport) {
            this.departureAirport = departureAirport;
            return this;
        }

        public Builder arrivalAirport(String arrivalAirport) {
            this.arrivalAirport = arrivalAirport;
            return this;
        }

        public Builder stops(int stops) {
            this.stops = stops;
            return this;
        }

        public Builder airline(String airline) {
            this.airline = airline;
            return this;
        }


        /**
         * Builds a "FlightResult" object.
         *
         * @return A new `FlightResult` instance.
         */
        public FlightResult build() {
            return new FlightResult(this);
        }
    }

    /**
     * Returns a new Builder instance for constructing a "FlightResult".
     *
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }
}
