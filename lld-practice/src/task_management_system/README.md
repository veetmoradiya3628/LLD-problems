### Task Management System
Design and implement a Task Management System that allows users to create, assign, update, and track tasks. The system should support task priorities, statuses, comments, and user assignment.

#### 1. Requirement Gathering

- Functional Requirement
  - The task management system should allow users to create, update, and delete tasks.
  - Each task should have a title, description, due date, priority, and status (e.g., pending, in progress, completed).
  - Users should be able to assign tasks to other users and set reminders for tasks.
  - User should be able to comment on the task
  - The system should support searching and filtering tasks based on various criteria (e.g., priority, due date, assigned user).
  - Users should be able to mark tasks as completed and view their task history.
- Non-Functional Requirement
  - The system should handle concurrent access to tasks and ensure data consistency.
  - The system should be extensible to accommodate future enhancements and new features.

#### 2. Core Identity
- TaskManagementSystem - class
  - serves as the entry point of the application and demonstrates the usage of the task management system.
- User - class
  - represents a user in the task management system, with properties such as id, name and email.
- Comment - class
  - represents a comment on a task with properties such as content, author (User), id, timestamp
- ActivityLog - class
  - represents a Log event on a task with properties such as timestamp, description 
- Task - class
  - main class to represent task with properties such as 
    - id, 
    - subtasks (List of tasks), 
    - currentState, 
    - activityLogs (List<ActivityLogs>), 
    - tags (Tag), 
    - description, 
    - observers (List<Observer>)
    - createdBy, assignee (User)
    - List<Comments> - store the list of comments
    - title
    - priority (TaskPriority)
  - additional utility methods for working and updating task item and its metadata
- Tag - class
  - simple POJO class for tag name and its methods
- TaskPriority - ENUM
  - HIGH, LOW, MEDIUM, CRITICAL etc
- TaskStatus - ENUM
  - IN_PROGRESS, DONE, TODO etc
- TaskState - Interface
  - to represent the task state management with state design pattern
  - Implementation includes classes such as InProgressState, TodoState, DoneState
- TaskList - class
  - To represent a list of tasks as group with concept as TaskList
  - contains id, tasks (List<Task>), name etc
- TaskSortStrategy - interface
  - base class for implementing listing / sorting list of task based on different strategy, implemented using strategy pattern
  - Implementation includes SortByDueDate, SortByPriority etc
- TaskObserver - interface
  - To implement task change notification with observer pattern
  - Implementation includes having class as ActivityLogger or etc with update method implementation

#### 3. Design class & relationships

#### 4. Code Impl, Run & Test

#### 5. Concurrency & Thread Safety

#### 6. Extensions

#### Design patterns & Principles

#### Open issues
