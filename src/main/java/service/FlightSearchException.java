package service;

/**
 * a klass for errors.
 *
 * @author Mahyar
 */
public class FlightSearchException extends Exception {

    private int httpStatus;
    public FlightSearchException(String message, Exception e, int httpStatus) {

        super(message);
        this.httpStatus = httpStatus;
    }

    public FlightSearchException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}