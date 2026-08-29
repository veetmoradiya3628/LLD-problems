### ATM System
Design and implement an ATM (Automated Teller Machine) system that allows users to perform basic banking operations such as balance inquiry, cash withdrawal, and cash deposit, with secure authentication and proper cash management.

#### 1. Requirement Gathering

- Functional Requirement
  - User Authentication - User must authenticate using a card and PIN
  - Balance Inquiry - Users can check their account balance
  - Cash Withdrawal - Users can withdraw cash if sufficient balance and cash available
  - Cash Deposit - Users can deposit cash into their account.
  - Transaction Management - The system records and processes transactions (withdrawal, deposit).
  - Banking Service Integration - The ATM interacts with a backend banking service to validate accounts and perform transactions.
  - Cash Dispenser - The ATM manages its own cash inventory and dispenses cash securely.
- Non-Functional Requirement
  - Concurrency & Consistency - The system handles concurrent access and ensures data consistency.
  - User Interface: The ATM provides a user-friendly interface for operations. 
  - Extensibility: Easy to add new features such as mini-statements, fund transfers, or multi-currency support.

#### 2. Core Identity
- ATMSystem - class
  - Main class for ATM operations; interacts with BankingService and CashDispenser.
- Card - class 
  - Represents an ATM card with card number and PIN.
- Account - class
  - Represents a bank account with account number and balance; supports debit and credit operations.
- Transaction (abstract) 
  - Base class for transactions; extended by WithdrawalTransaction and DepositTransaction.
- WithdrawalTransaction / DepositTransaction
  - Concrete transaction types for withdrawal and deposit.
- BankingService
  - Manages bank accounts and processes transactions; uses thread-safe data structures.
- CashDispenser
  - Manages the ATM's cash inventory and handles dispensing; ensures thread safety.
- ATMDemo class
  - Demonstrates the usage of the ATM system with sample accounts and operations.

#### 3. Design class & relationships
- UML - TODO

#### 4. Code Impl, Run & Test

1. ATMSystem - class
- Fields
    - transactionCounter: AtomicLong - to keep track of no. of transactions
    - currentState: ATMState
    - currentCard: Card
    - cashDispenser: CashDispenser
    - instance: ATMSystem
    - bankService: BankService
- Methods
    - selectOperation(OperationType Type, int[]): void
    - getCard(string): Card
    - enterPin(String): void
    - depositCash(int): void
    - changeState(ATMState): void
    - getCurrentCard(): void
    - withdrawCash(int): void
    - getInstance(): ATMSystem
    - checkBalance(): void
    - authenticate(string): boolean
    - getBankService(): BankService
    - setCurrentCard(Card): void
    - insertCard(String): void
2. OperationType - enum
    - WITHDRAW_CASH
    - DEPOSIT_CASH
    - CHECK_BALANCE
3. ATMState - interface
- Methods
    - insertCard(ATMSystem, String): void
    - enterPin(ATMSystem, String): void
    - selectOperation(ATMSystem, OperationType, int[]): void
    - ejectCard(ATMSystem): void
4. AuthenticatedState - Implements ATMState
5. HasCardState - Implements ATMState
6. IdleState - Implements ATMState
7. BankService - class
   Fields:
    - cards: Map<String, Card>
    - cardAccountMap: Map<Card, Account>
    - accounts: Map<String, Account>
      Methods:
    - linkCardToAccount(Card, Account): void
    - getBalance(Card): double
    - depositMoney(Card, double): void
    - createAccount(String, double): Account
    - createCard(String, String): Card
    - getCard(String): Card
    - withdrawMoney(Card, double): void
    - authenticate(Card, String): boolean
8. Account - class
   Fields
    - cards: Map<String, Card>
    - balance: double
    - accountNumber: String
      Methods
    - withdraw(double): boolean
    - deposit(double): void
9. Card
   Fields
    - cardNumber: string
    - pin: string
10. CashDispenser - class
- chain: DispenseChain
- canDispenseCash(int): boolean
- dispenseCash(int): void
11. DispenseChain - interface
- dispense(int): void
- setNextChain(DispenseChain): void
- canDispense(int): boolean
12. NoteDispenser - class implements DispenseChain
- noteValue: int
- nextChain: DispenseChain
- numNotes: int
- // same methods as DispenseChain but override
13. NoteDispenser100 - extends NoteDispenser
14. NoteDispenser20 - extends NoteDispenser
15. NoteDispenser50 - extends NoteDispenser 

#### 5. Concurrency & Thread Safety

#### 6. Extensions

#### Design patterns & Principles

#### Open issues
