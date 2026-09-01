Singleton Pattern is a creational design pattern that guarantees a class has only one instance and provides a global point of access to it.

Use cases :-
Single instance: No matter how many times any part of the code requests it, the same object is returned.
Global access: Any component can reach the instance without needing it passed through constructors or method parameters.

Useful :-
Managing Shared Resources
Coordinating System-Wide Actions
Managing State

To implement the singleton pattern, we must prevent external objects from creating instances of the singleton class. Only the singleton class should be permitted to create its own objects.

Variations
1. Lazy Initialization (Not Thread-Safe)
2. Thread-Safe Singleton
   - Using synchronized block
3. Double-Checked Locking
   - Double check for synchronized inside if block
4. Eager Initialization
   - In eager initialization, the Singleton instance is created as soon as the class/module is loaded, before any thread can access it. That makes it inherently thread-safe without explicit locks, because initialization happens once during load/initialization.

Language specific implementation
- Bill Pugh / Initialization-on-Demand Holder
  - Static inner class based approach
- Enum Singleton 
  - Simplest and safest approach

