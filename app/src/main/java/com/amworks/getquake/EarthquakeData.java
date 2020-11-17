package com.amworks.getquake;

public class EarthquakeData {

    private String place;
    private String date;
    private float magnitude;

    public EarthquakeData(float magnitude,String place, String date) {
        this.place = place;
        this.date = date;
        this.magnitude = magnitude;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public String getPlace() {
        return place;
    }

    public String getDate() {
        return date;
    }

    public float getMagnitude() {
        return magnitude;
    }
}
