package service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
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
    private Airport departureAirport;
    private String arrivalAirport;

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

    // Getters
    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public int getDuration() {
        return duration;
    }

    public int getStops() {
        return stops;
    }

    public String getAirline() {
        return airline;
    }

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

        private Airport departureAirport;
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

        public Builder departureAirport(Airport departureAirport) {
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

        public FlightResult build() {
            return new FlightResult(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
