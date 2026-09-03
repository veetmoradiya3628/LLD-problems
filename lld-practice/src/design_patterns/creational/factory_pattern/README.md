### Factory Method Pattern

The Factory Method Design Pattern is a creational pattern that provides an interface for creating objects in a superclass, but allows subclasses to alter the type of objects that will be created.

Useful
- The exact type of object to be created isn't known until runtime.
- Object creation logic is complex, repetitive, or needs encapsulation.
- You want to follow the Open/Closed Principle, open for extension, closed for modification.

Components
- Product - interface / abstract class
- ConcreteProduct - class implements interface product
- Creator - interface / abstract class
- ConcreteCreator - class implements interface creator
