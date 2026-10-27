# Low-Level Design (LLD) Problem-Solving Framework

Use this playbook for object-oriented and domain-level design problems: vending machines, booking systems, games, caches, parking lots, payment flows, and similar interview exercises. It is a reusable process, not a requirement to use every pattern or class in every answer. Spend time on the rules and difficult interactions; keep straightforward parts simple.

## What a Good LLD Answer Should Produce

By the end, someone should be able to understand:

1. What the system is expected to do and what assumptions constrain the design.
2. The main domain objects, their responsibilities, and how they relate.
3. The public operations/use cases and the important state transitions.
4. How critical flows behave on success, failure, and concurrent requests.
5. Why the design choices are appropriate and what is deliberately deferred.
6. How the design maps to a small, testable implementation.

Typical artifacts: concise requirements, assumptions, use-case list, domain model/class diagram, key API signatures, one or two important sequence/state diagrams, critical algorithm or pseudocode, pattern/trade-off notes, and focused tests.

## Step-by-Step Process

### 1. Clarify the problem and set scope

Restate the goal in one sentence. Identify the primary actors and the system boundary. Ask or state assumptions about ambiguous rules rather than silently inventing them.

- Who uses the system, and what are their primary goals?
- What are the 3–6 essential user-visible operations?
- What does each important term mean? For example, is a seat “held,” “reserved,” or “booked”?
- What happens on cancellation, timeout, retry, duplicate request, or invalid input?
- What is explicitly out of scope for this version?

**Output:** a short scope statement, functional requirements, important non-functional requirements, and a numbered list of assumptions/open questions. Separate confirmed requirements from assumptions.

### 2. Identify use cases and scenarios

Write use cases as actor + intent + result. Include the normal path and the most important alternate paths. Prioritize by business importance and design risk, not by the number of features.

For each critical use case, trace:

1. What input is supplied and what must be validated?
2. Which object/service owns the decision?
3. What state changes, and in what order?
4. What is returned or emitted?
5. Which failures must be handled, and what must be rolled back?

**Output:** a concise use-case list and a written happy path plus key failure paths for the hardest operation.

### 3. Model the domain and state

Find domain concepts from the requirements; do not make a class for every noun. For each candidate, identify its identity, important data, invariants, and behavior.

- **Entities:** have identity and lifecycle (for example `Booking`, `User`, `Vehicle`).
- **Value objects:** represent values compared by their contents (for example `Money`, `DateRange`, `Address`).
- **Services:** coordinate a use case that does not belong naturally to one entity.
- **Repositories/gateways:** abstract persistence or external systems only where needed.
- **Policies/strategies:** isolate a rule that varies or needs substitution.

Write down invariants such as “a seat belongs to at most one active booking” or “a completed order cannot be edited.” Model status transitions explicitly and reject invalid transitions.

**Output:** a domain glossary, key invariants, entity/value-object list, and a state diagram or transition table when lifecycle matters.

### 4. Assign responsibilities and define boundaries

Give each type one clear reason to change. Keep domain rules close to the state they protect; keep orchestration in an application/service layer. Identify boundaries to persistence, payment, messaging, clock, random IDs, or other external systems when those matter to correctness or testing.

Use interfaces at meaningful seams—not for every type by default. Prefer composition and explicit dependencies over global state and hidden singletons. Make mutation boundaries clear.

**Output:** a responsibility table and key interface/API signatures, with ownership of important invariants made explicit.

### 5. Design the hardest flow before drawing every class

Choose the use case with the most interactions or risk (often booking, payment, cancellation, or a turn in a game). Walk through it in order, including concurrency and failure cases.

- Define preconditions and postconditions.
- Decide when state is reserved, committed, released, or rolled back.
- Decide how duplicate submissions/retries are handled if relevant.
- Identify atomic boundaries and possible race conditions.
- Keep external calls, locks, and irreversible side effects in a deliberate order.
- Specify what happens if a dependency fails or times out.

Use pseudocode first. Add a sequence diagram for collaboration and a state diagram when transitions are non-trivial.

**Output:** an algorithm/sequence diagram detailed enough to implement and test.

### 6. Define API contracts and errors

Describe the operations a caller can request, their inputs/outputs, and observable failure modes. Prefer explicit domain errors (for example, `SeatUnavailable`, `NotFound`, `InvalidTransition`) over ambiguous booleans or silent fallback values. Clarify whether operations are idempotent and what callers can safely retry.

**Output:** a small operation table or method signatures, including meaningful errors and retry/idempotency behavior where applicable.

### 7. Check concurrency and consistency

Ask whether requests can run in parallel and identify the shared state. Choose the simplest mechanism that preserves invariants at the actual deployment boundary:

- Single process: mutex/lock or actor-style ownership may suffice.
- Persistent single database: transaction, locking, or conditional update may be better.
- Multiple instances: in-memory locks are not sufficient; use a shared atomic/transactional mechanism.

Specify lock scope and ordering if using locks. Avoid claiming global safety from a per-object or per-process lock. Consider races between reads and writes, duplicate work, deadlocks, and visibility of intermediate states.

**Output:** a short consistency/thread-safety statement: what is protected, by what, and where that guarantee stops.

### 8. Apply design patterns only when they solve a stated problem

For each proposed pattern, name the concrete variation or coupling it handles. Common candidates include:

| Pattern | Use when |
| --- | --- |
| Strategy | An algorithm or policy has interchangeable implementations |
| State | Behavior depends on lifecycle state and transitions are meaningful |
| Observer / Pub-Sub | Changes should notify independent listeners |
| Factory | Construction rules are complex, centralized, or select among variants |
| Repository | Domain/application logic needs storage independence |
| Adapter | An external API does not match the system’s contract |
| Decorator | Behavior needs composable wrapping without subclass explosion |
| Command | Requests need queuing, retry, audit, or undo semantics |
| Singleton | A truly process-wide unique resource is required; avoid as a default |

Do not add a pattern merely to demonstrate knowledge. A direct method or small function is often better. Mention meaningful alternatives and why the chosen design is simpler or safer.

**Output:** a brief pattern-to-purpose mapping, including patterns considered but intentionally omitted if that helps explain simplicity.

### 9. Validate with examples and tests

Test behavior and invariants, not just class methods. At minimum, cover:

- A representative successful use case.
- Invalid input and missing resources.
- Boundary values and invalid state transitions.
- Failure from an external dependency and required cleanup/rollback.
- Concurrent attempts for a resource that must remain exclusive.
- Retry/idempotency behavior if required.

Use injected clocks, ID generators, payment providers, or repositories when deterministic tests need them. Keep tests focused on externally observable outcomes.

**Output:** a test matrix mapping requirements/invariants to scenarios and expected results.

### 10. Review the design and communicate trade-offs

Walk the critical use cases through the model. Check that every requirement has an owner and that no class is doing unrelated work. Look for over-generalization, mutable public state, hidden dependencies, unhandled failures, and synchronization claims that exceed the implementation.

State what this version intentionally does not solve and what would change at larger scale or with new requirements.

**Output:** a concise list of trade-offs, limitations, and the next extension seam.

## Suggested Interview Timebox (45 Minutes)

| Time | Activity |
| --- | --- |
| 0–5 min | Clarify scope, actors, assumptions |
| 5–10 min | Use cases, invariants, state |
| 10–18 min | Domain model and responsibilities |
| 18–30 min | Deep dive into the hardest flow, failures, concurrency |
| 30–38 min | APIs, diagrams, pattern choices |
| 38–43 min | Test cases and design review |
| 43–45 min | Summarize decisions and open questions |

Adjust the timebox to the interview. If time is short, favor a correct critical flow and explicit assumptions over a large class diagram.

## Reusable LLD Write-Up Template

```markdown
# [Problem] — Low-Level Design

## 1. Scope
- Goal:
- Actors:
- Functional requirements:
- Non-functional requirements:
- Out of scope:

## 2. Assumptions and Open Questions
- Assumptions:
- Questions to confirm:

## 3. Use Cases and Invariants
- Use cases:
- Invariants:
- State transitions:

## 4. Domain Model
- Entities and value objects:
- Responsibilities:
- Class/relationship diagram:

## 5. APIs and Key Workflows
- Key operation contracts:
- Happy path:
- Failure/rollback paths:
- Sequence/state diagram or pseudocode:

## 6. Concurrency and Consistency
- Shared state:
- Atomicity/locking mechanism:
- Guarantee boundary:

## 7. Patterns and Trade-offs
- Pattern → problem solved:
- Simpler alternatives considered:

## 8. Validation
- Test scenarios:
- Complexity, if relevant:

## 9. Limitations and Next Steps
- Known gaps:
- How the design evolves:
```

## Adapt the Framework to the Problem

Use only relevant sections. A board game may emphasize turn ownership, legal moves, and state transitions; a cache may emphasize eviction policy, complexity, and concurrency; a booking/payment system may emphasize atomicity, retries, and compensation; a CRUD-heavy catalog may need little pattern or concurrency discussion. If persistence or distributed operation is outside the exercise, say so instead of designing an unnecessary production platform.