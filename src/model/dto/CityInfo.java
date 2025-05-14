package model.dto;

/**
 * Representerar en attraction med detaljerad information via alla parametrar
 * @author Mahyar
 * @author Emil
 */
public class CityInfo {
    private String title;  // Namnet på sevärdheten
    private String description;  // Kort beskrivning
    private String url;  // Länk till Wikipedia-artikeln
    private String imageUrl; // Bild-URL (om finns)

    // Konstruktor
    public CityInfo(String title, String description, String imageUrl, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    // Getters och Setters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }
    public String getImageUrl() { return imageUrl; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setUrl(String url) { this.url = url; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}
