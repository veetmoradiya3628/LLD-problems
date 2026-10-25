### Library Management System

#### 1. Requirement Gathering

- Functional Requirement
    - The library management system should allow librarians to manage books, members, and borrowing activities.
    - The system should support adding, updating, and removing books from the library catalog.
    - Each book should have details such as title, author, ISBN, publication year, and availability status.
    - The system should allow members to borrow and return books.
    - Each member should have details such as name, member ID, contact information, and borrowing history.
    - The system should enforce borrowing rules, such as a maximum number of books that can be borrowed at a time and loan duration.
- Non-Functional Requirement
    - The system should handle concurrent access to the library catalog and member records.
    - The system should be extensible to accommodate future enhancements and new features.
   
#### 2. Core Identity
- Book - class
    - represents book in library catalog, with properties such as ISBN, title, author, publication year, and availability status
- Member - class
    - represents library member, with properties like memberID, name, contact information, and list of borrowed books
- LibraryManger - class
    - core of the library management system and follows the Singleton pattern to ensure a single instance of the library manager. (Facade class)
- including sync.Mutex with all the struct to ensure locking with all concurrenct / critical section area

#### 3. Design class & relationships (Code Impl, Run & Test)

#### 4. Design patterns & Principles

#### 5. Concurrency & Thread Safety

#### 6. Algorithmic Complexities (Core Flows)

#### 7. Open issues & Future Extensions
