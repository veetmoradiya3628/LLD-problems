package design_patterns.proxy_pattern;

import java.util.HashMap;
import java.util.Map;

interface DatabaseService {
    String query(String sql);
}

class RealDatabaseService implements DatabaseService {
    @Override
    public String query(String sql) {
        System.out.println("RealDatabase: Executing query: " + sql);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Result for [" + sql + "]";
    }
}

class CachingDatabaseProxy implements DatabaseService {
    private final RealDatabaseService realService;
    private final Map<String, String> cache = new HashMap<>();

    public CachingDatabaseProxy() {
        this.realService = new RealDatabaseService();
    }

    @Override
    public String query(String sql) {
        if (cache.containsKey(sql)) {
            System.out.println("CachingProxy: Cache HIT for: " + sql);
            return cache.get(sql);
        }
        System.out.println("CachingProxy: Cache MISS for: " + sql);
        String result = realService.query(sql);
        cache.put(sql, result);
        return result;
    }

    public void clearCache() {
        System.out.println("CachingProxy: Cache cleared.");
        cache.clear();
    }
}

public class DatabaseQueryPatternDemo {
    public static void main(String[] args) {
        CachingDatabaseProxy db = new CachingDatabaseProxy();

        System.out.println("--- First query (cache miss) ---");
        System.out.println(db.query("SELECT * FROM users"));

        System.out.println("\n--- Same query again (cache hit) ---");
        System.out.println(db.query("SELECT * FROM users"));

        System.out.println("\n--- Different query (cache miss) ---");
        System.out.println(db.query("SELECT * FROM orders WHERE status = 'pending'"));

        System.out.println("\n--- Clear cache and retry ---");
        db.clearCache();
        System.out.println(db.query("SELECT * FROM users"));
    }
}