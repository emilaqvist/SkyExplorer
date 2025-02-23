package service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightResponse {
    private FlightData data;

    public FlightResponse() {}

    public FlightData getData() {
        return data;
    }

    public void setData(FlightData data) {
        this.data = data;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlightData {
        private Context context;
        private List<Itinerary> itineraries;

        public FlightData() {}

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public List<Itinerary> getItineraries() {
            return itineraries;
        }

        public void setItineraries(List<Itinerary> itineraries) {
            this.itineraries = itineraries;
        }
    }

    public static class Context {
        private String status;
        private String sessionId;
        private int totalResults;

        public Context() {}

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public int getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(int totalResults) {
            this.totalResults = totalResults;
        }
    }
}