package com.example.trafficlights.service;

import com.example.trafficlights.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.example.trafficlights.model.enums.Roads.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class IntersectionTest {
    private Intersection intersection;

    @BeforeEach
    public void setUp() {
        intersection = new Intersection(new TrafficLightManager(), new TrafficManager());
    }

    @Test
    public void testAddVehicle() {
        Vehicle vehicle = new Vehicle("car1", NORTH, SOUTH);

        intersection.addVehicle(NORTH, vehicle);
        List<String> leftVehicles = intersection.step();

        assertEquals(1, leftVehicles.size());
        assertEquals("car1", leftVehicles.getFirst());
    }

    @Test
    public void testStepWithMultipleVehicles() {
        Vehicle vehicle1 = new Vehicle("car1", NORTH, SOUTH);
        Vehicle vehicle2 = new Vehicle("car2", SOUTH, NORTH);
        Vehicle vehicle3 = new Vehicle("car3", EAST, WEST);

        intersection.addVehicle(NORTH, vehicle1);
        intersection.addVehicle(SOUTH, vehicle2);
        intersection.addVehicle(EAST, vehicle3);

        List<String> leftVehicles = intersection.step();

        assertEquals(2, leftVehicles.size());
        assertTrue(leftVehicles.contains("car1"));
        assertTrue(leftVehicles.contains("car2"));
        assertFalse(leftVehicles.contains("car3"));
    }
}