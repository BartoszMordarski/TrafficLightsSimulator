package com.example.trafficlights.service;

import com.example.trafficlights.model.Vehicle;
import com.example.trafficlights.model.Command;
import com.example.trafficlights.model.SimulationResponse;
import com.example.trafficlights.model.StepStatus;

import java.util.ArrayList;
import java.util.List;

public class SimulationService {

    private Intersection intersection;
    private List<StepStatus> stepStatuses = new ArrayList<>();

    public SimulationService() { this.intersection = new Intersection(); }

    public void processCommand(Command command) {
        switch (command.getType()) {
            case "addVehicle":
                Vehicle vehicle = new Vehicle(
                        command.getVehicleId(),
                        command.getStartRoad(),
                        command.getEndRoad()
                );
                intersection.addVehicle(command.getStartRoad(), vehicle);
                break;

            case "step":
                List<String> leftVehicles = intersection.step();
                StepStatus status = new StepStatus();
                status.getLeftVehicles().addAll(leftVehicles);
                stepStatuses.add(status);
                break;

        }
    }

    public SimulationResponse generateResponse() {
        SimulationResponse response = new SimulationResponse();
        response.setStepStatuses(stepStatuses);
        return response;
    }
}