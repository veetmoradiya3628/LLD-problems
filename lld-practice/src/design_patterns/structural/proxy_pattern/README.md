The Proxy Design Pattern is a structural pattern that provides a placeholder or surrogate for another object, allowing you to control access to it.

Use cases:
- Delay creation or loading until it’s actually needed (lazy access).
- Restrict or control access (authentication, authorization, rate limiting).
- Add cross-cutting behavior like logging, caching, retries, or monitoring without changing the original class.

The proxy sits between the client and the real object, intercepting calls and deciding whether to forward them as-is, block them, or wrap them with extra behavior.

Participants
- Subject
- RealSubject
- Proxy
- Client

Type of Proxies
- Virtual Proxy - Defers creation of the real object until it’s actually needed (lazy loading).
- Protection Proxy - Performs permission checks before allowing access to certain operations.
- Remote Proxy - Handles communication between local and remote objects over a network.
- Caching Proxy - Caches expensive results and avoids repeated calls to the real subject.
- Smart Proxy - Adds logging, reference counting, or monitoring before/after method calls.

