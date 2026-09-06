### Prototype Pattern

The Prototype Design Pattern is a creational design pattern that lets you create new objects by cloning existing ones, instead of instantiating them from scratch.

Useful when
- Creating a new object is expensive, time-consuming, or resource-intensive.
- You want to avoid duplicating complex initialization logic.
- You need many similar objects with only slight differences.

The Prototype Pattern allows you to create new instances by cloning a pre-configured prototype object, ensuring consistency while reducing boilerplate and complexity.
The Prototype pattern specifies the kinds of objects to create using a prototypical instance and creates new objects by copying (cloning) this prototype.

Two ideas
- Self-cloning - The object itself knows how to create a copy of itself. No external code needs to understand its internal structure.
- Decoupled creation - The client does not need to know the concrete class of the object it is cloning. It works through a common interface with a clone() method.

Components
- Prototype
- ConcretePrototype
- Client
- Prototype Registry


