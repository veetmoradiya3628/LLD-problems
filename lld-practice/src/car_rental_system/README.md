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

#### 3. Design class & relationships

#### 4. Code Impl, Run & Test

#### 5. Concurrency & Thread Safety

#### 6. Extensions

#### Design patterns & Principles

#### Open issues
