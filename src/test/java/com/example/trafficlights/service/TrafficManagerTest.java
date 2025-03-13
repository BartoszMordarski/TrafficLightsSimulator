package com.example.trafficlights.service;

import com.example.trafficlights.model.TrafficLight;
import com.example.trafficlights.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static com.example.trafficlights.model.enums.Colors.*;
import static com.example.trafficlights.model.enums.Roads.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

public class TrafficManagerTest {
    private TrafficManager trafficManager;
    private Map<String, Queue<Vehicle>> roads;
    private Map<String, TrafficLight> trafficLights;

    @BeforeEach
    public void setUp() {
        trafficManager = new TrafficManager();
        roads = new HashMap<>();
        trafficLights = new HashMap<>();

        for (String direction : DIRECTIONS) {
            roads.put(direction, new LinkedList<>());
            trafficLights.put(direction, new TrafficLight(RED));
        }
    }

    @Test
    public void testGetDirectionsToGoStraightGreen() {
        trafficLights.get(NORTH).setColor(GREEN);
        Vehicle vehicle = new Vehicle("car1", NORTH, SOUTH);
        roads.get(NORTH).add(vehicle);

        List<String> directionsToGo = trafficManager.getDirectionsToGo(roads, trafficLights);

        assertEquals(1, directionsToGo.size());
        assertEquals(NORTH, directionsToGo.getFirst());
    }

    @Test
    public void testGetDirectionsToGoLeftTurnGreen() {
        trafficLights.get(NORTH).setColor(GREEN);
        Vehicle vehicle = new Vehicle("car1", NORTH, EAST);
        roads.get(NORTH).add(vehicle);

        List<String> directionsToGo = trafficManager.getDirectionsToGo(roads, trafficLights);

        assertEquals(1, directionsToGo.size());
        assertEquals(NORTH, directionsToGo.getFirst());
    }

    @Test
    public void testGetDirectionsToGoLeftOnYellow() {
        trafficLights.get(EAST).setColor(YELLOW);
        Vehicle vehicle = new Vehicle("car1", EAST, SOUTH);
        roads.get(EAST).add(vehicle);

        List<String> directionsToGo = trafficManager.getDirectionsToGo(roads, trafficLights);

        assertEquals(1, directionsToGo.size());
        assertEquals(EAST, directionsToGo.getFirst());
    }

    @Test
    public void testGetDirectionsToGoBothLeftTurn() {
        trafficLights.get(NORTH).setColor(GREEN);
        trafficLights.get(SOUTH).setColor(GREEN);

        Vehicle northVehicle = new Vehicle("car1", NORTH, EAST);
        Vehicle southVehicle = new Vehicle("car2", SOUTH, WEST);

        roads.get(NORTH).add(northVehicle);
        roads.get(SOUTH).add(southVehicle);

        List<String> directionsToGo = trafficManager.getDirectionsToGo(roads, trafficLights);

        assertEquals(2, directionsToGo.size());
        assertTrue(directionsToGo.contains(NORTH));
        assertTrue(directionsToGo.contains(SOUTH));
    }

    @Test
    public void testGetDirectionsToGoBothLeftTurn2() {
        trafficLights.get(EAST).setColor(GREEN);
        trafficLights.get(WEST).setColor(GREEN);

        Vehicle northVehicle = new Vehicle("car1", EAST, SOUTH);
        Vehicle southVehicle = new Vehicle("car2", WEST, NORTH);

        roads.get(EAST).add(northVehicle);
        roads.get(WEST).add(southVehicle);

        List<String> directionsToGo = trafficManager.getDirectionsToGo(roads, trafficLights);

        assertEquals(2, directionsToGo.size());
        assertTrue(directionsToGo.contains(EAST));
        assertTrue(directionsToGo.contains(WEST));
    }

    @Test
    public void testGetDirectionsToGoRightTurnRed() {
        trafficLights.get(NORTH).setColor(RED);
        Vehicle vehicle = new Vehicle("car1", NORTH, WEST);
        Vehicle vehicle2 = new Vehicle("car2", EAST, NORTH);
        roads.get(NORTH).add(vehicle);
        roads.get(EAST).add(vehicle2);

        List<String> directionsToGo = trafficManager.getDirectionsToGo(roads, trafficLights);

        assertEquals(2, directionsToGo.size());
    }

    @Test
    public void testGetDirectionsToGoRightTurnRedWithConflict() {
        trafficLights.get(NORTH).setColor(RED);
        trafficLights.get(EAST).setColor(GREEN);

        Vehicle northVehicle = new Vehicle("car1", NORTH, WEST);
        Vehicle eastVehicle = new Vehicle("car2", EAST, WEST);

        roads.get(NORTH).add(northVehicle);
        roads.get(EAST).add(eastVehicle);

        List<String> directionsToGo = trafficManager.getDirectionsToGo(roads, trafficLights);

        assertEquals(1, directionsToGo.size());
        assertEquals(EAST, directionsToGo.getFirst());
    }

    @Test
    public void testIsTurnLeft() {
        Vehicle vehicle = new Vehicle("car1", NORTH, EAST);
        roads.get(NORTH).add(vehicle);

        boolean result = trafficManager.isTurnLeft(roads, NORTH);

        assertTrue(result);
    }

    @Test
    public void testIsTurnRight() {
        Vehicle vehicle = new Vehicle("car1", NORTH, WEST);
        roads.get(NORTH).add(vehicle);

        boolean result = trafficManager.isTurnRight(roads, NORTH);

        assertTrue(result);
    }
}
