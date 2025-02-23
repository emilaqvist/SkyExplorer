package service;

public class FlightSearchException extends Exception {
    public FlightSearchException(String message) {
        super(message);
    }

    public FlightSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}