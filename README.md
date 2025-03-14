# Traffic Lights Simulation - AVSystems internship
## Author: Bartosz Mordarski
## Overview

This project was created as part of a recruitment assignment for an internship at AVSystems. The application simulates traffic flow through a four-way intersection with traffic lights. The system manages vehicles approaching from four directions (north, south, east, west), controls traffic signals, and determines when vehicles can safely move through the intersection based on traffic light states and turning rules.

## Technologies Used

- **Java 17** - Core programming language
- **Spring Boot** - Framework for creating standalone Spring-based applications
- **RESTful API** - For handling simulation commands
- **SLF4J with Logback** - For comprehensive logging of the simulation
- **Maven** - Dependency management and build tool
- **JUnit** - testing framework for unit tests

## Application Architecture

The application follows a standard Spring Boot architecture with:

- **Controllers** - Handle HTTP requests and responses
- **Models** - Define data structures
- **Services** - Implement business logic

## How to Run the Application

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Steps to Run

1. Clone the repository
   ```bash
   git clone https://github.com/BartoszMordarski/TrafficLightsSimulator.git
   cd TrafficLightsSimulator
   ```

2. Build the application
   ```bash
   mvn clean install
   ```

3. Run the application
   ```bash
   mvn spring-boot:run
   ```

4. The application will start on port 8080 by default
   - API endpoint: `http://localhost:8080/api/traffic/simulate`

### Sample Request

You can interact with the API using Postman or any HTTP client by sending a POST request to the endpoint above. Here's a sample request:

```json
{
  "commands": [
    {
      "type": "addVehicle",
      "vehicleId": "vehicle1",
      "startRoad": "south",
      "endRoad": "north"
    },
    {
      "type": "addVehicle",
      "vehicleId": "vehicle2",
      "startRoad": "north",
      "endRoad": "south"
    },
    {
      "type": "step"
    },
    {
      "type": "step"
    },
    {
      "type": "addVehicle",
      "vehicleId": "vehicle3",
      "startRoad": "west",
      "endRoad": "south"
    },
    {
      "type": "addVehicle",
      "vehicleId": "vehicle4",
      "startRoad": "west",
      "endRoad": "south"
    },
    {
      "type": "step"
    },
    {
      "type": "step"
    }
  ]
}
```

### Sample Output

For the request above, the app will generate the following output:

```json
{
    "stepStatuses": [
        {
            "leftVehicles": [
                "vehicle1",
                "vehicle2"
            ]
        },
        {
            "leftVehicles": []
        },
        {
            "leftVehicles": [
                "vehicle3"
            ]
        },
        {
            "leftVehicles": [
                "vehicle4"
            ]
        }
    ]
}
```

### Logs for this example

For the request above, the app will generate the following logs:

```bash
Processing command: addVehicle
Adding vehicle: ID=vehicle1, from south to north
Set NORTH-SOUTH to GREEN, EAST-WEST to RED
Processing command: addVehicle
Adding vehicle: ID=vehicle2, from north to south
Processing command: step
Vehicles left the intersection: [vehicle1, vehicle2]
Processing command: step
No vehicles left the intersection
Processing command: addVehicle
Adding vehicle: ID=vehicle3, from west to south
Set EAST-WEST to GREEN, NORTH-SOUTH to RED
Processing command: addVehicle
Adding vehicle: ID=vehicle4, from west to south
Processing command: step
Vehicles left the intersection: [vehicle3]
Processing command: step
Vehicles left the intersection: [vehicle4]
Generating simulation response with 4 step statuses
```

## Algorithm Explanation

### 1. Traffic Flow Management

The traffic simulation operates with a four-way intersection (north, south, east, west). Each direction has its own traffic light and queue of vehicles.

#### Key Classes and Components:

- **Intersection**: Central class managing the state of the intersection
- **TrafficLightManager**: Controls the traffic light cycle
- **TrafficManager**: Determines which vehicles can move in each step
- **SimulationService**: Processes simulation commands and generates responses
- **TrafficController**: Processes input json and sends commands for execution to SimulationService

### 2. Traffic Light Cycle

Traffic lights operate on a 6-step cycle with the following pattern:

```java
public void updateTrafficLightsCycle(Map<String, TrafficLight> trafficLights) {
    currentCycleStep++;
    if(currentCycleStep > MAX_CYCLE_STEP) {
        currentCycleStep = 1;
    }

    if(currentCycleStep == 1 || currentCycleStep == MAX_CYCLE_STEP) {
        for (String direction : DIRECTIONS) {
            if(GREEN.equals(trafficLights.get(direction).getColor()) && currentCycleStep == MAX_CYCLE_STEP) {
                trafficLights.get(direction).setColor(YELLOW);
            }
            else if(YELLOW.equals(trafficLights.get(direction).getColor()) && currentCycleStep == 1) {
                trafficLights.get(direction).setColor(RED);
            }
            else if(RED.equals(trafficLights.get(direction).getColor()) && currentCycleStep == 1) {
                trafficLights.get(direction).setColor(GREEN);
            }
        }
    }
}
```

Traffic lights follow the standard Green → Yellow → Red pattern. Opposite directions (North-South and East-West) share the same light state, ensuring efficient traffic flow.

### 3. Vehicle Movement Rules

The decision about which vehicles can move through the intersection is based on a set of rules:

#### a. Green Light:
- Vehicles going straight or turning right can always proceed
- Vehicles turning left can only proceed if:
  - There's no opposing traffic, or
  - The opposing traffic is also turning left

#### b. Yellow Light:
- Only vehicles turning left can proceed. Assumption is that a car trying to turn left is waiting in the middle of the intersection just like in real life scenario. That is why it passes during the yellow light, when opposite cars are stopping.

#### c. Red Light:
- Only vehicles turning right can proceed, and only if there's no vehicle to the left going straight

This logic is implemented in the `TrafficManager` class:

```java
public List<String> getDirectionsToGo(Map<String, Queue<Vehicle>> roads, Map<String, TrafficLight> trafficLights) {
    List<String> directionsToGo = new ArrayList<>();
    
    for (String direction : roads.keySet()) {
        if (roads.get(direction).isEmpty()) continue;
        
        // GREEN light rules
        if (!roads.get(direction).isEmpty() && trafficLights.get(direction).getColor().equals(GREEN)) {
            if (!isTurnLeft(roads, direction)) {
                directionsToGo.add(direction);
            } else {
                String oppositeDirection = getOppositeDirection(direction);
                if (roads.get(oppositeDirection).isEmpty() || 
                        (!roads.get(oppositeDirection).isEmpty() &&
                        isTurnLeft(roads, oppositeDirection) &&
                        trafficLights.get(oppositeDirection).getColor().equals(GREEN))) {
                    directionsToGo.add(direction);
                }
            }
        }

        // YELLOW light rules
        if (!roads.get(direction).isEmpty() && trafficLights.get(direction).getColor().equals(YELLOW)) {
            if (isTurnLeft(roads, direction)) {
                directionsToGo.add(direction);
            }
        }

        // RED light rules
        if (!roads.get(direction).isEmpty() && trafficLights.get(direction).getColor().equals(RED)) {
            if (isTurnRight(roads, direction) && !isCarToTheLeftGoingStraight(direction, roads)) {
                directionsToGo.add(direction);
            }
        }
    }
    return directionsToGo;
}
```

### 4. Simulation Steps

The simulation proceeds in steps with two primary command types:

1. **addVehicle**: Adds a vehicle to a specific road with a destination
2. **step**: Advances the simulation by one time unit

In each step:
- The system determines which vehicles can move based on traffic rules
- Vehicles that can move exit the intersection
- The traffic light cycle is updated

```java
public List<String> step() {
    List<String> leftVehicles = new ArrayList<>();
    
    List<String> directionsToGo = trafficManager.getDirectionsToGo(roads, trafficLights);
    
    for (String direction : directionsToGo) {
        Queue<Vehicle> queue = roads.get(direction);
        if (!queue.isEmpty()) {
            Vehicle vehicle = queue.poll();
            leftVehicles.add(vehicle.getId());
        }
    }
    
    trafficLightManager.updateTrafficLightsCycle(trafficLights);
    
    return leftVehicles;
}
```

## Logging System

The application features comprehensive logging to help track the flow of events. Each log entry includes:

1. Descriptive message about the event
2. Relevant data such as vehicle IDs, directions, traffic light states

## Testing
The application includes a comprehensive test suite to ensure the proper functioning of the traffic light simulation system. These tests verify the behavior of different components and their interactions. It includes unit tests for each servie and integration tests for the controller. 100% of classes and methods is covered by tests.

The tests cover:

1. Traffic light state management
2. Vehicle movement rules
3. Intersection management
4. API validation
5. End-to-end simulation scenarios

## Project Structure

```
src/main/java/com/example/trafficlights/
├── TrafficLightsApplication.java
├── controller/
│   └── TrafficController.java
├── model/
│   ├── Command.java
│   ├── CommandRequest.java
│   ├── SimulationResponse.java
│   ├── StepStatus.java
│   ├── TrafficLight.java
│   ├── Vehicle.java
│   └── enums/
│       ├── Colors.java
│       └── Roads.java
├── service/
│   ├── Intersection.java
│   ├── SimulationService.java
│   ├── TrafficLightManager.java
│   └── TrafficManager.java
├── validator/
    └── RequestValidator.java
```

## Extension Points

The simulation is developed in a way that it can be further extended to include more features like:

1. More complex traffic patterns
2. Traffic density analysis
3. Different vehicle types with varying behaviors
4. Pedestrian crossings
