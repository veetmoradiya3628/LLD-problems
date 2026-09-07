### Chain of Responsibility Pattern

The Chain of Responsibility Design Pattern is a behavioral pattern that lets you pass requests along a chain of handlers, allowing each handler to decide whether to process the request or pass it to the next handler in the chain.

Useful when
- A request must be handled by one of many possible handlers, and you don’t want the sender to be tightly coupled to any specific one.
- You want to decouple request logic from the code that processes it.
- You want to flexibly add, remove, or reorder handlers without changing the client code.

The Chain of Responsibility Pattern allows a request to be passed along a chain of handlers. Each handler in the chain can either:

Handle the request and stop the chain
Pass it to the next handler in the chain
Handle the request and then pass it along

This pattern decouples the sender of the request from the receivers, giving you the flexibility to compose chains dynamically, reuse logic, and avoid rigid conditional blocks.

Components
- Handler (interface)
- ConcreteHandlers
- Client
