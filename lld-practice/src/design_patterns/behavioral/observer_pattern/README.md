## Observer pattern

The Observer Design Pattern is a behavioral pattern that defines a one-to-many dependency between objects so that when one object (the subject) changes its state, all its dependents (observers) are automatically notified and updated.

Use cases :
- You have multiple parts of the system that need to react to a change in one central component.
- You want to decouple the publisher of data from the subscribers who react to it.
- You need a dynamic, event-driven communication model without hardcoding who is listening to whom.

Components
1. Subject - interface
2. ConcreteSubject - Class implements Subject
3. Observer - interface
4. ConcreteObserverA - Class implements Observer
5. ConcreteObserverB - Class implements Observer

