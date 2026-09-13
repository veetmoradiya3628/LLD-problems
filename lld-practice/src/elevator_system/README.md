### Elevator System

#### 1. Requirement Gathering

- Functional Requirement
  - The elevator system should consist of multiple elevators serving multiple floors.
  - Each elevator should have a capacity limit and should not exceed it.
  - Users should be able to request an elevator from any floor and select a destination floor.
  - The elevator system should efficiently handle user requests and optimize the movement of elevators to minimize waiting time.
  - The system should prioritize requests based on the direction of travel and the proximity of the elevators to the requested floor.
  - The elevators should be able to handle multiple requests concurrently and process them in an optimal order.
- Non-Functional Requirement
  - The system should ensure thread safety and prevent race conditions when multiple threads interact with the elevators.

#### 2. Core Identity
- Direction - ENUM
  - represents a possible elevator directions as an IDLE, UP, DOWN etc

- RequestSource - ENUM
  - represents a possible request sources as an internal (from elevator), external (from floor)

- Request - class
  - represents a fields and methods for request consisting data for direction, source (Request Source), targetFloor details to go to.

- ElevatorSystem - class
  - Main orchestrator / facade class for the system - singleton object class

- Elevator - class
  - Represents elevator data and methods as id, currentFloor, observers, state, isRunning, etc

- ElevatorObserver - interface
  - represents contract for elevator display - update methods declaration

- Display - class implements ElevatorObserver
  - actual implementation for Elevator display as an observer for the elevator

- ElevatorSelectionStrategy - interface
  - defines contract to implement elevator selection strategy for request 

- NearestElevatorStrategy - class implements ElevatorSelectionStrategy
  - implementation for nearest elevator strategy for given request 

- ElevatorState - interface
  - defines set of methods to represent elevator state

- IdleState - class implements ElevatorState
  - implementation in idle state

- MovingDownState - class implements ElevatorState
  - implementation in moving down state 
  
- MovingUpState - class implements ElevatorState
  - implementation in moving up state

#### 3. Design class & relationships

#### 4. Code Impl, Run & Test

#### 5. Concurrency & Thread Safety

#### 6. Extensions

#### Design patterns & Principles

#### Open issues
