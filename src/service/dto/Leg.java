package service.dto;

import java.util.List;


public class Leg {

    private Airport origin;
    private Airport destination;
    private int durationInMinutes;
    private int stopCount;
    private String departure;
    private String arrival;
    private Carriers carriers;
    // Getters and Setters

    public Airport getOrigin() {
        return origin;
    }


    public Airport getDestination() {
        return destination;
    }


    public int getDurationInMinutes() {
        return durationInMinutes;
    }


    public int getStopCount() {
        return stopCount;
    }


    public String getDeparture() {
        return departure;
    }


    public String getArrival() {
        return arrival;
    }


    public Carriers getCarriers() {
        return carriers;
    }



}