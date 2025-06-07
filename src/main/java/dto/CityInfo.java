package dto;

/**
 * Represents basic city information retrieved from Wikipedia.
 * Includes the city's title, a short description, an image URL, and a link to the full article.
 *
 * @author Amer Sabaredzovic
 * @author Algot
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
