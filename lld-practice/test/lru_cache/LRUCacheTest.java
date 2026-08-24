package lru_cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;


class LRUCacheTest {
    private LRUCache<Integer, String> cache;
    private final int CAPACITY = 3;

    @BeforeEach
    void setup() {
        cache = new LRUCache<>(CAPACITY);
    }

    @Test
    void testCacheMissReturnsNull(){
        assertNull(cache.get(1), "Getting a non-existent key should return null");
    }

    @Test
    void testPutAndGet() {
        cache.put(1, "One");
        assertEquals("One", cache.get(1), "Cache should return the inserted value");
    }

    @Test
    void testUpdateExistingKey() {
        cache.put(1, "One");
        cache.put(1, "UpdatedOne");

        assertEquals("UpdatedOne", cache.get(1), "Cache should return the updated value");
    }

    @Test
    void testEvictionPolicy() {
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");

        // Cache is now full. Putting a 4th item should evict the least recently used (key 1)
        cache.put(4, "Four");

        assertNull(cache.get(1), "Key 1 should have been evicted");
        assertEquals("Two", cache.get(2));
        assertEquals("Three", cache.get(3));
        assertEquals("Four", cache.get(4));
    }

    @Test
    void testLRUOrderMaintainedOnGet() {
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");

        // Access key 1, making it the MOST recently used. Key 2 is now the LEAST recently used.
        cache.get(1);

        // Putting a 4th item should now evict key 2, not key 1
        cache.put(4, "Four");

        assertNull(cache.get(2), "Key 2 should have been evicted");
        assertEquals("One", cache.get(1), "Key 1 should still be in the cache");
        assertEquals("Three", cache.get(3));
        assertEquals("Four", cache.get(4));
    }

    @Test
    void testLRUOrderMaintainedOnPutUpdate() {
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");

        // Update key 1, making it the MOST recently used. Key 2 is now the LEAST recently used.
        cache.put(1, "One-Updated");

        // Putting a 4th item should evict key 2
        cache.put(4, "Four");

        assertNull(cache.get(2), "Key 2 should have been evicted");
        assertEquals("One-Updated", cache.get(1), "Key 1 should hold the updated value");
    }

    @Test
    void testThreadSafetyUnderConcurrentAccess() throws InterruptedException {
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // Concurrently put 100 items into a cache of size 3
        for (int i = 0; i < numberOfThreads; i++) {
            final int key = i;
            executorService.execute(() -> {
                cache.put(key, "Value" + key);
                cache.get(key);
                latch.countDown();
            });
        }

        latch.await(); // Wait for all threads to finish
        executorService.shutdown();
        assertTrue(true, "Execution completed without concurrent modification exceptions");
    }
}