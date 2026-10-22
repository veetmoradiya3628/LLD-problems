### Airline Management System

#### 1. Requirement Gathering

- Functional Requirement
    - The airline management system should allow users to search for flights based on source, destination, and date.
    - Users should be able to book flights, select seats, and make payments.
    - The system should manage flight schedules, aircraft assignments, and crew assignments.
    - The system should handle passenger information, including personal details and baggage information.
    - The system should support different types of users, such as passengers, airline staff, and administrators.
    - The system should be able to handle cancellations, refunds, and flight changes.
- Non-Functional Requirement
    - The system should ensure data consistency and handle concurrent access to shared resources.
    - The system should be scalable and extensible to accommodate future enhancements and new features.

#### 2. Core Identity
- Flight class
    - represents a flight in the airline management system with properties such as flight number, source, destination, departure time, arrival time, and available seats
- Aircraft class
    - represents an aircraft, with properties like tail number, model and total seats
- Passanger class
    - represents a passenger, with properties such as ID, name, email, and phone number.
- Booking class
    - represents a booking made by a passenger for a specific flight and seat, with properties such as booking number, flight, passenger, seat, price, and booking status.
- Seat class
    - represents a seat on a flight, with properties like seat number, seat type, and seat status
- Payment class
    - represents a payment made for a booking, with properties such as payment ID, payment method, amount, and payment status
- FlightSearch class
    - provides functionality to search for flights based on source, destination, and date.
- BookingManager class
    - manages the creation and cancellation of bookings. It follows the Singleton pattern to ensure a single instance of the booking manager.
- PaymentProcessor class
    - handles the processing of payments. It follows the Singleton pattern to ensure a single instance of the payment processor.
- AirlineManagementSystem class
    - serves as the main entry point of the system, combining all the components and providing methods for flight management, booking, payment processing, and other operations.


#### 3. Design class & relationships (Code Impl, Run & Test)

#### 4. Design patterns & Principles

#### 5. Concurrency & Thread Safety

#### 6. Algorithmic Complexities (Core Flows)

#### 7. Open issues & Future Extensions
