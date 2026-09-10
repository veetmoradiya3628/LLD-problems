### PubSub System

#### 1. Requirement Gathering

- Functional Requirement
  - The Pub-Sub system should allow publishers to publish messages to specific topics.
  - Subscribers should be able to subscribe to topics of interest and receive messages published to those topics.
  - The system should support multiple publishers and subscribers.
  - Messages should be delivered to all subscribers of a topic in real-time.
  - Future scope involve having attribute based subscription and delivery
- Non-Functional Requirement
  - The system should handle concurrent access and ensure thread safety.
  - The Pub-Sub system should be scalable and efficient in terms of message delivery.

#### 2. Core Identity
- Broker
  - Manages topics, subscriptions, and message delivery
- Topic
  - Represents a topic to which messages can be published and subscribers can subscribe
- Publisher
  - Publishes messages to topics via the pubSubService.
- Subscriber - interface
  - Interface for all subscribers, defines the consume(Message) method.
- PrintSubscriber
  - A subscriber that prints received messages.
- LoggingSubscriber
  - A subscriber that logs received messages.
- Message
  - Represents a message with a payload.
- Dispatcher
  - Handles asynchronous delivery of messages to subscribers.

#### 3. Design class & relationships
- Message - class
  - POJO class to simply implement a message structure
  - timestamp: Instant
  - payload: String
- Topic - class
  - name: String
  - deliveryExecutor: ExecutorService
  - subscribers: Set<Subscriber>
  - addSubscriber(Subscriber): void
  - removeSubscriber(Subscriber): void
  - broadcast(Message): void
- Subscriber - Interface
  - onMessage(Message): void
- AlertSubscriber: class implements Subscriber
  - id: String
  - onMessage(Message): void
- NewsSubscriber: class implements Subscriber
  - id: String
  - onMessage(Message): void
- PubSubService - class
  - deliveryExecutor: ExecutorService
  - topicRegistry: Map<String, Topic>
  - createTopic(String): void
  - publish(String, message): void
  - subscribe(String, Subscriber): void
  - unsubscribe(String, Subscriber): void
  - shutdown(): void

#### 4. Code Impl, Run & Test

#### 5. Concurrency & Thread Safety

#### 6. Extensions

#### Design patterns & Principles

#### Open issues
