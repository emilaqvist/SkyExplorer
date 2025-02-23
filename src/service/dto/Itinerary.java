package service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Itinerary {
    private String id;
    private Price price;
    private List<Leg> legs;

    public Itinerary() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public static class Price {
        private double raw;
        private String formatted;
        private String pricingOptionId;

        public Price() {}

        public double getRaw() {
            return raw;
        }

        public void setRaw(double raw) {
            this.raw = raw;
        }

        public String getFormatted() {
            return formatted;
        }

        public void setFormatted(String formatted) {
            this.formatted = formatted;
        }

        public String getPricingOptionId() {
            return pricingOptionId;
        }

        public void setPricingOptionId(String pricingOptionId) {
            this.pricingOptionId = pricingOptionId;
        }
    }
}