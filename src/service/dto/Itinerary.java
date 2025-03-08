package service.dto;


import java.util.List;


/**
 * Represents a flight itinerary, which consists of multiple legs and pricing details.
 * @author Mahyar
 */
public class Itinerary {

    /**
     * The price details of the itinerary.
     */


    private Price price;

    /**
     * The list of flight legs included in the itinerary.
     */
    private List<Leg> legs;



    /**
     * Gets the price details of the itinerary.
     *
     * @return the price object containing raw and formatted price
     */
    public Price getPrice() {
        return price;
    }

    /**
     * Gets the list of flight legs in the itinerary.
     *
     * @return a list of {@link Leg} objects representing different segments of the journey
     */
    public List<Leg> getLegs() {
        return legs;
    }


    /**
     * Represents the price details of an itinerary, including raw and formatted price values.
     */
    public static class Price {

        /**
         * The raw numerical value of the price.
         */
        private double raw;

        /**
         * The formatted price string (e.g., including currency symbols).
         */
        private String formatted;

        public double getRaw() {
            return raw;
        }

        public String getFormatted() {
            return formatted;
        }

    }
}