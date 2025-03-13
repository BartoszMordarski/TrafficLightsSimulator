package com.example.trafficlights.service;

import com.example.trafficlights.model.TrafficLight;

import java.util.Map;

import static com.example.trafficlights.model.enums.Colors.*;
import static com.example.trafficlights.model.enums.Roads.*;

public class TrafficLightManager {
    public static final int MAX_CYCLE_STEP = 6;
    private int currentCycleStep = 0;

    public void initTrafficLights(Map<String, TrafficLight> trafficLights, String road) {
        currentCycleStep = 1;
        if(road.equals(NORTH) || road.equals(SOUTH) ) {
            trafficLights.get(NORTH).setColor(GREEN);
            trafficLights.get(SOUTH).setColor(GREEN);
            trafficLights.get(EAST).setColor(RED);
            trafficLights.get(WEST).setColor(RED);
        } else {
            trafficLights.get(EAST).setColor(GREEN);
            trafficLights.get(WEST).setColor(GREEN);
            trafficLights.get(NORTH).setColor(RED);
            trafficLights.get(SOUTH).setColor(RED);
        }

    };


    public void updateTrafficLightsCycle(Map<String, TrafficLight> trafficLights) {
        currentCycleStep++;
        if(currentCycleStep > MAX_CYCLE_STEP) {
            currentCycleStep = 1;
        }

        if(currentCycleStep == 1 || currentCycleStep == MAX_CYCLE_STEP){
            for (String direction : DIRECTIONS) {

                if(GREEN.equals(trafficLights.get(direction).getColor()) && currentCycleStep == MAX_CYCLE_STEP) {
                    trafficLights.get(direction).setColor(YELLOW);
                }
                else if(YELLOW.equals(trafficLights.get(direction).getColor()) && currentCycleStep == 1) {
                    trafficLights.get(direction).setColor(RED);
                }
                else if(RED.equals(trafficLights.get(direction).getColor()) && currentCycleStep == 1) {
                    trafficLights.get(direction).setColor(GREEN);
                }
            }
        }

    }

}
