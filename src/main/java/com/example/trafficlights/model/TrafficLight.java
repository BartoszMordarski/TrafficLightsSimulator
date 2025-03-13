package com.example.trafficlights.model;


import com.example.trafficlights.model.enums.Colors;

public class TrafficLight {
    private Colors color;


    public TrafficLight(Colors color) {
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }
}
