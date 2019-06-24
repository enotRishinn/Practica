package com.practice.findmap.domain.model;

public class MarkerCoordinates {

    private final double latitude;
    private final double longitude;

    public MarkerCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
