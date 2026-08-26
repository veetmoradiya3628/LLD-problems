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
1. User - class
   - fields: id string, email string, name string
   - methods: NA

2. Comment - class
   - fields: content string, author User, id string, timestamp Date
   - methods: NA

3. ActivityLog - class
   - fields: timestamp LocalDateTime, description string
   - methods: toString(): string

4. Task - class
   - fields: id string, subtasks List<Task>, currentState TaskState, activityLogs List<ActivityLog>, tags Set<Tag>, description string, observers List<TaskObserver>, createdBy User, dueDate LocalDate, assignee User, comments List<Comment>, title String, priority TaskPriority 
   - methods: addObserver(TaskObserver): void, startProgress(): void, removeObserver(TaskObserver): void, getStatus(): TaskStatus, reopenTask(): void, updatePriority(TaskPriority): void, addComment(Comment): void, addSubTask(Task): void, completeTask(): void, addLog(String): void, display(String): void, isComposite(): boolean, notifyObservers(String): void, setState(TaskState): void

5. Tag - class
   - fields: name string
   - methods: getName(): string

6. TaskList - class
   - fields: id string, tasks: List<Task>, name: string 
   - methods: display(): void, addTask(Task): void

7. TaskManagementSystem - class
   - fields: users Map<string, user>, instance: TaskManagementSystem, taskLists Map<String, TaskList>, tasks: Map<String, Task>
   - methods: listTaskByUser(String): List<Task>, createTaskList(String): TaskList, searchTask(String, TaskSortStrategy): List<Task>, createUser(String, String): User, createTask(String, String, LocalDate, TaskPriority, String): Task, deleteTask(String): void, getInstance(): TaskManagementSystem, listTaskByStatus(TaskStatus): List<Task>

8. TaskPriority - ENUM
   - MEDIUM, LOW, HIGH, CRITICAL

9. TaskStatus - ENUM
   - IN_PROGRESS, DONE, TODO

10. TaskSortStrategy - Interface
    - sort(List<Task>): void

11. SortByDueDate - class implements TaskSortStrategy
    - sort(List<Task>): void
     
12. SortByPriority - class implements TaskSortStrategy
    - sort(List<Task>): void
  
13. TaskObserver - Interface
    - update(Task, string): void

14. ActivityLogger - class implements TaskObserver
    - update(Task, string): void

15. TaskState - Interface
    - completeTask(Task): void
    - reopenTask(Task): void
    - getStatus(): TaskStatus
    - startProgress(Task): void

16. InProgressState - class implements TaskState
17. TodoState - class implements TaskState
18. DoneState - class implements TaskState

#### 4. Code Impl, Run & Test
- UML Todo

#### 5. Concurrency & Thread Safety
- Used ConcurrentHashMap & CopyOnWrite List implementation for thread safe design
- Used Thread safe version of singleton pattern using synchronized

#### 6. Extensions
- To add new status or priorities - required enums updates only required
- Plan for 
  - deadlines
  - notifications
  - task dependencies etc

#### Design patterns & Principles
- Manager pattern 
  - Task management system class acts as service / manager for all operations
- SOC - each class has separate responsibilities
- Strategy
  - for different sorting strategies based on parameters for list of tasks
- State
  - To manage task's life cycle state management 
- Observer
  - To notify on task state change

#### Open issues
- TODO...