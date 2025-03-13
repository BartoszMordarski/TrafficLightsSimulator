package com.example.trafficlights.controller;


import com.example.trafficlights.model.Command;
import com.example.trafficlights.model.CommandRequest;
import com.example.trafficlights.service.SimulationService;
import com.example.trafficlights.model.SimulationResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traffic")
public class TrafficController {

    @PostMapping("/simulate")
    public SimulationResponse processCommands(@RequestBody CommandRequest request) {
        List<Command> commands = request.getCommands();
        SimulationService simulationEntry = new SimulationService();

        for (Command command : commands) {
            simulationEntry.processCommand(command);
        }

        return simulationEntry.generateResponse();
    }
}

