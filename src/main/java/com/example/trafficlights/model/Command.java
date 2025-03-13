package com.example.trafficlights.model;

public class Command {
    private String type;
    private String vehicleId;
    private String startRoad;
    private String endRoad;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartRoad() {
        return startRoad;
    }

    public void setStartRoad(String startRoad) {
        this.startRoad = startRoad;
    }

    public String getEndRoad() {
        return endRoad;
    }

    public void setEndRoad(String endRoad) {
        this.endRoad = endRoad;
    }
}
