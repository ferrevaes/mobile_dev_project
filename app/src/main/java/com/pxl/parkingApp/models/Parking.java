package com.pxl.parkingApp.models;

import java.io.Serializable;

public class Parking implements Serializable {
    private String name;
    private int total_places;
    private int available_places;
    private double longitude;
    private double latitude;
    private String address;

    public Parking(){}

    public Parking(String name, int total_places, int available_places, float longitude, float latitude, String address) {
        this.name = name;
        this.total_places = total_places;
        this.available_places = available_places;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotal_places(int total_places) {
        this.total_places = total_places;
    }

    public void setAvailable_places(int available_places) {
        this.available_places = available_places;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public int getTotal_places() {
        return total_places;
    }

    public int getAvailable_places() {
        return available_places;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
