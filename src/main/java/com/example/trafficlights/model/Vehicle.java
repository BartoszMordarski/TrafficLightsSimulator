package com.example.trafficlights.model;

public class Vehicle {
    private String id;
    private String startRoad;
    private String endRoad;

    public Vehicle(String id, String startRoad, String endRoad) {
        this.id = id;
        this.startRoad = startRoad;
        this.endRoad = endRoad;
    }

    public String getId() {
        return id;
    }

    public String getStartRoad() {
        return startRoad;
    }

    public String getEndRoad() {
        return endRoad;
    }
}
