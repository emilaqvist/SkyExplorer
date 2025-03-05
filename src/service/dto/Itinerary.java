package service.dto;


import java.util.List;

public class Itinerary {
    private String id;
    private Price price;
    private List<Leg> legs;

    public String getId() {
        return id;
    }

    public Price getPrice() {
        return price;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public static class Price {
        private double raw;
        private String formatted;

        public double getRaw() {
            return raw;
        }

        public String getFormatted() {
            return formatted;
        }

    }
}