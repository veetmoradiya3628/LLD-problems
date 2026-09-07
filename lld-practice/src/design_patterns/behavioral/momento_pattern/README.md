### Memento Pattern

The Memento Design Pattern is a behavioral design pattern that lets you capture and store an object’s internal state so it can be restored later, without violating encapsulation.

Useful when :
- You need to implement undo/redo functionality.
- You want to support checkpointing or versioning of an object’s state.
- You want to separate the concerns of state storage from state management logic.

The Memento Design Pattern allows an object to save and restore its state without exposing its internal structure. It achieves this by encapsulating the state in a special object called a Memento.

Characteristics
- State capture without exposure.
- External state management. 

Participants 
- Originator
- Memento
- Caretaker
