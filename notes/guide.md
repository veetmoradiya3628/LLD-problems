# 🧠 System Design Practice Guide (The Thinking Phase)

## Phase 1: Requirement Gathering (Timebox: 5-7 mins)
*Don't assume anything. Ask yourself these questions to box in the problem.*
- [ ] **Who is the user?** (Consumers, internal admins, IoT devices?)
- [ ] **What are the top 3 critical features?** (If I only build 3 things, what must work?)
- [ ] **What is explicitly OUT of scope?** (Cut features aggressively to save time.)
- [ ] **What is the read/write ratio?** (Is it read-heavy like YouTube, or write-heavy like a metrics logger?)

## Phase 2: Back-of-the-Envelope Math (Timebox: 5 mins)
*Only calculate what actually impacts the architecture.*
- [ ] **Traffic:** What is the Daily Active Users (DAU)? Requests per second (RPS)?
- [ ] **Storage:** How much data is generated per day? Per 5 years? (Guides DB choice).
- [ ] **Network:** What is the peak bandwidth? (Important for streaming or heavy file uploads).

## Phase 3: High-Level Design / HLD (Timebox: 15 mins)
*Think in macro-components. Focus on the flow of data.*
- [ ] **API Contracts:** What are the 2-3 main endpoints? (REST/gRPC/GraphQL). What goes in, what comes out?
- [ ] **The "Happy Path":** Trace a user request from the client -> Load Balancer -> Gateway -> App Server -> Database.
- [ ] **Data Stores:** SQL or NoSQL? Why? Do I need a blob store (S3) for images/videos?
- [ ] **Caching:** Where can I cache data to save DB hits? (CDN for static, Redis for dynamic).

## Phase 4: Low-Level Design / LLD (Timebox: 15 mins)
*Zoom into the hardest part of the system.*
- [ ] **Database Schema:** What are the main tables? Primary keys? Foreign keys? Indexes?
- [ ] **Core Logic/Algorithm:** Is there a complex piece of logic? (e.g., Ticket booking lock, geo-spatial search, ranking algorithm). Draft the steps.
- [ ] **Data Structures:** Does a specific feature require a Trie (typeahead), a Graph (social network), or a Heap (top-K elements)?

## Phase 5: Bottlenecks & Trade-offs (Timebox: 5 mins)
*Break your own system before someone else does.*
- [ ] **Single Points of Failure (SPOF):** What happens if the database dies? (Need replicas).
- [ ] **Scaling:** What happens when traffic spikes 10x? (Autoscaling, sharding).
- [ ] **Trade-offs:** Why did I choose X over Y? (e.g., "I chose eventual consistency over strong consistency for lower latency").
