package com.example.trafficlights.validator;

import com.example.trafficlights.model.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RequestValidatorTest {

    private RequestValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RequestValidator();
    }

    @Test
    void validateEmptyCommandList() {
        String result = validator.validate(Collections.emptyList());
        assertEquals("Empty body request", result);
    }

    @Test
    void validateValidStepCommand() {
        Command cmd = new Command();
        cmd.setType("step");

        String result = validator.validate(Collections.singletonList(cmd));
        assertNull(result);
    }

    @Test
    void validateNullCommandType() {
        Command cmd = new Command();
        cmd.setType(null);

        String result = validator.validate(Collections.singletonList(cmd));
        assertEquals("Invalid command type", result);
    }

    @Test
    void validateInvalidCommandType() {
        Command cmd = new Command();
        cmd.setType("invalidType");

        String result = validator.validate(Collections.singletonList(cmd));
        assertEquals("Invalid command type", result);
    }

    @Test
    void validateAddVehicleWithValidRoads() {
        Command cmd = new Command();
        cmd.setType("addVehicle");
        cmd.setStartRoad("north");
        cmd.setEndRoad("south");

        String result = validator.validate(Collections.singletonList(cmd));
        assertNull(result);
    }

    @Test
    void validateAddVehicleWithInvalidStartRoad() {
        Command cmd = new Command();
        cmd.setType("addVehicle");
        cmd.setStartRoad("invalid");
        cmd.setEndRoad("south");

        String result = validator.validate(Collections.singletonList(cmd));
        assertEquals("Invalid start road", result);
    }

    @Test
    void validateAddVehicleWithInvalidEndRoad() {
        Command cmd = new Command();
        cmd.setType("addVehicle");
        cmd.setStartRoad("north");
        cmd.setEndRoad("invalid");

        String result = validator.validate(Collections.singletonList(cmd));
        assertEquals("Invalid end road", result);
    }

    @Test
    void validateMultipleValidCommands() {
        Command cmd1 = new Command();
        cmd1.setType("step");

        Command cmd2 = new Command();
        cmd2.setType("addVehicle");
        cmd2.setStartRoad("east");
        cmd2.setEndRoad("west");

        String result = validator.validate(Arrays.asList(cmd1, cmd2));
        assertNull(result);
    }

    @Test
    void validateInvalidCommandInList() {
        Command cmd1 = new Command();
        cmd1.setType("step");

        Command cmd2 = new Command();
        cmd2.setType("addVehicle");
        cmd2.setStartRoad("invalid");
        cmd2.setEndRoad("west");

        String result = validator.validate(Arrays.asList(cmd1, cmd2));
        assertEquals("Invalid start road", result);
    }

    @Test
    void validateSameRoadForStartAndEnd_shouldBeValid() {
        Command cmd = new Command();
        cmd.setType("addVehicle");
        cmd.setStartRoad("north");
        cmd.setEndRoad("north");

        String result = validator.validate(Collections.singletonList(cmd));
        assertEquals("Start and end roads cannot be the same", result);
    }
}