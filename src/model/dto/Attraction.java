package model.dto;

/**
 * Representerar en attraction med detaljerad information via alla parametrar
 * @author Mahyar
 * @author Emil
 */
public class Attraction {
    private String xid;
    private String name;
    private String kinds;
    private String description;
    private String imageUrl;
    private double latitude;
    private double longitude;

    // Getters och setters för att setta detaljer för varje attraction object och sedan kunna hämta dem.
    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}