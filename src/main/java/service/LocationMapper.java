package service;

import java.util.HashMap;
import java.util.Map;


/**
 * The {@code LocationMapper} class provides a mapping between city names and their corresponding airport codes.
 * This mapping is used for API calls that require airport codes instead of city names.
 * @author  Emil Åqvist
 * @author Mahyar Baghal Shirvan
 */
public class LocationMapper {
    // map för flygplatsers, förkortning som behövs för API för
    private static final Map<String, String> flightIdMapping = new HashMap<>();

    static {
        flightIdMapping.put("Stockholm", "ARN");
        flightIdMapping.put("Oslo", "OSL");

        flightIdMapping.put("Stockholm", "ARN");
        flightIdMapping.put("Göteborg", "GOT");
        flightIdMapping.put("Malmö", "MMX");
        flightIdMapping.put("Oslo", "OSL");
        flightIdMapping.put("Bergen", "BGO");
        flightIdMapping.put("Trondheim", "TRD");
        flightIdMapping.put("Köpenhamn", "CPH");
        flightIdMapping.put("Helsingfors", "HEL");
        flightIdMapping.put("Reykjavik", "KEF");

// Europa
        flightIdMapping.put("London", "LHR");
        flightIdMapping.put("Paris", "CDG");
        flightIdMapping.put("Amsterdam", "AMS");
        flightIdMapping.put("Frankfurt", "FRA");
        flightIdMapping.put("Berlin", "BER");
        flightIdMapping.put("Madrid", "MAD");
        flightIdMapping.put("Barcelona", "BCN");
        flightIdMapping.put("Rom", "FCO");
        flightIdMapping.put("Milano", "MXP");
        flightIdMapping.put("Wien", "VIE");
        flightIdMapping.put("Zürich", "ZRH");
        flightIdMapping.put("Genève", "GVA");
        flightIdMapping.put("Bryssel", "BRU");
        flightIdMapping.put("Lissabon", "LIS");
        flightIdMapping.put("Aten", "ATH");
        flightIdMapping.put("Istanbul", "IST");
        flightIdMapping.put("Warszawa", "WAW");
        flightIdMapping.put("Prag", "PRG");
        flightIdMapping.put("Budapest", "BUD");
        flightIdMapping.put("Dublin", "DUB");

// Nordamerika
        flightIdMapping.put("New York", "JFK");
        flightIdMapping.put("Los Angeles", "LAX");
        flightIdMapping.put("Chicago", "ORD");
        flightIdMapping.put("Toronto", "YYZ");
        flightIdMapping.put("Vancouver", "YVR");
        flightIdMapping.put("San Francisco", "SFO");
        flightIdMapping.put("Miami", "MIA");
        flightIdMapping.put("Dallas", "DFW");
        flightIdMapping.put("Atlanta", "ATL");
        flightIdMapping.put("Boston", "BOS");
        flightIdMapping.put("Washington", "IAD");
        flightIdMapping.put("Seattle", "SEA");
        flightIdMapping.put("Las Vegas", "LAS");
        flightIdMapping.put("Montreal", "YUL");
        flightIdMapping.put("Mexico City", "MEX");

// Asien
        flightIdMapping.put("Tokyo", "NRT");
        flightIdMapping.put("Peking", "PEK");
        flightIdMapping.put("Shanghai", "PVG");
        flightIdMapping.put("Hong Kong", "HKG");
        flightIdMapping.put("Singapore", "SIN");
        flightIdMapping.put("Bangkok", "BKK");
        flightIdMapping.put("Seoul", "ICN");
        flightIdMapping.put("Dubai", "DXB");
        flightIdMapping.put("Abu Dhabi", "AUH");
        flightIdMapping.put("Doha", "DOH");
        flightIdMapping.put("Mumbai", "BOM");
        flightIdMapping.put("Delhi", "DEL");
        flightIdMapping.put("Kuala Lumpur", "KUL");
        flightIdMapping.put("Jakarta", "CGK");
        flightIdMapping.put("Manila", "MNL");
        flightIdMapping.put("Taipei", "TPE");

// Australien och Oceanien
        flightIdMapping.put("Sydney", "SYD");
        flightIdMapping.put("Melbourne", "MEL");
        flightIdMapping.put("Brisbane", "BNE");
        flightIdMapping.put("Perth", "PER");
        flightIdMapping.put("Auckland", "AKL");
        flightIdMapping.put("Wellington", "WLG");

// Afrika
        flightIdMapping.put("Johannesburg", "JNB");
        flightIdMapping.put("Kapstaden", "CPT");
        flightIdMapping.put("Kairo", "CAI");
        flightIdMapping.put("Nairobi", "NBO");
        flightIdMapping.put("Casablanca", "CMN");
        flightIdMapping.put("Lagos", "LOS");
        flightIdMapping.put("Addis Abeba", "ADD");

// Mellanöstern
        flightIdMapping.put("Tel Aviv", "TLV");
        flightIdMapping.put("Beirut", "BEY");
        flightIdMapping.put("Amman", "AMM");
        flightIdMapping.put("Riyadh", "RUH");
        flightIdMapping.put("Jeddah", "JED");

        flightIdMapping.put("Mallorca", "PMI");
        flightIdMapping.put("Ibiza", "IBZ");
        flightIdMapping.put("Malaga", "AGP");
        flightIdMapping.put("Alicante", "ALC");
        flightIdMapping.put("Nice", "NCE");
        flightIdMapping.put("Kreta", "HER");
        flightIdMapping.put("Rhodos", "RHO");
        flightIdMapping.put("Antalya", "AYT");
        flightIdMapping.put("Phuket", "HKT");
        flightIdMapping.put("Bali", "DPS");
        flightIdMapping.put("Maldiverna", "MLE");
        flightIdMapping.put("Cancun", "CUN");
        flightIdMapping.put("Punta Cana", "PUJ");

    }

    /**
     * Returns the airport code if the city is mapped, otherwise returns the original city name.
     *
     * @param city The user-input city name.
     * @return The corresponding airport code if found, or the original city name.
     * @author  Emil
     * @author Mahyar
     */
    public static String getFlightId(String city) {
        if (city == null) {
            return null;
        }
        // tar bort bara extra white space
        String trimmed = city.trim();
        return flightIdMapping.getOrDefault(trimmed, trimmed);
    }
}
