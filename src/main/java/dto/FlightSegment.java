package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightSegment {
    private String from;
    private String to;
    private String departureTime;
    private String arrivalTime;
    private String airline;
    private String flightNumber;
}
