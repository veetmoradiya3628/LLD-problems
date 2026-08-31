The Command Design Pattern is a behavioral pattern that turns a request into a standalone object, allowing you to parameterize actions, queue them, log them, or support undoable operations all while decoupling the sender from the receiver.

Useful when
- You want to encapsulate operations as objects.
- You need to queue, delay, or log requests.
- You want to support undo/redo functionality.
- You want to decouple the object that invokes an operation from the one that knows how to perform it.

Characteristics 
- Encapsulation of requests as objects
- Decoupling of invoker and receiver

Components
1. Command
2. ConcreteCommand
3. Receiver
4. Invoker