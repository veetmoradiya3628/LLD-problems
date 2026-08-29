The Bridge Design Pattern is a structural pattern that lets you decouple an abstraction from its implementation, allowing the two to vary independently.

- useful when
  - You have classes that can be extended in multiple orthogonal dimensions
  - You want to avoid a deep inheritance hierarchy that multiplies combinations of features.
  - You need to combine multiple variations of behavior or implementation at runtime.

- Abstraction & Inheritance
- These two hierarchies are "bridged" via composition (not inheritance) allowing you to mix and match independently.

- Participants
  - Abstraction
  - RefinedAbstraction
  - Implementor
  - ConcreteImplementor

