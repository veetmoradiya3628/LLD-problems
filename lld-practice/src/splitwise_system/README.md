### SplitWise system design

#### 1. Requirement Gathering

- Functional Requirement
  - The system should allow users to create accounts and manage their profile information
  - Users should be able to create group and add other users to the groups
  - Users should be able to add expenses within a group, specifying the amount, description, and participants.
  - The system should automatically split the expenses among the participants based on their share.
  - Users should be able to view their individual balances with other users and settle up the balances.
  - The system should support different split methods, such as equal split, percentage split, and exact amounts.
  - Users should be able to view their transaction history and group expenses.
- Non-Functional Requirement
  - The system should handle concurrent transactions and ensure data consistency.

#### 2. Core Identity
- SplitWise Service
  - singleton class to manage multiple users, groups, expenses
- User
  - class to represent user with field as id, name, email, balances
- Group
  - class to represent group with id, name, list of members, list of expenses
- Expense
  - class to represent expense with field as id, description, amount, paidBy, list of paid for, splittype
- SplitType
  - enum to represent different split type as equal, exact, percentage
- Transaction
  - class to represent transaction between to users from and to, double amount and supporting methods
- SplitStrategy
  - interface and its implemented classes to represent logic for different split methods

#### 3. Design class & relationships
- SplitType enum
  - EQUAL, EXACT, PERCENTAGE
- SplitWise service class
  - Represents facade service class for all supported end user operations
  - fields
    - static class instance
    - map of users
    - map of groups
    - map of expenses
    - balanceSheet instance
  - Supports operations to createUser, createGroup, addExpense, settle, printBalance, printBalanceForUser, deleteExpense
- Expense class
  - fields
    - expenseId
    - double amount
    - description
    - group
    - paidBy
    - list of splits
    - createdAt
  - methods
    - getter, setter
- Group class
  - fields
    - groupId
    - name
    - members list of users
    - createdAt
- Split class
  - User user
  - double amount
- User class
  - userId
  - name
  - email
- BalanceSheet class
  - map<user, map<user, double>> balances
  - update and settle methods
- SplitStrategy interface
  - calculateSplit method
- EqualSplitStrategy implements SplitStrategy and overrides calculateSplit
- PercentageSplitStrategy implements SplitStrategy and overrides calculateSplit
- ExactSplitStrategy implements SplitStrategy and overrides calculateSplit

#### 4. Code Impl, Run & Test

#### 5. Concurrency & Thread Safety
- Concurrent data structures and synchronized methods declarations used to handle concurrent behaviour

#### 6. Extensions
- Add expense categories: Categorize expenses (food, travel, etc.)
- Add recurring expenses: Support for regular payments
- Add expense comments: Allow users to add notes to expenses
- Add expense attachments: Support for receipts and documents
- Add payment integration: Integrate with payment gateways
- Add notification system: Send reminders for pending payments

#### Design patterns & Principles
- Singleton - for SplitWiseSystem
- Facade for SplitWiseSystem to support end user operations abstractions
- Strategy - for different split strategies
- Builder - for Expense object creation

#### Open issues
