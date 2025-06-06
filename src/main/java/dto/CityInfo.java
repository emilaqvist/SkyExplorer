package dto;

/**
 * Representerar en attraction med detaljerad information via alla parametrar
 * @author Amer Sabaredzovic
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
}
