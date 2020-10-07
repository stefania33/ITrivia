package com.example.triviaapp.model;

public class UserLocation {

    private String userId;
    private double latitude;
    private double longitude;

    public UserLocation() {
    }

    public UserLocation(String userId, double latitude, double longitude) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitute(double longitude) {
        this.longitude = longitude;
    }
}
