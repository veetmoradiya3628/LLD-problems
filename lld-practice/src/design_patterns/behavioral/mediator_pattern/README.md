### Mediator Pattern

The Mediator Design Pattern is a behavioral pattern that defines an object (the Mediator) to encapsulate how a set of objects interact.

It promotes loose coupling by preventing objects from referring to each other directly, and lets you vary their interactions independently.

Useful when :
- You have a group of tightly coupled classes or UI components that need to communicate.
- Changes in one component require updates in multiple others.
- You want to centralize communication logic to simplify maintenance and testing.

The Mediator Pattern promotes loose coupling by centralizing communication between objects. Instead of having components refer to and interact with each other directly, they communicate through a mediator.

Characteristics
- Centralized communication
- Loose coupling between components

Components
- Mediator - Interface
- ConcreteMediator
- Component - Abstract class
- ConcreteComponents

What we will achieve
- Loose coupling
- Separation of concerns
- Ease of extensions
- Reusability

