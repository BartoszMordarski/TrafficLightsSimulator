package com.example.trafficlights.controller;


import com.example.trafficlights.model.Command;
import com.example.trafficlights.model.CommandRequest;
import com.example.trafficlights.service.SimulationService;
import com.example.trafficlights.validator.RequestValidator;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Scope("request")
@RequestMapping("/api/traffic")
public class TrafficController {

    RequestValidator validator;
    SimulationService service;

    public TrafficController(RequestValidator validator, SimulationService service) {
        this.validator = validator;
        this.service = service;
    }

    @PostMapping("/simulate")
    public ResponseEntity<?> processCommands(@RequestBody CommandRequest request) {
        List<Command> commands = request.getCommands();
        String validationError = validator.validate(commands);
        if(validationError != null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(validationError);
        }

        for (Command command : commands) {
            service.processCommand(command);
        }

        return ResponseEntity.ok(service.generateResponse());
    }
}

