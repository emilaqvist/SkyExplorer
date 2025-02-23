package service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)


public class Leg {
    private String id;
    private Airport origin;
    private Airport destination;
    private int durationInMinutes;
    private int stopCount;
    private boolean isSmallestStops;
    private String departure;
    private String arrival;
    private int timeDeltaInDays;
    private Carriers carriers;
    private List<Segment> segments;

    public Leg() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Airport getOrigin() {
        return origin;
    }

    public void setOrigin(Airport origin) {
        this.origin = origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public void setDestination(Airport destination) {
        this.destination = destination;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public int getStopCount() {
        return stopCount;
    }

    public void setStopCount(int stopCount) {
        this.stopCount = stopCount;
    }

    public boolean isSmallestStops() {
        return isSmallestStops;
    }

    public void setSmallestStops(boolean smallestStops) {
        isSmallestStops = smallestStops;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public int getTimeDeltaInDays() {
        return timeDeltaInDays;
    }

    public void setTimeDeltaInDays(int timeDeltaInDays) {
        this.timeDeltaInDays = timeDeltaInDays;
    }

    public Carriers getCarriers() {
        return carriers;
    }

    public Airport getAirPort() {
        return destination;
    }

    public void setCarriers(Carriers carriers) {
        this.carriers = carriers;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}