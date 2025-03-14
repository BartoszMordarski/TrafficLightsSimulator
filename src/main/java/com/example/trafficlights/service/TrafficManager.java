package com.example.trafficlights.service;

import com.example.trafficlights.model.TrafficLight;
import com.example.trafficlights.model.Vehicle;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.trafficlights.model.enums.Colors.*;
import static com.example.trafficlights.model.enums.Roads.*;

@Service
public class TrafficManager {

    public List<String> getDirectionsToGo(Map<String, Queue<Vehicle>> roads, Map<String, TrafficLight> trafficLights) {
        List<String> directionsToGo = new ArrayList<>();
        for (String direction : roads.keySet()) {

            if (!roads.get(direction).isEmpty() && trafficLights.get(direction).getColor().equals(GREEN)) {
                if (!isTurnLeft(roads, direction)) {
                    directionsToGo.add(direction);
                } else {
                    String oppositeDirection = getOppositeDirection(direction);
                    if (roads.get(oppositeDirection).isEmpty()) {
                        directionsToGo.add(direction);
                    }
                    else if (!roads.get(oppositeDirection).isEmpty() &&
                            isTurnLeft(roads, oppositeDirection) &&
                            trafficLights.get(oppositeDirection).getColor().equals(GREEN)) {
                        directionsToGo.add(direction);
                    }
                }
            }

            if (!roads.get(direction).isEmpty() && trafficLights.get(direction).getColor().equals(YELLOW)) {
                if (isTurnLeft(roads, direction)) {
                    directionsToGo.add(direction);
                }
            }

            if (!roads.get(direction).isEmpty() && trafficLights.get(direction).getColor().equals(RED)) {
                if (isTurnRight(roads, direction)) {
                    if(!isCarToTheLeftGoingStraight(direction, roads)) {
                        directionsToGo.add(direction);
                    }
                }
            }


        }
        return directionsToGo;
    }

    private String getOppositeDirection(String direction) {
        return switch (direction) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
            default -> direction;
        };
    }

    private boolean isCarToTheLeftGoingStraight(String direction, Map<String, Queue<Vehicle>> roads) {

        String directionToTheLeft = getDirectionToTheLeft(direction);
        if(!roads.get(directionToTheLeft).isEmpty()) {
            Vehicle vehicle = roads.get(directionToTheLeft).peek();
            return isGoingStraight(vehicle.getStartRoad(), vehicle.getEndRoad());
        }

        return false;

    }

    private String getDirectionToTheLeft(String direction) {
        return switch (direction) {
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case EAST -> SOUTH;
            case WEST -> NORTH;
            default -> direction;
        };
    }

    private boolean isGoingStraight(String startRoad, String endRoad) {
        String expectedEndRoad = switch (startRoad) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
            default -> startRoad;
        };
        return expectedEndRoad.equals(endRoad);
    }

    public boolean isTurnLeft(Map<String, Queue<Vehicle>> roads, String direction) {
        Vehicle vehicle = roads.get(direction).peek();
        return isTurnLeftCondition(vehicle.getStartRoad(), vehicle.getEndRoad());
    }

    public boolean isTurnRight(Map<String, Queue<Vehicle>> roads, String direction) {
        Vehicle vehicle = roads.get(direction).peek();
        return isTurnRightCondition(vehicle.getStartRoad(), vehicle.getEndRoad());
    }

    private boolean isTurnLeftCondition(String startRoad, String endRoad) {
        return (SOUTH.equals(startRoad) && WEST.equals(endRoad)) ||
                (WEST.equals(startRoad) && NORTH.equals(endRoad)) ||
                (NORTH.equals(startRoad) && EAST.equals(endRoad)) ||
                (EAST.equals(startRoad) && SOUTH.equals(endRoad));
    }

    private boolean isTurnRightCondition(String startRoad, String endRoad) {
        return (SOUTH.equals(startRoad) && EAST.equals(endRoad)) ||
                (EAST.equals(startRoad) && NORTH.equals(endRoad)) ||
                (NORTH.equals(startRoad) && WEST.equals(endRoad)) ||
                (WEST.equals(startRoad) && SOUTH.equals(endRoad));
    }

}
