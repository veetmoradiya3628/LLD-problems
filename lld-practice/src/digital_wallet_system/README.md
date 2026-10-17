### Digital Waller System

#### 1. Requirement Gathering

- Functional Requirement
    - The digital wallet should allow users to create an account and manage their personal information.
    - Users should be able to add and remove payment methods, such as credit cards or bank accounts.
    - The digital wallet should support fund transfers between users and to external accounts.
    - The system should handle transaction history and provide a statement of transactions.
    - The digital wallet should support multiple currencies and perform currency conversions.
    - The system should ensure the security of user information and transactions.
- Non-Functional Requirement
    - The digital wallet should handle concurrent transactions and ensure data consistency.
    - The system should be scalable to handle a large number of users and transactions.
   
#### 2. Core Identity
- User - class - represents properties such as ID, name, email, list of accounts
- Account - class - represents a user's account within the digital wallet, with properties like ID, user, account number, currency, balance, and a list of transactions. It provides methods to deposit and withdraw funds.
- Transaction - class - represents a financial transaction between two accounts, containing properties such as ID, source account, destination account, amount, currency, and timestamp.
- PaymentMethod - abstract class - for different payment methods, such as credit cards and bank accounts. It defines the common properties and methods for processing payments.
- CreditCard, BankAccount - class - concrete implementations of the PaymentMethod class, representing specific payment methods.
- Currency - enum - represents different currencies supported by the digital wallet.
- CurrencyConverter - class - provides a static method to convert amounts between different currencies based on predefined exchange rates.
- DigitalWallet - class - singleton class with facade methods such as create users, accounts, add payment methods, transfer funds, retrieve transaction history.

#### 3. Design class & relationships (Code Impl, Run & Test)

#### 4. Design patterns & Principles
- Singleton pattern - for wallet management
- Interface with class implementation for processing payment different methods
- SRP for individual classes
- helper classes as converter utils etc
- errors & types as enum const in different files for better management

#### 5. Concurrency & Thread Safety

#### 6. Algorithmic Complexities (Core Flows)

#### 7. Open issues & Future Extensions
