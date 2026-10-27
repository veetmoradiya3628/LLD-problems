# High-Level Design (HLD) Problem-Solving Framework

Use this playbook for architecture problems involving services, data stores, traffic, availability, and distributed behavior. It works across product types by focusing on workload, critical flows, and system guarantees—not by prescribing a fixed technology stack. Do not design every component for every problem: include a component only when a requirement, scale estimate, or failure mode justifies it.

## What a Good HLD Answer Should Produce

By the end, a reviewer should understand:

1. Who the system serves and which use cases are in scope.
2. The expected workload and the scale-driving assumptions.
3. The major components and how requests/data flow through them.
4. The APIs and data model needed for the critical use cases.
5. How the system meets its important consistency, latency, availability, and durability needs.
6. What happens during overload, dependency failure, retries, and recovery.
7. The key trade-offs and how the design evolves as demand grows.

Typical artifacts: scope and assumptions, workload estimates, API sketch, architecture diagram, critical request/data flows, storage model, consistency and reliability decisions, bottleneck analysis, trade-offs, and phased evolution.

## Step-by-Step Process

### 1. Clarify scope and success criteria

Restate the goal and identify users, clients, external systems, and the system boundary. Separate core features from optional extensions.

- What are the top user journeys?
- What is explicitly out of scope?
- Which requirements are hard constraints versus preferences?
- What does “fast,” “available,” or “reliable” mean for this product?
- Are there privacy, security, regulatory, geographic, or retention constraints?

**Output:** concise functional requirements, non-functional requirements with measurable targets when known, exclusions, and assumptions/open questions.

### 2. Identify critical use cases and rank requirements

List the core read and write flows. Rank them by product importance, traffic, and technical risk. Select one or two flows for the detailed walkthrough.

For each critical flow, identify its caller, input, response, side effects, dependent services, and failure behavior. Distinguish synchronous work needed to answer the request from work that can be asynchronous.

**Output:** prioritized use-case list and a short description of the critical end-to-end flows.

### 3. Estimate workload and data scale

Use provided facts first. If values are unknown, state reasonable assumptions and use ranges; do not present invented precision as fact. Estimate only what can change architecture.

- Users/devices, active fraction, and request frequency.
- Average and peak requests per second; read/write ratio.
- Burstiness, seasonality, fan-out, and hot-key concentration.
- Average payload and data generated per day/year.
- Retention, growth, replication, and rough storage needs.
- Latency targets and the tail percentile that matters (for example p95/p99).

Useful back-of-the-envelope relations:

- Average RPS ≈ daily requests / 86,400; peak RPS is higher and should be estimated from the traffic shape.
- Raw storage ≈ records per time × average record size × retention; add indexes, replicas, and overhead separately.
- Network throughput ≈ requests per second × average bytes per request/response.

**Output:** a small estimate table with assumptions, rough values, and the design decisions each estimate affects.

### 4. Define APIs and contracts

Sketch only APIs needed to explain the main flows. Specify request/response shape, authentication boundary, pagination/filtering where relevant, error behavior, and idempotency for retryable writes.

For asynchronous interactions, identify the event/command, producer, consumer, ordering needs, and delivery semantics. Avoid choosing REST, RPC, or a broker before understanding the interaction.

**Output:** a compact API/event table sufficient to trace client and service interactions.

### 5. Draw the simplest useful architecture

Start with clients, entry point, core services, storage, and external dependencies. Add a component only when it has a clear responsibility. Show sync versus async paths, trust boundaries, and ownership of data.

Ask:

- Which service owns each domain and source of truth?
- Which calls are synchronous on the user path?
- What is cached, and how is it invalidated or made acceptably stale?
- Which workloads need queues, streams, search indexes, or object storage?
- What is intentionally not a separate service?

**Output:** one high-level diagram with labeled data/control flows and a short responsibility list for non-obvious components.

### 6. Choose data ownership and storage

Model the core entities and the access patterns before selecting storage. Consider transaction boundaries, query patterns, data size, write/read rates, retention, consistency, and operational capability.

- Use relational storage when relationships, constraints, and multi-record transactions matter.
- Consider key-value/document/wide-column/search/graph/time-series stores when access patterns or scale justify them.
- Use object/blob storage for large immutable files; keep metadata and authorization in suitable stores.
- Separate source-of-truth data from derived indexes/caches.
- Define primary keys, indexes, partition/shard keys, and hot-partition risks where relevant.

There is no universally best database. Explain why the selected model fits the access pattern and what it makes harder.

**Output:** a small schema/entity sketch, ownership map, and storage choice with rationale.

### 7. Deep-dive the critical flow

Trace a request step by step through components. Include input validation, authentication/authorization where relevant, reads/writes, response, and asynchronous side effects.

For state-changing operations, explain:

- Transaction and atomicity boundary.
- Consistency expected by the caller.
- Duplicate request and retry behavior.
- Ordering requirements.
- Partial failure and compensation behavior.
- How derived state is updated and repaired.

**Output:** a sequence diagram or numbered flow and the core correctness mechanism.

### 8. Design for scale, latency, and resilience

Tie each scaling technique to an identified bottleneck; avoid adding infrastructure without evidence.

- **Scale:** stateless horizontal scaling, batching, partitioning, read replicas, caching, CDN, queue-based smoothing.
- **Latency:** minimize synchronous hops, parallelize independent reads carefully, cache appropriate data, set timeouts and budgets.
- **Reliability:** redundancy, health checks, bounded retries with backoff/jitter, circuit breakers, bulkheads, graceful degradation.
- **Queues/streams:** define backpressure, consumer lag, replay, poison-message handling, delivery semantics, and idempotent consumers.
- **Cache:** define source of truth, TTL/invalidation, stale-read tolerance, stampede protection, and behavior on cache outage.
- **Data protection:** backups, restore objectives, replication, disaster recovery, retention, and deletion behavior.

**Output:** a short table mapping each key requirement or bottleneck to a mechanism and its trade-off.

### 9. Address security, privacy, and operations

Include only the depth required, but do not leave sensitive paths implicit.

- Authentication, authorization, least privilege, secret management.
- Encryption in transit/at rest and handling of sensitive data.
- Abuse protection, quotas/rate limits, validation, auditability.
- Logs, metrics, traces, SLOs, alerting, and dashboards.
- Deployment, migrations, rollback, capacity planning, and incident recovery.

**Output:** the security/operability decisions that materially affect the architecture.

### 10. Identify bottlenecks, failure modes, and trade-offs

Challenge the design:

- What is the first component to saturate at 10× traffic?
- What is a single point of failure?
- What if the database, cache, queue, or external provider is unavailable?
- What consistency can be relaxed, and which invariants cannot?
- What hot keys, large tenants, or uneven partitions may appear?
- Which component adds operational complexity without enough benefit?

For each major choice, state one alternative and why it was not chosen. Distinguish current needs from future scale.

**Output:** prioritized risks, mitigations, and explicit trade-offs.

### 11. Present an evolution plan

Describe the smallest credible initial system, then the changes triggered by measured growth or new requirements. Do not begin with a globally distributed architecture unless the problem requires it.

Example progression:

1. Single application with a well-chosen durable store.
2. Add caching or asynchronous work when a measured bottleneck appears.
3. Split service/data ownership when team boundaries, load, or deployment needs justify it.
4. Partition/shard or distribute across regions when capacity or availability targets demand it.

**Output:** a brief “now / next / later” plan tied to measurable triggers.

## Suggested Interview Timebox (45 Minutes)

| Time | Activity |
| --- | --- |
| 0–5 min | Scope, users, assumptions, success targets |
| 5–10 min | Workload estimates and critical use cases |
| 10–18 min | APIs, data ownership, initial architecture |
| 18–30 min | Deep dive into the most critical flow |
| 30–38 min | Scale, consistency, reliability, security |
| 38–43 min | Bottlenecks, failure modes, trade-offs |
| 43–45 min | Summarize and propose evolution |

Adapt to interviewer direction. If interrupted or time-limited, keep the requirements and correctness model clear and spend detail on the highest-risk flow.

## Reusable HLD Write-Up Template

```markdown
# [Problem] — High-Level Design

## 1. Scope and Success Criteria
- Goal and system boundary:
- Actors:
- Functional requirements:
- NFRs/SLOs:
- Out of scope:

## 2. Assumptions and Open Questions
- Assumptions:
- Questions:

## 3. Use Cases and Workload
- Critical flows:
- Traffic/read-write estimates:
- Data growth/retention:
- Latency and availability targets:

## 4. APIs and Events
- Main API contracts:
- Events/commands and delivery requirements:

## 5. Architecture
- Diagram:
- Component responsibilities:
- Sync and async boundaries:
- Data ownership:

## 6. Data Model and Storage
- Core entities/access patterns:
- Store choices and rationale:
- Keys, indexes, partitioning:

## 7. Critical Flow Deep Dive
- Sequence:
- Transaction/consistency model:
- Retry/idempotency:
- Partial failures/compensation:

## 8. Scale, Reliability, Security, Operations
- Bottlenecks and scaling:
- Failure handling/recovery:
- Security/privacy:
- Observability and SLOs:

## 9. Trade-offs and Evolution
- Alternatives:
- Risks:
- Now / next / later:
```

## How HLD and LLD Fit Together

HLD decides service boundaries, data ownership, communication, and system-level guarantees. LLD details the objects, APIs, algorithms, and state transitions inside a component. Move between them when a system-level choice depends on a local invariant—for example, HLD identifies seat inventory as a consistency boundary, while LLD defines how a booking operation atomically claims and releases seats. Do not duplicate every LLD class in the HLD.

## Adapt the Framework to the Problem

Select relevant sections, not every technology:

- A URL shortener may focus on key generation, read/write traffic, cache behavior, and collisions.
- A chat or feed system may focus on fan-out, ordering, delivery, and offline behavior.
- A payment or booking system may focus on idempotency, strong invariants, transactions, and reconciliation.
- A media system may focus on object storage, transcoding pipelines, CDN delivery, and bandwidth.
- An internal low-volume tool may need a single service and database; justify simplicity rather than inventing scale.

When requirements or scale are unknown, state assumptions, show how they affect the design, and explain what would change if the assumptions are wrong.