package model.dto;

/**
 * Representerar en attraction med detaljerad information via alla parametrar
 * @author Mahyar
 * @author Emil
 */
public class Attraction {

    private String name;
    private String description;
    private String imageUrl;


    // Getters och setters för att setta detaljer för varje attraction object och sedan kunna hämta dem.


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}