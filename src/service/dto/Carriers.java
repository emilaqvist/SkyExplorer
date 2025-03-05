package service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class Carriers {
    private List<Airline> marketing;

    public List<Airline> getMarketing() {
        return marketing;
    }
}