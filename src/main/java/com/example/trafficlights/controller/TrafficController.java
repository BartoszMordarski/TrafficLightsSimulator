package com.example.trafficlights.controller;


import com.example.trafficlights.model.Command;
import com.example.trafficlights.model.CommandRequest;
import com.example.trafficlights.service.SimulationService;
import com.example.trafficlights.model.SimulationResponse;
import com.example.trafficlights.validator.RequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traffic")
public class TrafficController {

    @PostMapping("/simulate")
    public ResponseEntity<?> processCommands(@RequestBody CommandRequest request) {
        List<Command> commands = request.getCommands();
        RequestValidator validator = new RequestValidator();
        String validationError = validator.validate(commands);
        if(validationError != null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(validationError);
        }

        SimulationService simulationEntry = new SimulationService();

        for (Command command : commands) {
            simulationEntry.processCommand(command);
        }

        return ResponseEntity.ok(simulationEntry.generateResponse());
    }
}

