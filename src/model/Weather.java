package model;

public class Weather {
    private double temperatur;
    private String nederbordTyp;

    public Weather(){

    }
    public Weather(double temperatur,String nederbordTyp){
        this.nederbordTyp = nederbordTyp;
        this.temperatur = temperatur;
    }

    public double getTemperatur() {
        return temperatur;
    }

    public void setTemperatur(double temperatur) {
        this.temperatur = temperatur;
    }

    public String getNederbordTyp() {
        return nederbordTyp;
    }

    public void setNederbordTyp(String nederbordTyp) {
        this.nederbordTyp = nederbordTyp;
    }
}
