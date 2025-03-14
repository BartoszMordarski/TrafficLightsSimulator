package com.example.trafficlights.service;

import com.example.trafficlights.model.TrafficLight;
import com.example.trafficlights.model.Vehicle;


import java.util.*;

import static com.example.trafficlights.model.enums.Colors.RED;
import static com.example.trafficlights.model.enums.Roads.*;

public class Intersection {
    private final Map<String, Queue<Vehicle>> roads = new HashMap<>();
    private final Map<String, TrafficLight> trafficLights = new HashMap<>();
    TrafficLightManager trafficLightManager = new TrafficLightManager();
    TrafficManager trafficManager = new TrafficManager();

    public Intersection() {
        for (String direction : DIRECTIONS) {
            roads.put(direction, new LinkedList<>());
            trafficLights.put(direction, new TrafficLight(RED));
        }
    }

    private boolean isAnyCarOnTheRoad() {
        for(String direction : roads.keySet()) {
            if(!roads.get(direction).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void addVehicle(String startRoad, Vehicle vehicle) {
        if(!isAnyCarOnTheRoad()) {
            trafficLightManager.initTrafficLights(trafficLights, vehicle.getStartRoad());
        }
        roads.get(startRoad).add(vehicle);
    }

    public List<String> step() {
        List<String> leftVehicles = new ArrayList<>();

        List<String> directionsToGo = trafficManager.getDirectionsToGo(roads, trafficLights);
        for (String direction : directionsToGo) {
            Queue<Vehicle> queue = roads.get(direction);
            if (!queue.isEmpty()) {
                Vehicle vehicle = queue.poll();
                leftVehicles.add(vehicle.getId());
            }
        }
        trafficLightManager.updateTrafficLightsCycle(trafficLights);
        return leftVehicles;
    }

}