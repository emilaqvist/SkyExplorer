package model.dto;
import java.util.List;

/**
 * Represents the carriers (airlines) associated with a flight.
 * This class is mainly used for retrieving marketing airline details.
 * @author Mahyar
 */
public class Carriers {
    private List<Airline> marketing;

    /**
     * Retrieves the list of marketing airlines.
     *
     * @return A list of {@link Airline} objects representing the marketing airlines.
     */
    public List<Airline> getMarketing() {
        return marketing;
    }
}