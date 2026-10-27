# Practical Design Toolkit

This guide complements the [LLD problem framework](../lld-problem-framework.md) and [HLD problem framework](../hld-problem-framework.md). Use it when a design needs more detail in API contracts, data persistence, deployment, or the tools used to build and operate a system.

These are decision guides, not mandatory technology choices. Start with requirements and constraints; use a tool only when it addresses a real need.

## 1. API Contract Design

An API contract is the agreement between a caller and a service. Design the behavior first, then choose a transport such as HTTP/JSON, gRPC, or asynchronous messaging.

### Step-by-step

1. **Identify consumers and use cases.** List who calls the API, their permissions, and the operations they need.
2. **Choose resource and operation boundaries.** Use domain language; make reads, state changes, and long-running operations clear.
3. **Define request and response schemas.** Specify required/optional fields, formats, limits, defaults, and validation rules. Avoid exposing internal database shapes as the public contract.
4. **Specify errors.** Define stable error codes, HTTP status or RPC status, safe user-facing messages, and whether a client may retry. Never make callers infer failure from an empty success response.
5. **Design for retries.** For non-idempotent operations, decide whether to support an idempotency key and how long its result is retained. Document duplicate-request behavior.
6. **Define pagination and filtering.** Set maximum page sizes and deterministic ordering. Prefer cursor pagination for large or changing datasets when offset pagination would be unstable or expensive.
7. **Set compatibility and versioning rules.** Prefer additive, backward-compatible changes. Define how incompatible changes are introduced and how old versions are retired.
8. **Include cross-cutting behavior.** Document authentication/authorization, rate limits, timeouts, conditional requests, correlation IDs, and deprecation where relevant.
9. **Review with examples and tests.** Check successful, invalid, unauthorized, duplicate, and dependency-failure cases.

### Contract checklist

| Concern | Questions to answer |
| --- | --- |
| Naming and semantics | Do names use domain terms? Are side effects clear? |
| Validation | Which fields are required? What formats, ranges, and limits apply? |
| Errors | Can clients distinguish not found, conflict, invalid input, and transient failure? |
| Idempotency | What happens when a write is retried after a timeout? |
| Pagination | Is ordering stable? Are limits bounded? |
| Security | How are callers authenticated and authorized? Is sensitive data excluded? |
| Compatibility | Can old clients continue to work after additive changes? |
| Operations | Are timeouts, rate limits, request IDs, and retry guidance clear? |

### REST, gRPC, and asynchronous contracts

- **REST/HTTP with JSON:** a practical default for public and browser-facing APIs, broad interoperability, and resource-oriented operations. Document with OpenAPI.
- **gRPC:** useful for typed service-to-service APIs, generated clients, and streaming. Define protobuf compatibility rules and consider gateway needs for browser/public access.
- **Events and messages:** useful when producers and consumers should be decoupled or work can happen asynchronously. Document event schemas, ownership, ordering, delivery expectations, replay, and consumer idempotency. AsyncAPI can describe message-driven interfaces.

Do not force every system into one style. A system can expose REST externally and use gRPC or events internally, provided each boundary has a clear contract.

### Example REST contract sketch

```text
POST /v1/bookings
Request: { concertId, seatIds, idempotencyKey }
Success: 201 { bookingId, status, total }
Possible errors:
  400 invalid request
  404 concert not found
  409 one or more seats unavailable
  422 payment declined
  429 rate limit exceeded
  503 temporary dependency failure
Retry: repeat with the same idempotency key to retrieve the original result
```

The exact status mapping is a product/API decision; preserve stable error semantics even if internal implementations change.

### Expected API design output

- Contract definitions for critical operations and events.
- Schemas with validation constraints and representative examples.
- Error and retry/idempotency behavior.
- Authentication/authorization and compatibility policy.
- Contract validation or consumer tests where appropriate.

## 2. Database Design

Database selection follows the required access patterns, invariants, scale, and operational constraints. Avoid choosing a database by popularity or by assuming one database must serve every workload.

### Step-by-step

1. **List the data and its owner.** Identify entities, relationships, lifecycle, source of truth, and data sensitivity.
2. **Write access patterns.** List the important reads, writes, queries, sorting, reporting, and retention needs. Estimate volume and concurrency.
3. **Identify invariants and atomic operations.** Examples: unique email, non-negative balance, one active booking per seat. Decide which must be enforced atomically.
4. **Choose a data model.** Relational tables, documents, key-value records, time-series, graph, and object storage solve different access patterns. Select based on evidence.
5. **Define keys, constraints, and indexes.** Choose stable identifiers; use constraints for correctness; add indexes for measured or expected query patterns. Consider index write and storage costs.
6. **Set transaction and consistency requirements.** Specify which updates must commit together, acceptable staleness, isolation needs, and how concurrent writes are resolved.
7. **Plan growth and retention.** Estimate storage and index growth; consider archival, partitioning, sharding, and hot-key risks only when justified.
8. **Plan schema evolution.** Use reviewed, repeatable migrations; consider backward-compatible expand/migrate/contract changes for rolling deployments.
9. **Plan operations and recovery.** Define backup frequency, restore testing, recovery point/time objectives, replication, monitoring, and data deletion requirements.
10. **Validate with realistic workloads.** Explain queries, inspect query plans, benchmark representative data, and test concurrent updates and failure recovery.

### Relational or non-relational?

- **Relational databases** (for example PostgreSQL) are a strong choice when relationships, constraints, joins, and multi-row transactions are important.
- **Key-value/document stores** can fit direct key access or flexible records at scale, when query and transaction requirements align with the store's model.
- **Search engines** are usually derived indexes for full-text search and relevance; keep a clear source of truth and reindexing strategy.
- **Caches** accelerate reads or hold short-lived derived data; they should not silently become the only durable copy unless explicitly designed as such.
- **Object storage** is appropriate for large blobs such as images, recordings, and backups; store metadata and access policy separately as needed.
- **Message logs/streams** support event distribution and replay; they are not automatically a replacement for the domain's authoritative database.

Polyglot persistence adds operational and consistency costs. Start with the fewest stores that meet the requirements.

### Database design review

- Can the database enforce the most important invariants?
- Are transaction boundaries aligned with business operations?
- Can a retry create duplicate effects?
- Do indexes support the actual filter and sort patterns?
- Are uniqueness and nullability rules explicit?
- Are query and data growth costs understood?
- Is there a tested backup restore and migration path?
- Are personal or sensitive data retention/deletion rules defined?

### Expected database design output

- Entity/schema sketch and ownership of each dataset.
- Main queries/access patterns and supporting indexes.
- Transaction, isolation, consistency, and concurrency approach.
- Migration, backup/restore, retention, and scaling notes.
- Explicit rationale for database choice and its trade-offs.

## 3. Kubernetes Deployment Design

Kubernetes is an orchestration platform, not a default requirement. Consider it when the team needs its scheduling, self-healing, rollout, service discovery, and ecosystem capabilities and can operate the added complexity. A managed application platform or a simpler deployment can be better for a small system.

### Step-by-step

1. **Define the runtime shape.** Identify stateless services, workers, scheduled jobs, stateful dependencies, and external services.
2. **Decide what belongs in the cluster.** Prefer managed databases and managed queues unless there is a clear reason and operational capability to run them on Kubernetes.
3. **Map workloads to resources.** Common choices include `Deployment` for stateless replicated applications, `Job`/`CronJob` for finite/scheduled tasks, and `StatefulSet` plus persistent volumes for workloads needing stable identity/storage.
4. **Design network entry and service discovery.** Define `Service`, ingress or Gateway API, TLS termination, DNS, network policies, and external exposure.
5. **Set configuration and secrets.** Keep non-secret configuration separate from credentials; use an approved secret manager or external secrets integration. Do not commit secret values to manifests.
6. **Set resource and scaling policies.** Specify CPU/memory requests and limits, replicas, disruption budget, autoscaling signals, and capacity headroom. Ensure workloads can tolerate rescheduling.
7. **Define health and shutdown behavior.** Use startup, readiness, and liveness probes for distinct purposes. Handle termination signals, graceful draining, and bounded shutdown time.
8. **Plan rollout and rollback.** Use rolling updates or another rollout strategy with readiness checks, safe schema compatibility, and a known rollback procedure.
9. **Add security and policy.** Use least-privilege service accounts/RBAC, restricted pod security, image scanning/signing as required, network policies, and resource quotas.
10. **Make it observable and recoverable.** Collect logs, metrics, and traces; alert on user-visible symptoms; define backup, restore, cluster upgrade, and disaster recovery ownership.
11. **Test operational behavior.** Exercise deployment, failed readiness, pod termination, scaling, dependency outage, and rollback in a representative environment.

### Workload-to-resource mapping

| Workload | Typical Kubernetes resource | Design notes |
| --- | --- | --- |
| Stateless HTTP/API service | `Deployment` + `Service` | External traffic generally enters through Ingress or Gateway API |
| Background consumer | `Deployment` | Scale based on queue lag or another workload metric; make handling idempotent |
| Finite task | `Job` | Set retries, deadlines, resource bounds, and duplicate-effect protection |
| Scheduled task | `CronJob` | Decide whether overlapping executions are allowed |
| Stateful application | `StatefulSet` + PVC | Use only with a sound storage, backup, failover, and upgrade plan |
| Cluster configuration | ConfigMap / Secret reference | Keep secrets out of source control; prefer external secret management where available |

Kubernetes objects are examples, not a complete architecture. Exact choices depend on the cluster, cloud provider, security policy, and workload.

### Deployment design output

- Workload/resource map and deployment diagram.
- Ingress, service discovery, TLS, and network-boundary decisions.
- Resource requests/limits, replica and autoscaling approach.
- Health checks, graceful shutdown, rollout, and rollback behavior.
- Configuration/secrets and access-control approach.
- Logging/metrics/tracing, backup/recovery, and operational ownership.

## 4. Security and Privacy Design

Security should shape requirements, APIs, data models, and deployment—not be postponed to a final checklist.

### Step-by-step

1. **Identify assets and trust boundaries.** Mark sensitive data, credentials, administrative actions, external callers, and service-to-service boundaries.
2. **Model threats.** For each important flow, ask who could abuse it, what they could access/change, and what the impact would be. Prioritize realistic threats rather than trying to enumerate everything.
3. **Define identity and authorization.** Specify how users and workloads authenticate, what each role may do, and where authorization is enforced. Prefer least privilege and deny by default.
4. **Minimize and protect data.** Collect only required data; classify it; define encryption in transit/at rest, retention, deletion, masking, and access logging.
5. **Protect interfaces.** Validate inputs, bound payloads and work, apply rate/abuse limits, use safe output handling, and avoid leaking secrets or internal details in errors.
6. **Protect dependencies and runtime.** Pin and update dependencies, scan source/images, restrict workload permissions and network paths, and keep secrets out of source control and logs.
7. **Plan response and verification.** Identify security-relevant events, alerting, credential rotation/revocation, incident handling, and tests for authorization boundaries.

**Expected output:** assets/trust boundaries, a concise threat list with mitigations, authentication/authorization model, sensitive-data lifecycle, and security verification plan.

### Useful open-source security tools

| Tool | Useful for |
| --- | --- |
| [OWASP ZAP](https://www.zaproxy.org/) | Dynamic application security testing for web applications and APIs |
| [Trivy](https://trivy.dev/) | Scanning container images, filesystems, and configuration for known risks |
| [Gitleaks](https://github.com/gitleaks/gitleaks) | Detecting likely secrets committed to source repositories |
| [Semgrep](https://semgrep.dev/) | Static analysis and security rule checks across supported languages |
| [Kyverno](https://kyverno.io/) or [OPA Gatekeeper](https://open-policy-agent.github.io/gatekeeper/) | Kubernetes admission policy and configuration guardrails |

These tools help find classes of problems; they do not replace threat modeling, review, secure configuration, or incident response.

## 5. Testing and Software Delivery

Design is incomplete if the team cannot verify behavior and deliver changes safely. Test at the boundaries where contracts, state, and infrastructure can fail.

### Testing strategy

- **Unit tests:** domain rules, calculations, state transitions, and error cases without external services.
- **Integration tests:** real database, broker, or storage behavior; migrations; constraints; and transaction semantics.
- **Contract tests:** provider/consumer agreement for APIs and events.
- **End-to-end tests:** a small number of important user journeys through deployed components.
- **Load and resilience tests:** expected and peak load, saturation behavior, dependency timeouts, retries, and recovery.
- **Security tests:** authorization boundaries, input handling, dependency/image scanning, and secret detection.

Choose tests by risk; a larger test count is not automatically better. Make tests repeatable, isolate external side effects, and avoid relying only on mocks for database or distributed-system guarantees.

### Delivery design checklist

- Build once and promote the same immutable artifact through environments.
- Automate formatting, linting, tests, dependency checks, and image/config scans.
- Use reviewed infrastructure and schema changes; make rollout order compatible with old and new application versions.
- Define progressive rollout and rollback mechanisms appropriate to risk (for example canary or blue/green).
- Keep deployment credentials least-privileged; separate build and runtime identities.
- Record artifact provenance and deployment metadata where required.
- Make failure visible and provide a safe way to halt or reverse a rollout.

### Useful open-source testing and delivery tools

| Tool | Useful for |
| --- | --- |
| [GitHub Actions](https://github.com/features/actions), [Jenkins](https://www.jenkins.io/), or [Tekton](https://tekton.dev/) | Automating build, test, and delivery workflows |
| [SonarQube Community Build](https://www.sonarsource.com/products/sonarqube/) | Static analysis and maintainability feedback |
| [Renovate](https://github.com/renovatebot/renovate) | Automating dependency update proposals |
| [Trivy](https://trivy.dev/) | Container and configuration scanning in a build pipeline |
| [k6](https://k6.io/) | Load and performance checks |
| [Testcontainers](https://testcontainers.com/) | Integration tests against real dependency containers |

Select CI/CD tooling that fits the source host, deployment target, and team operations; avoid running multiple tools that solve the same problem without a clear reason.

## 6. Reliability, Operations, and Cost

The design should account for how the system is measured, supported, recovered, and paid for after launch.

### Reliability and operations

1. **Set service objectives.** Define user-visible SLIs such as request success, latency percentiles, freshness, or job completion time; set achievable SLO targets.
2. **Budget latency and failures.** Set timeouts across service hops; use bounded retries with backoff/jitter only for retryable operations; prevent retry amplification.
3. **Bound work.** Apply concurrency limits, queue bounds, rate limits, payload limits, and backpressure. Define what degrades when capacity is exhausted.
4. **Observe outcomes.** Use metrics for trends/alerts, structured logs for diagnosis, and traces for cross-service latency. Avoid high-cardinality or sensitive telemetry.
5. **Recover deliberately.** Define backup/restore, recovery point/time objectives, regional or zone failure assumptions, and practice restore/failover procedures.
6. **Prepare operations.** Document ownership, dashboards, alerts, runbooks, escalation paths, and safe operational actions.

### Cost and sustainability

- Estimate the major cost drivers: compute, storage, network/egress, managed services, observability retention, and support/operational effort.
- Relate capacity to workload and peak headroom; distinguish fixed baseline from variable cost.
- Set budgets or alerts and review actual usage after launch.
- Use caching, batching, autoscaling, retention policies, and right-sizing only when they preserve required correctness and latency.
- Include human operational complexity in trade-offs; the cheapest infrastructure bill may not be the lowest total cost.

**Expected output:** service objectives, key failure behavior, recovery plan, operational ownership, and an approximate cost-driver model with assumptions.

## 7. Open-Source Tools and Frameworks to Know

Know the problem a tool solves and its operational trade-offs; familiarity does not mean every project should use it. Check current maintenance, license, security posture, and ecosystem fit before adopting any dependency.

### API design and testing

| Tool | Useful for |
| --- | --- |
| [OpenAPI](https://www.openapis.org/) | Describing HTTP APIs, generating documentation/clients, and validating contract conventions |
| [Swagger UI](https://swagger.io/tools/swagger-ui/) | Exploring an OpenAPI contract interactively |
| [AsyncAPI](https://www.asyncapi.com/) | Describing event-driven and message-based APIs |
| [Buf](https://buf.build/) | Protobuf linting, breaking-change checks, and schema workflows for gRPC |
| [Bruno](https://www.usebruno.com/) | Version-controllable API request collections and manual checks |
| [Schemathesis](https://schemathesis.readthedocs.io/) | Generating API tests from OpenAPI schemas |

### Databases, migrations, and local development

| Tool | Useful for |
| --- | --- |
| [PostgreSQL](https://www.postgresql.org/) | Relational data, constraints, SQL, and transactional workflows |
| [SQLite](https://www.sqlite.org/) | Embedded/local applications, prototypes, and lightweight test setups |
| [Redis](https://redis.io/) | Caching, ephemeral coordination, and selected low-latency data structures; understand persistence and failure semantics before using it as authoritative data |
| [Flyway](https://flywaydb.org/) or [Liquibase](https://www.liquibase.com/) | Versioned database schema migrations |
| [Testcontainers](https://testcontainers.com/) | Running real dependency containers for integration tests |
| [pgTAP](https://pgtap.org/) | Testing PostgreSQL database behavior and schema constraints |

### Kubernetes and infrastructure

| Tool | Useful for |
| --- | --- |
| [Kubernetes](https://kubernetes.io/) | Container orchestration and workload lifecycle management |
| [Helm](https://helm.sh/) | Packaging and templating Kubernetes applications |
| [Kustomize](https://kustomize.io/) | Composing environment-specific Kubernetes manifests |
| [kind](https://kind.sigs.k8s.io/) or [minikube](https://minikube.sigs.k8s.io/) | Local Kubernetes development and integration testing |
| [OpenTofu](https://opentofu.org/) | Open-source infrastructure as code |
| [Argo CD](https://argo-cd.readthedocs.io/) | GitOps-based continuous delivery to Kubernetes |

### Observability, messaging, and reliability

| Tool | Useful for |
| --- | --- |
| [OpenTelemetry](https://opentelemetry.io/) | Vendor-neutral instrumentation and collection of traces, metrics, and logs |
| [Prometheus](https://prometheus.io/) and [Grafana](https://grafana.com/oss/) | Metrics collection, alerting, and dashboards |
| [Jaeger](https://www.jaegertracing.io/) | Distributed trace collection and investigation |
| [Loki](https://grafana.com/oss/loki/) | Aggregated log storage and querying |
| [Apache Kafka](https://kafka.apache.org/) | Durable high-throughput event streaming where partitioning and replay are useful |
| [NATS](https://nats.io/) | Lightweight messaging and service communication options |
| [k6](https://k6.io/) | Scripted load and performance testing |
| [Pact](https://pact.io/) | Consumer-driven contract testing between services |

### Application frameworks

Choose a framework based on language, team expertise, ecosystem, and deployment needs; keep business rules independent from framework-specific APIs where practical.

| Framework | Ecosystem | Often useful for |
| --- | --- | --- |
| [Spring Boot](https://spring.io/projects/spring-boot) | Java | Service applications with a broad dependency-injection and integration ecosystem |
| [Gin](https://gin-gonic.com/) | Go | Lightweight HTTP services |
| [FastAPI](https://fastapi.tiangolo.com/) | Python | Typed HTTP APIs with generated OpenAPI documentation |
| [ASP.NET Core](https://dotnet.microsoft.com/en-us/apps/aspnet) | .NET | HTTP APIs and service applications in the .NET ecosystem |
| [NestJS](https://nestjs.com/) | TypeScript/Node.js | Structured modular server applications |

## 8. A Practical Starter Stack

For a small service, a reasonable starting point might be:

- One application framework already familiar to the team.
- OpenAPI for HTTP contracts.
- PostgreSQL for durable relational data when transactions and constraints fit.
- A migration tool and automated integration tests.
- Structured logs, metrics, and traces using OpenTelemetry-compatible instrumentation.
- Containers for packaging; Kubernetes only if its operational benefits justify the cost.

This is an example, not a universal recommendation. A static site, command-line tool, embedded application, high-volume stream processor, or strongly regulated system may need a very different stack.

## 9. Design Review Checklist

- Does every technology choice map to a requirement, access pattern, or operational need?
- Are API schemas, errors, retries, and compatibility explicit?
- Are critical data invariants enforced at the authoritative boundary?
- Can concurrent updates or duplicate requests violate correctness?
- Are schema changes and deployments compatible during rollouts?
- Are health checks, resource bounds, shutdown, and rollback defined?
- Are trust boundaries, data protection, and authorization explicit?
- Are testing, deployment, recovery, and service ownership designed?
- Are major cost drivers understood and monitored?
- Can the team observe, back up, restore, secure, and operate the system?
- Is a simpler design sufficient today, with a credible evolution path later?
