package service.dto;


import java.util.List;


/**
 * Represents the response structure for flight data retrieved from an external API.
 * This class contains flight-related details and is used for deserializing JSON responses.
 * @author Mahyar
 */

public class FlightResponse {
    private FlightData data;

    /**
     * Gets the flight data containing the list of itineraries.
     *
     * @return The {@link FlightData} object containing itinerary details.
     */
    public FlightData getData() {
        return data;
    }

    public static class FlightData {

        /** List of flight itineraries included in the response. */
        private List<Itinerary> itineraries;

        /**
         * Gets the list of itineraries for the flight response.
         *
         * @return A list of {@link Itinerary} objects.
         */
        public List<Itinerary> getItineraries() {
            return itineraries;
        }
    }
}