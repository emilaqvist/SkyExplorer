package model.dto;


/**
 * Represents an airport with its name and the city it is located in.
 * This class is typically used to store departure and arrival airport details.'
 * @author Mahyar
 */

public class Airport {

    private String name;
    private String city;


    /**
     * Retrieves the name of the airport.
     *
     * @return The airport name as a {@link String}.
     */
    // Getters
    public String getName() {
        return name;
    }


    /**
     * Retrieves the city where the airport is located.
     *
     * @return The city name as a {@link String}.
     */
    public String getCity() {
        return city;
    }

}