package com.example.trafficlights.validator;

import com.example.trafficlights.model.Command;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestValidator {
    private static final Set<String> VALID_COMMAND_TYPES = new HashSet<>(Arrays.asList("addVehicle", "step"));
    private static final Set<String> VALID_ROADS = new HashSet<>(Arrays.asList("north", "south", "east", "west"));

    public String validate(List<Command> commands) {

        if(commands.isEmpty()) {
            return "Empty body request";
        }

        for(Command cmd : commands) {
            if(cmd.getType() == null || !VALID_COMMAND_TYPES.contains(cmd.getType())) {
                return "Invalid command type";
            }

            if("addVehicle".equals(cmd.getType())) {
                if(!VALID_ROADS.contains(cmd.getStartRoad())) {
                    return "Invalid start road";
                }
                if(!VALID_ROADS.contains(cmd.getEndRoad())) {
                    return "Invalid end road";
                }
                if(cmd.getStartRoad().equals(cmd.getEndRoad())) {
                    return "Start and end roads cannot be the same";
                }
            }
        }
        return null;
    }
}
