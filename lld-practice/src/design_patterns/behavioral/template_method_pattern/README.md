The Template Method Design Pattern is a behavioral design pattern that defines the skeleton of an algorithm in a base class, but allows subclasses to override specific steps of the algorithm without changing its overall structure.

characteristics 
- Algorithm skeleton in the base class (Abstract class)
- Subclasses override specific steps (Concrete Classes)

Two types of methods
- Template Method
  - The method in the abstract class that defines the algorithm's skeleton. Mixes abstract method calls (subclass-provided) with concrete method calls (base-class-provided) and hook calls (optionally overridden).
- Hooks 
  - Concrete methods in the abstract class with a default implementation (often empty or trivial) that subclasses can optionally override. Provides extension points without forcing subclasses to implement them.

Advantages of Templater pattern
- Eliminates code duplication
- Enforced consistency
- Made the system extensible
- Improved maintainability
- Provides clear extension points