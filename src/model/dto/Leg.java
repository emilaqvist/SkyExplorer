package model.dto;


/**
 * Represents a flight leg, which is a single segment of a journey.
 * It contains details about the departure and arrival airports, flight duration,
 * stop count, departure and arrival times, and the carriers operating the flight.
 * @author Mahyar
 */
public class Leg {

    private Airport origin;
    private Airport destination;
    private int durationInMinutes;
    private int stopCount;
    private String departure;
    private String arrival;
    private Carriers carriers;
    // Getters and Setters

    /**
     * Gets the origin airport of the flight leg.
     *
     * @return the origin {@link Airport}
     *
     */
    public Airport getOrigin() {
        return origin;
    }


    /**
     * Gets the destination airport of the flight leg.
     *
     * @return the destination {@link Airport}
     */
    public Airport getDestination() {
        return destination;
    }


    /**
     * Gets the duration of the flight leg in minutes.
     *
     * @return the duration in minutes
     */
    public int getDurationInMinutes() {
        return durationInMinutes;
    }


    /**
     * Gets the number of stops during the flight leg.
     *
     * @return the number of stops
     */
    public int getStopCount() {
        return stopCount;
    }


    /**
     * Gets the departure time of the flight leg.
     *
     * @return the departure time as a string
     */
    public String getDeparture() {
        return departure;
    }


    /**
     * Gets the arrival time of the flight leg.
     *
     * @return the arrival time as a string
     */
    public String getArrival() {
        return arrival;
    }


    /**
     * Gets the carriers operating the flight leg.
     *
     * @return the {@link Carriers} information
     */
    public Carriers getCarriers() {
        return carriers;
    }



}