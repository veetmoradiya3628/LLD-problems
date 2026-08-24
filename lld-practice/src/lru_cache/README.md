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
    - To efficiently retrieve values by key, we need a data structure that supports constant-time key access.
    - Choice is HashMap
  - fast ordering to track item usage and enforce eviction based on recency
    - Doubly LL allows this operation to
      - move most recently used item at the front
      - remove least recently item from the back
      - insert new item at the front
- Combine both hashmap and doubly linked list approach
  - Instead of value hashmap stores reference to the node on doubly LL
  - Doubly LL maintains the usage order. head is always MRU and tail is always LRU
- Along with DoublyLL & HashMap we need
  - Node - A simple internal class that represents an individual entry in the cache and a node in the linked list. It stores the key-value pair and maintains pointers to adjacent nodes.
  - LRUCache - The main class that exposes the public cache API and coordinates all operations. It owns both the HashMap and the DoublyLinkedList.

#### 3. Design class & relationships
- Node<K, V> - Data class
  - Stores key-value pair and maintains linked list operations
- DoublyLinkedList<K, V> - Utility class
  - Manages MRU and LRU ordering with O(1) operations
- HashMap<K, Node> - Standard Library
  - Provides O(1) Key-to-node lookup
- LRUCache<K, V> - Main class
  - Coordinates all operations and enforces eviction policy

#### 4. Code Impl, Run & Test
- Node<K, V> - class (data container)
  - -K key
  - -V value
  - -Node<K, V> prev
  - -Node<K, V> next
  - +Node(K key, V value)
- DoublyLinkedList<K, V> - Class manages MRU to LRU
  - -Node<K, V> head
  - -Node<K, V> tail
  - +DoublyLinkedList()
  - +addFirst(Node<K, V> node)
  - +remove(Node<K, V> node)
  - +moveToFront(Node<K, V> node)
  - +removeLast() : Node<K, V>
- LRUCache<K, V> - main class that provides the public API (get and put) and manages the overall cache logic
  - -int capacity
  - -Map<K, Node<K, V>> map
  - -DoublyLinkedList<K, V> list
  - +LRUCache(int capacity)
  - +get(K key): V
  - +put(K key, V value)

#### 5. Design Principle, Concurrency & Thread Safety
- Single Responsibility - The LRUCache coordinates operations but delegates ordering to DoublyLinkedList and lookup to HashMap. Each component does one thing well.
- Encapsulation - The internal data structures are private. Callers only see get and put. They don't know about nodes, linked lists, or eviction mechanics.
- Thread Safety - Both get and put should be synchronized to prevent race conditions in multi-threaded environments.

#### 6. Extensions
1. Per-entry TTL Expiry
   - Cached values should expire after a fixed time, even if they are still being accessed.
   - The cleanest place to enforce this is inside get: if the looked-up node is past its expiry, remove it and return null as if it were never there.
2. Weight-based (Size-aware) Eviction
   - Cap the cache by total memory, not by a fixed number of entries.
   - The fix is to give each node a weight and track the running total. Eviction then keeps calling removeLast until the total weight fits under the limit, which can remove several small entries or a single large one.
3. Hit & miss metrics
   - Report how often the cache actually helps.
   - The fix is to increment two counters inside get: a hit when the key is present and live, a miss when it is absent or expired. Exposing a small stats snapshot lets a dashboard or log line track the ratio over time.

#### Open issues
