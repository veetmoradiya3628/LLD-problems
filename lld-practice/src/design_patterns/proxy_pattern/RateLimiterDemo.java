package design_patterns.proxy_pattern;

import java.util.ArrayList;
import java.util.List;

interface ApiService {
    String request(String endpoint);
}

class RealApiService implements ApiService {
    @Override
    public String request(String endpoint) {
        return "Response from " + endpoint;
    }
}

class RateLimitingProxy implements ApiService {
    private final ApiService realApiService;
    private final List<Long> timestamps = new ArrayList<>();
    private static final int MAX_REQUESTS = 3;
    private static final long TIME_WINDOW_MS = 10_000;

    public RateLimitingProxy(ApiService service) {
        this.realApiService = service;
    }

    @Override
    public String request(String endpoint) {
        long now = System.currentTimeMillis();
        timestamps.removeIf(t -> now - t > TIME_WINDOW_MS);
        if (timestamps.size() >= MAX_REQUESTS) {
            return "Rate limit exceeded. Try again later.";
        }
        timestamps.add(now);
        return realApiService.request(endpoint);
    }
}

public class RateLimiterDemo {
    public static void main(String[] args) {
        ApiService api = new RealApiService(); // Replace with RateLimitingProxy

        System.out.println(api.request("/users"));
        System.out.println(api.request("/orders"));
        System.out.println(api.request("/products"));
        System.out.println(api.request("/inventory")); // Should be rejected

        // After waiting 10 seconds, requests should work again
    }
}
