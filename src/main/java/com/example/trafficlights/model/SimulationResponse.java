package com.example.trafficlights.model;

import java.util.List;

public class SimulationResponse {
    private List<StepStatus> stepStatuses;

    public void setStepStatuses(List<StepStatus> stepStatuses) {
        this.stepStatuses = stepStatuses;
    }

    public List<StepStatus> getStepStatuses() {
        return stepStatuses;
    }
}