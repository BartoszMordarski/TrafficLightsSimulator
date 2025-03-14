package com.example.trafficlights.service;

import com.example.trafficlights.model.Vehicle;
import com.example.trafficlights.model.Command;
import com.example.trafficlights.model.SimulationResponse;
import com.example.trafficlights.model.StepStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@Scope("request")
public class SimulationService {
    private static final Logger logger = LoggerFactory.getLogger(SimulationService.class);

    private final Intersection intersection;
    private List<StepStatus> stepStatuses;

    public SimulationService(Intersection intersection) {
        this.intersection = intersection;
        stepStatuses = new ArrayList<>();
    }

    public void processCommand(Command command) {
        logger.info("Processing command: {}", command.getType());

        switch (command.getType()) {
            case "addVehicle":
                Vehicle vehicle = new Vehicle(
                        command.getVehicleId(),
                        command.getStartRoad(),
                        command.getEndRoad()
                );
                logger.info("Adding vehicle: ID={}, from {} to {}", vehicle.getId(), vehicle.getStartRoad(), vehicle.getEndRoad());
                intersection.addVehicle(command.getStartRoad(), vehicle);
                break;

            case "step":
                List<String> leftVehicles = intersection.step();
                if (!leftVehicles.isEmpty()) {
                    logger.info("Vehicles left the intersection: {}", leftVehicles);
                } else {
                    logger.info("No vehicles left the intersection");
                }
                StepStatus status = new StepStatus();
                status.getLeftVehicles().addAll(leftVehicles);
                stepStatuses.add(status);
                break;

        }
    }

    public SimulationResponse generateResponse() {
        logger.info("Generating simulation response with {} step statuses", stepStatuses.size());
        SimulationResponse response = new SimulationResponse();
        response.setStepStatuses(stepStatuses);
        return response;
    }
}