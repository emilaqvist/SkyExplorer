package dto;

import lombok.Builder;
import lombok.Data;
/**
 * Represents a single segment of a flight journey.
 *
 * A flight segment contains details such as the departure and arrival locations,
 * times, airline, and flight number.
 *
 * For example, a multi-leg journey from Stockholm to New York via Frankfurt
 * would contain two {@code FlightSegment} instances: one from Stockholm to Frankfurt,
 * and another from Frankfurt to New York.
 *
 * This class uses Lombok's {@code @Data} and {@code @Builder} annotations
 * to automatically generate getters, setters, toString, equals, hashCode, and a builder pattern.
 *
 * @author Mahyar Baghal Shirvan
 */
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
