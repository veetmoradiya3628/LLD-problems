### Parking Lot
Design and implement a Parking Lot Management System that supports parking and unparking of vehicles, parkingTicket generation, fee calculation, and management of multiple floors and spot types.

#### 1. Requirement Gathering
- Functional Requirement
  - The parking lot can have multiple floors
  - Each floor has multiple parking spots of different types (e.g. car, bike, truck)
  - Support for different vehicle types
  - Generate a parking ticket when a vehicle is parked
  - allow vehicle to unpark and calculate the parking fee
  - support for different fee strategies
  - Allocate the nearest available sport of the correct type
- Non-Functional Requirement
  - Should be easy to add
    - new vehicle type
    - spot type
    - fee strategy

#### 2. Core Identity
- ParkingLot - main class managing the entire parking lot, floors, and overall operations.
- ParkingFloor - Represents a single floor in the parking lot, manages its sports
- ParkingSpot - Represents an individual parking spot, knows its type and occupancy.
- ParkingTicket - Represents a parking parkingTicket issued when a vehicle is parked.
- Vehicle - Abstract class for Vehicle
- Bike - concrete vehicle with size 
- Car - concrete vehicle with size
- Truck - concrete vehicle with size
- VehicleSize - ENUM for vehicle size (small, medium, large)
- Fee - FeeStrategy - represents strategy hierarchy for calculating fees based on passed parking ticket
  - FlatRateFeeStrategy
  - VehicleBasedFeeStrategy
- Parking - ParkingStrategy - represents strategy hierarchy for findingSpot based on List of ParkingFloor pf, Vehicle v
  - BestFitStrategy
  - FarthestFirstStrategy
  - NearestFirstStrategy

#### 3. Design class & relationships
- TODO: UML

#### 4. Code Impl, Run & Test
- Clean, modular and extensible code impl 

#### 5. Concurrency & Thread Safety

#### 6. Extensions

#### Design patterns & Principles

#### Open issues
