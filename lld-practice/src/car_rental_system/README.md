### Car Rental System

#### 1. Requirement Gathering

- Functional Requirement
  - The car rental system should allow customers to browse and reserve available cars for specific dates.
  - Each car should have details such as make, model, year, license plate number, and rental price per day.
  - Customers should be able to search for cars based on various criteria, such as car type, price range, and availability.
  - The system should handle reservations, including creating, modifying, and canceling reservations.
  - The system should keep track of the availability of cars and update their status accordingly.
  - The system should handle customer information, including name, contact details, and driver's license information.
  - The system should handle payment processing for reservations.
- Non-Functional Requirement
  - The system should be able to handle concurrent reservations and ensure data consistency.

#### 2. Core Identity
- Car - class
- Customer - class
- Reservation - class
- PaymentProcessor - interface
  - CreditCardPaymentProcessor
  - PayPalPaymentProcessor
- CarRentalSystem - facade singleton class
- CarType - ENUM
- CarStatus - ENUM

#### 3. Concurrency & Thread Safety
- Concurrency bottlenecks 
  - synchronized blocks entire method is not a good approach instead move to Lock at resource level or move to ReentrantLock and conditions
- Algorithms efficiency in query
  - `isCarAvailable` method is O(N) which will not scale in huge number of reservations, think of data structures like Interval Tree or something
- DI Violation
  - `PaymentProcess` is being hardcoded in `CarRentalSystem` class, it can be passed as method parameter or FactoryMethod can be used to get at runtime
- State machine transition
  - Utilize ENUMS for proper state management instead of booleans

#### 6. Extensions
- from in-memory to database transition, how it will be ?
- distributed locking with redis or something in multi-node environment

#### Design patterns & Principles
- Singleton for `CarRentalSystem`
- Strategy for `PaymentProcessor`

#### Open issues
