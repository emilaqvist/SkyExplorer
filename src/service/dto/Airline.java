package service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Airline {
    private String name;

    public Airline() {}

    public String getName() {
        return name;
    }
}