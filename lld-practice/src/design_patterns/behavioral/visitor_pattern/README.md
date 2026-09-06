### Visitor pattern

The Visitor Design Pattern is a behavioral pattern that lets you add new operations to existing object structures without modifying their classes.

Useful when :-
- You have a complex object structure (like ASTs, documents, or UI elements) that you want to perform multiple unrelated operations on.
- You want to add new behaviors to classes without changing their source code.
- You need to perform different actions depending on an object’s concrete type, without resorting to a long chain of if-else or instanceof checks.

The Visitor Design Pattern lets you separate algorithms from the objects on which they operate. It enables you to add new operations to a class hierarchy without modifying the classes themselves.

Characteristics
- Separation of algorithm and structure: The data classes (elements) stay clean. All operational logic lives in visitor classes that are entirely separate from the element hierarchy.
- Double dispatch: The correct method to call is determined by both the type of the element and the type of the visitor. The element calls back the visitor with this, which resolves the element's concrete type at compile time. This two-step dispatch is what makes the pattern work without instanceof checks.

Components
1. Element - Interface
2. Concrete Elements - Class
3. Visitor - Interface
4. Concrete Visitors - Class