# Design Document: [System Name]

## 1. Scope & Requirements
**Functional Requirements:**
1. [Core Feature 1]
2. [Core Feature 2]
3. [Core Feature 3]

**Out of Scope:**
*   [Excluded Feature 1]
*   [Excluded Feature 2]

**Non-Functional Requirements:**
*   **Latency:** [e.g., < 200ms]
*   **Availability:** [e.g., 99.99%]
*   **Consistency Model:** [Strong vs. Eventual]

---

## 2. Capacity Estimations
*   **Traffic Estimate:** [e.g., 50M DAU, Peak RPS: 10,000]
*   **Storage Estimate:** [e.g., 500GB/day -> ~1TB/year]
*   **Memory (Cache) Estimate:** [e.g., 20% of daily read traffic = 50GB RAM needed]

---

## 3. High-Level Architecture (HLD)
### 3.1 APIs
| Endpoint | Method | Payload / Params | Description |
| :--- | :--- | :--- | :--- |
| `/api/v1/resource` | `POST` | `{ "key": "value" }` | Creates a new resource |
| `/api/v1/resource/{id}` | `GET` | `id` (path var) | Fetches the resource |

### 3.2 System Architecture Diagram
*(If practicing digitally, insert Mermaid.js code or an image link here. If on paper, note "See whiteboard sketch 1")*
*   **Client -> API Gateway -> Service A -> Database**
*   **Message Queue (Kafka) -> Async Workers -> Notification Service**

### 3.3 Component Breakdown
*   **Component A:** [What it does and why it exists]
*   **Component B:** [What it does and why it exists]

---

## 4. Low-Level Design (LLD)
### 4.1 Database Schema
**Database Type:** [e.g., PostgreSQL for ACID guarantees]

**Table: `Entity_A`**
| Field | Type | Attributes |
| :--- | :--- | :--- |
| `id` | UUID | Primary Key |
| `user_id` | UUID | Indexed, FK to Users |
| `created_at` | Timestamp | |

### 4.2 Core Logic / Algorithms
*Detail the most complex part of the code here.*
*   **Process:** [e.g., Concurrent Ticket Booking]
*   **Logic:**
    1. Begin Database Transaction.
    2. `SELECT * FROM seats WHERE id = X FOR UPDATE` (Pessimistic lock).
    3. If seat status is 'AVAILABLE', update to 'BOOKED'.
    4. Commit Transaction.

---

## 5. Scaling, Resilience, and Trade-offs
### 5.1 Resolving Bottlenecks
*   **Database Scaling:** To handle the read-heavy load, we will provision 3 Read-Replicas.
*   **Caching:** Implemented Redis as a look-aside cache to reduce DB load by 80%.

### 5.2 Trade-offs Made
*   **Choice A over Choice B:** Chose Cassandra over PostgreSQL for the analytics service because write throughput was prioritized over complex JOIN capabilities.
*   **Consistency vs. Latency:** Opted to update the user's feed asynchronously via a message queue. This creates eventual consistency but guarantees the user isn't waiting on the UI.
