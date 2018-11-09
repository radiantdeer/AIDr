package com.aidr.aidr.Model;

public class Hospital {

    private String name;
    private String address;
    private double rating;
    private int numRating;
    private double distance;

    public Hospital(String name, String address, double rating, int numRating, double distance) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.numRating = numRating;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumRating() {
        return numRating;
    }

    public void setNumRating(int numRating) {
        this.numRating = numRating;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
