package service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class FlightResponse {
    private FlightData data;

    public FlightData getData() {
        return data;
    }

    public static class FlightData {
        private List<Itinerary> itineraries;
        public List<Itinerary> getItineraries() {
            return itineraries;
        }
    }
}