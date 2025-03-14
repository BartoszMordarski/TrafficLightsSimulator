package com.example.trafficlights.service;

import com.example.trafficlights.logger.LogCounter;
import com.example.trafficlights.model.Vehicle;
import com.example.trafficlights.model.Command;
import com.example.trafficlights.model.SimulationResponse;
import com.example.trafficlights.model.StepStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;

public class SimulationService {
    private static final Logger logger = LoggerFactory.getLogger(SimulationService.class);

    private Intersection intersection;
    private List<StepStatus> stepStatuses = new ArrayList<>();

    public SimulationService() { this.intersection = new Intersection(); }

    public void processCommand(Command command) {
        logger.info("[{}] Processing command: {}", LogCounter.getNextNumber(), command.getType());

        switch (command.getType()) {
            case "addVehicle":
                Vehicle vehicle = new Vehicle(
                        command.getVehicleId(),
                        command.getStartRoad(),
                        command.getEndRoad()
                );
                logger.info("[{}] Adding vehicle: ID={}, from {} to {}",
                        LogCounter.getNextNumber(), vehicle.getId(), vehicle.getStartRoad(), vehicle.getEndRoad());
                intersection.addVehicle(command.getStartRoad(), vehicle);
                break;

            case "step":
                List<String> leftVehicles = intersection.step();
                if (!leftVehicles.isEmpty()) {
                    logger.info("[{}] Vehicles left the intersection: {}", LogCounter.getNextNumber(), leftVehicles);
                } else {
                    logger.info("[{}] No vehicles left the intersection", LogCounter.getNextNumber());
                }
                StepStatus status = new StepStatus();
                status.getLeftVehicles().addAll(leftVehicles);
                stepStatuses.add(status);
                break;

        }
    }

    public SimulationResponse generateResponse() {
        logger.info("[{}] Generating simulation response with {} step statuses",
                LogCounter.getNextNumber(), stepStatuses.size());
        SimulationResponse response = new SimulationResponse();
        response.setStepStatuses(stepStatuses);
        return response;
    }
}