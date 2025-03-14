package com.example.trafficlights.service;

import com.example.trafficlights.logger.LogCounter;
import com.example.trafficlights.model.TrafficLight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.example.trafficlights.model.enums.Colors.*;
import static com.example.trafficlights.model.enums.Roads.*;

public class TrafficLightManager {
    private static final Logger logger = LoggerFactory.getLogger(TrafficLightManager.class);
    public static final int MAX_CYCLE_STEP = 6;
    private int currentCycleStep = 0;

    public void initTrafficLights(Map<String, TrafficLight> trafficLights, String road) {
        currentCycleStep = 1;
        if(road.equals(NORTH) || road.equals(SOUTH) ) {
            trafficLights.get(NORTH).setColor(GREEN);
            trafficLights.get(SOUTH).setColor(GREEN);
            trafficLights.get(EAST).setColor(RED);
            trafficLights.get(WEST).setColor(RED);
            logger.info("[{}] Set NORTH-SOUTH to GREEN, EAST-WEST to RED",
                    LogCounter.getNextNumber());
        } else {
            trafficLights.get(EAST).setColor(GREEN);
            trafficLights.get(WEST).setColor(GREEN);
            trafficLights.get(NORTH).setColor(RED);
            trafficLights.get(SOUTH).setColor(RED);
            logger.info("[{}] Set EAST-WEST to GREEN, NORTH-SOUTH to RED",
                    LogCounter.getNextNumber());
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
                    logger.info("[{}] Changed {} traffic light: GREEN -> YELLOW",
                            LogCounter.getNextNumber(), direction);
                }
                else if(YELLOW.equals(trafficLights.get(direction).getColor()) && currentCycleStep == 1) {
                    trafficLights.get(direction).setColor(RED);
                    logger.info("[{}] Changed {} traffic light: YELLOW -> RED",
                            LogCounter.getNextNumber(), direction);
                }
                else if(RED.equals(trafficLights.get(direction).getColor()) && currentCycleStep == 1) {
                    trafficLights.get(direction).setColor(GREEN);
                    logger.info("[{}] Changed {} traffic light: RED -> GREEN",
                            LogCounter.getNextNumber(), direction);
                }
            }
        }

    }

}
