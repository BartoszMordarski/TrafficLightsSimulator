package com.example.trafficlights.service;


import com.example.trafficlights.model.Command;
import com.example.trafficlights.model.SimulationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.example.trafficlights.model.enums.Roads.*;
import static org.junit.jupiter.api.Assertions.*;

public class SimulationServiceTest {
    private SimulationService simulationService;

    @BeforeEach
    public void setUp() {
        simulationService = new SimulationService(new Intersection(new TrafficLightManager(), new TrafficManager()));
    }

    @Test
    public void testProcessCommandAddVehicle() {
        Command command = new Command();
        command.setType("addVehicle");
        command.setVehicleId("car1");
        command.setStartRoad(NORTH);
        command.setEndRoad(SOUTH);

        simulationService.processCommand(command);

        SimulationResponse response = simulationService.generateResponse();

        assertNotNull(response);
        assertNotNull(response.getStepStatuses());
        assertEquals(0, response.getStepStatuses().size());
    }

    @Test
    public void testProcessCommandStep() {
        Command addCommand = new Command();
        addCommand.setType("addVehicle");
        addCommand.setVehicleId("car1");
        addCommand.setStartRoad(NORTH);
        addCommand.setEndRoad(SOUTH);
        simulationService.processCommand(addCommand);

        Command stepCommand = new Command();
        stepCommand.setType("step");

        simulationService.processCommand(stepCommand);

        SimulationResponse response = simulationService.generateResponse();
        assertNotNull(response);
        assertEquals(1, response.getStepStatuses().size());
    }

    @Test
    public void testGenerateResponse() {
        Command addCommand = new Command();
        addCommand.setType("addVehicle");
        addCommand.setVehicleId("car1");
        addCommand.setStartRoad(NORTH);
        addCommand.setEndRoad(SOUTH);
        simulationService.processCommand(addCommand);

        Command stepCommand = new Command();
        stepCommand.setType("step");
        simulationService.processCommand(stepCommand);
        simulationService.processCommand(stepCommand);

        SimulationResponse response = simulationService.generateResponse();

        assertNotNull(response);
        assertNotNull(response.getStepStatuses());
        assertEquals(2, response.getStepStatuses().size());
    }
}
