### LRU Cache
LRU stands for Least Recently Used. LRU Cache is a type of cache replacement policy that evicts the least recently accessed item when the cache reaches its capacity.
In performance-critical systems (like web servers, databases, or OS memory management), caching helps avoid expensive computations or repeated data fetching. But cache memory is limited so when it's full, we need a policy to decide which item to remove.

#### 1. Requirement Gathering

- Functional Requirement
  - Support get(key) operation: returns the value if the key exists, otherwise returns null or -1
  - Support put(key, value) operation: inserts a new key-value pair or updates the value of an existing key
  - If the cache exceeds its capacity, it should automatically evict the least recently used item.
  - Both get and put operations should update the recency of the accessed or inserted item.
  - Keys and values should be generic (e.g., <K, V>), provided the keys are hashable.
- Non-Functional Requirement
  - Time Complexity: Both get and put operations must run in O(1) time on average.
  - Thread Safety: The implementation must be thread-safe for use in concurrent environments.
  - Modularity: The design should follow object-oriented principles with clean separation of responsibilities.
  - Memory Efficiency: The internal data structures should be optimized for speed and space within the defined constraints.

#### 2. Core Identity
- core challenges
  - fast key-based lookup
  - fast ordering to track item usage and enforce eviction based on recency
- 

#### 3. Design class & relationships

#### 4. Code Impl, Run & Test

#### 5. Concurrency & Thread Safety

#### 6. Extensions

#### Design patterns & Principles

#### Open issues
