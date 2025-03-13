package com.example.trafficlights.model;

import java.util.ArrayList;
import java.util.List;

public class StepStatus {
    private final List<String> leftVehicles = new ArrayList<>();
    public List<String> getLeftVehicles() {
        return leftVehicles;
    }
}