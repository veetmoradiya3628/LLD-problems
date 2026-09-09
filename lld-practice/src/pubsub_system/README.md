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

#### 4. Code Impl, Run & Test

#### 5. Concurrency & Thread Safety

#### 6. Extensions

#### Design patterns & Principles

#### Open issues
