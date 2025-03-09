package model.dto;



/**
 * Represents the details of a flight search result.
 * This class contains essential information such as price, departure and arrival details, duration, stops, and airline information.
 * It follows the **Builder pattern** to allow for flexible object creation.
 * @author Mahyar
 */
public class FlightResult {
    private String id;
    private double price;
    private String formattedPrice;
    private String departureCity;
    private String arrivalCity;
    private String departureTime;
    private String arrivalTime;
    private int duration;
    private int stops;
    private String airline;
    private String departureAirport;
    private String arrivalAirport;

    /**
     * Private constructor for `FlightResult`, used by the Builder class.
     *
     * @param builder The builder instance used to construct a `FlightResult` object.
     */

    private FlightResult(Builder builder) {
        this.id = builder.id;
        this.price = builder.price;
        this.formattedPrice = builder.formattedPrice;
        this.departureCity = builder.departureCity;
        this.arrivalCity = builder.arrivalCity;
        this.departureTime = builder.departureTime;
        this.arrivalTime = builder.arrivalTime;
        this.duration = builder.duration;
        this.stops = builder.stops;
        this.airline = builder.airline;
        this.departureAirport = builder.departureAirport;
        this.arrivalAirport = builder.arrivalAirport;
    }


    /**
     * Builder class for creating `FlightResult` instances.
     * This allows for a flexible and readable way of constructing a flight result object.
     */
    // Builder class
    public static class Builder {
        private String id;
        private double price;
        private String formattedPrice;
        private String departureCity;
        private String arrivalCity;
        private String departureTime;
        private String arrivalTime;
        private int duration;
        private int stops;
        private String airline;

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

        public Builder departureTime(String departureTime) {
            this.departureTime = departureTime;
            return this;
        }

        public Builder arrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
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
