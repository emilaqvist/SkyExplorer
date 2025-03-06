package service;

import java.util.HashMap;
import java.util.Map;

public class LocationMapper {
    private static final Map<String, String> flightIdMapping = new HashMap<>();

    static {
        flightIdMapping.put("Stockholm", "ARN");
        flightIdMapping.put("Oslo", "OSL");

    }


    public static String getFlightId(String city) {
        if (city == null) {
            return null;
        }
        String trimmed = city.trim();
        return flightIdMapping.getOrDefault(trimmed, trimmed);
    }
}
