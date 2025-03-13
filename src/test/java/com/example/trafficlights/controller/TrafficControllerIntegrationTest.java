package com.example.trafficlights.controller;

import com.example.trafficlights.model.Command;
import com.example.trafficlights.model.CommandRequest;
import com.example.trafficlights.model.SimulationResponse;
import com.example.trafficlights.model.StepStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.example.trafficlights.model.enums.Roads.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TrafficControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSimulateEndpoint() {
        List<Command> commands = new ArrayList<>();

        Command addNorthSouth = createAddVehicleCommand("car1", NORTH, SOUTH);
        Command addSouthNorth = createAddVehicleCommand("car2", SOUTH, NORTH);
        Command addEastWest = createAddVehicleCommand("car3", EAST, WEST);
        Command addWestEast = createAddVehicleCommand("car4", WEST, EAST);

        commands.add(addNorthSouth);
        commands.add(addSouthNorth);
        commands.add(addEastWest);
        commands.add(addWestEast);

        Command step1 = new Command();
        step1.setType("step");
        commands.add(step1);

        CommandRequest request = new CommandRequest() {
            @Override
            public List<Command> getCommands() {
                return commands;
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CommandRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<SimulationResponse> response = restTemplate.postForEntity(
                "/api/traffic/simulate",
                entity,
                SimulationResponse.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        SimulationResponse simulationResponse = response.getBody();
        assertNotNull(simulationResponse);

        List<StepStatus> stepStatuses = simulationResponse.getStepStatuses();
        assertNotNull(stepStatuses);
        assertEquals(1, stepStatuses.size());

        StepStatus firstStep = stepStatuses.getFirst();
        List<String> leftVehicles = firstStep.getLeftVehicles();

        assertEquals(2, leftVehicles.size());
        assertTrue(leftVehicles.contains("car1"));
        assertTrue(leftVehicles.contains("car2"));
    }

    @Test
    public void testComplexTrafficScenario() {
        List<Command> commands = new ArrayList<>();

        commands.add(createAddVehicleCommand("car1", NORTH, EAST));
        commands.add(createAddVehicleCommand("car2", SOUTH, WEST));
        commands.add(createAddVehicleCommand("car3", EAST, NORTH));
        commands.add(createAddVehicleCommand("car4", WEST, SOUTH));
        commands.add(createAddVehicleCommand("car5", NORTH, WEST));
        commands.add(createAddVehicleCommand("car6", SOUTH, EAST));

        for (int i = 0; i < 7; i++) {
            Command step = new Command();
            step.setType("step");
            commands.add(step);
        }

        CommandRequest request = new CommandRequest() {
            @Override
            public List<Command> getCommands() {
                return commands;
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CommandRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<SimulationResponse> response = restTemplate.postForEntity(
                "/api/traffic/simulate",
                entity,
                SimulationResponse.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        SimulationResponse simulationResponse = response.getBody();
        assertNotNull(simulationResponse);

        List<StepStatus> stepStatuses = simulationResponse.getStepStatuses();
        assertNotNull(stepStatuses);
        assertEquals(7, stepStatuses.size());

        List<String> allLeftVehicles = new ArrayList<>();
        for (StepStatus status : stepStatuses) {
            allLeftVehicles.addAll(status.getLeftVehicles());
        }

        assertEquals(6, allLeftVehicles.size());
        assertTrue(allLeftVehicles.contains("car1"));
        assertTrue(allLeftVehicles.contains("car2"));
        assertTrue(allLeftVehicles.contains("car3"));
        assertTrue(allLeftVehicles.contains("car4"));
        assertTrue(allLeftVehicles.contains("car5"));
        assertTrue(allLeftVehicles.contains("car6"));
    }

    private Command createAddVehicleCommand(String vehicleId, String startRoad, String endRoad) {
        Command command = new Command();
        command.setType("addVehicle");
        command.setVehicleId(vehicleId);
        command.setStartRoad(startRoad);
        command.setEndRoad(endRoad);
        return command;
    }
}
