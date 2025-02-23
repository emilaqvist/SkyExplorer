package service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Carriers {
    private List<Airline> marketing;
    private String operationType;

    public Carriers() {}

    public List<Airline> getMarketing() {
        return marketing;
    }

    public void setMarketing(List<Airline> marketing) {
        this.marketing = marketing;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}