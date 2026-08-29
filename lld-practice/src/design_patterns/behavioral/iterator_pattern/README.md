The Iterator Design Pattern is a behavioral pattern that provides a standard way to access elements of a collection sequentially without exposing its internal structure.

Useful when :
- You need to traverse a collection (like a list, tree, or graph) in a consistent and flexible way.
- You want to support multiple ways to iterate (e.g., forward, backward, filtering, or skipping elements).
- You want to decouple traversal logic from collection structure, so the client doesn't depend on the internal representation.

Two characteristics
1. Separation of traversal from storage
2. Multiple Independent traversals

Components
1. Iterator - interface
2. IterableCollection - interface
3. ConcreteCollection - class
4. ConcreteIterator - class