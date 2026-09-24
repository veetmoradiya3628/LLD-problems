### Vending Machine Design
Design and implement a Vending Machine system that allows users to select products, insert coins/notes, dispense products, and return change. The system should manage inventory, handle payments, and use the State design pattern for its operations.

#### 1. Requirement Gathering

- Functional Requirement
  - Product management - The system manages a catalog of products, each with a price and available quantity.
  - Inventory management - The system tracks the quantity of each item and prevents dispensing if out of stock.
  - Payment Handling - The system accepts coins and notes, tracks total payment, and returns change if necessary.
  - State management - The system uses the State design pattern to manage its operational states (Idle, Ready, Dispense, ReturnChange).
  - User Interactions - Users can select products, insert coins/notes, and receive products and change.
  - Extensibility - Easy to add new item types, payment methods, or states.
- Non-Functional Requirement
  - Concurrency handling in multithreaded system

#### 2. Core Identity
- VendingMachine - class
  - Main class that manages inventory, vendingMachineState transitions, item selection, and payment
- Product/Item - class
  - Represents a item with a name and price
- Inventory - class
  - Manages the stock of products
- Coin / Note - class
  - Represents accepted denominations for payment
- VendingMachineState - interface
  - Interface for different machine states
- IdleState, ReadyState, DispenseState, ReturnChangeState - class
  - Concrete states implementing VendingMachineStates
- Coin - ENUM
  - represents different types of coins

#### 3. Design class & relationships

#### 4. Code Impl, Run & Test

#### 5. Concurrency & Thread Safety

#### 6. Extensions

#### Design patterns & Principles
- Singleton pattern - VendingMachine is implemented as singleton
- State pattern - different ATM-machine state and its operations are represented as state classes

#### Open issues
