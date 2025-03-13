package com.example.trafficlights.service;

import com.example.trafficlights.model.TrafficLight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.example.trafficlights.model.enums.Colors.*;
import static com.example.trafficlights.model.enums.Roads.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

public class TrafficLightManagerTest {
    private TrafficLightManager trafficLightManager;
    private Map<String, TrafficLight> trafficLights;

    @BeforeEach
    public void setUp() {
        trafficLightManager = new TrafficLightManager();
        trafficLights = new HashMap<>();
        for (String direction : DIRECTIONS) {
            trafficLights.put(direction, new TrafficLight(RED));
        }
    }

    @Test
    public void testInitTrafficLightsNorthSouth() {
        trafficLightManager.initTrafficLights(trafficLights, NORTH);

        assertEquals(GREEN, trafficLights.get(NORTH).getColor());
        assertEquals(GREEN, trafficLights.get(SOUTH).getColor());
        assertEquals(RED, trafficLights.get(EAST).getColor());
        assertEquals(RED, trafficLights.get(WEST).getColor());
    }

    @Test
    public void testInitTrafficLightsEastWest() {
        trafficLightManager.initTrafficLights(trafficLights, EAST);

        assertEquals(RED, trafficLights.get(NORTH).getColor());
        assertEquals(RED, trafficLights.get(SOUTH).getColor());
        assertEquals(GREEN, trafficLights.get(EAST).getColor());
        assertEquals(GREEN, trafficLights.get(WEST).getColor());
    }

    @Test
    public void testUpdateTrafficLightsCycle() {
        trafficLightManager.initTrafficLights(trafficLights, NORTH);

        for (int i = 0; i < 5; i++) {
            trafficLightManager.updateTrafficLightsCycle(trafficLights);
        }

        assertEquals(YELLOW, trafficLights.get(NORTH).getColor());
        assertEquals(YELLOW, trafficLights.get(SOUTH).getColor());
        assertEquals(RED, trafficLights.get(EAST).getColor());
        assertEquals(RED, trafficLights.get(WEST).getColor());

        trafficLightManager.updateTrafficLightsCycle(trafficLights);

        assertEquals(RED, trafficLights.get(NORTH).getColor());
        assertEquals(RED, trafficLights.get(SOUTH).getColor());
        assertEquals(GREEN, trafficLights.get(EAST).getColor());
        assertEquals(GREEN, trafficLights.get(WEST).getColor());
    }
}
