package cache;

import com.github.benmanes.caffeine.cache.*;
import com.github.benmanes.caffeine.cache.Cache;
import org.junit.After;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * https://www.baeldung.com/java-caching-caffeine
 *
 * Design Pattern
 * https://bowenli86.github.io/2016/04/14/system%20design/cache/Cache-Design-a-cache-with-inspiration-from-Guava-Cache/
 *
 * Reference
 * http://learningviacode.blogspot.com/2014/02/reference-queues.html
 */
public class CaffeineBasicTest {
    @Test
    public void invalidate() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .build(k -> DataObject.get(k));
        cache.get("A");
        cache.get("A");
        assertEquals(1, DataObject.getCounter());
        cache.invalidate("A");
        cache.get("A");
        cache.get("A");
        assertEquals(2, DataObject.getCounter());
    }

    @Test
    public void get() {
        Cache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(10)
                .build();

        String key = "A";
        DataObject dataObject = cache.getIfPresent(key);
        assertNull(dataObject);

        dataObject = cache.get(key, k -> DataObject.get("Data for " + k));
        assertEquals("Data for A", dataObject.getData());

        cache.invalidate(key);
        assertNull(cache.getIfPresent(key));
    }

    @Test
    public void syncLoading() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(10)
                .build(k -> {
                    return DataObject.get("Data for " + k);
                });

        DataObject dataObject = cache.get("A");
        assertEquals("Data for A", dataObject.getData());

        Map<String, DataObject> resMap = cache.getAll(Arrays.asList("B", "C", "D"));
        assertEquals(3, resMap.size());
    }

    @Test(timeout = 1000)
    public void asyncLoading() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        AsyncLoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .maximumSize(10)
                .buildAsync(k -> DataObject.get("Data for " + k));

        cache.get("A")
                .thenAccept(dataObject -> {
                    assertEquals("Data for A", dataObject.getData());
                    latch.countDown();
                });

        latch.await();
    }

    @Test
    public void evict() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .maximumSize(1)
                .build(k -> DataObject.get("Data for " + k));

        DataObject dataObject = cache.get("A");
        assertEquals("Data for A", dataObject.getData());

        cache.get("B");
        // clean process is async
        cache.cleanUp();
        assertEquals(1, cache.estimatedSize());
    }

    @Test
    public void evictByWeigher() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .maximumWeight(10)
                .weigher((k , v) -> 5)
                .build(k -> DataObject.get("Data for " + k));

        DataObject dataObject = cache.get("A");
        assertEquals("Data for A", dataObject.getData());

        cache.get("A");
        cache.get("B");
        // clean process is async
        cache.cleanUp();
        assertEquals(2, cache.estimatedSize());
    }

    @Test
    public void expireRead() throws InterruptedException {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .build(k -> DataObject.get("Data for " + k));

        cache.get("A");
        cache.get("A");
        cache.get("B");
        cache.get("B");

        assertEquals(2, DataObject.getCounter());

        Thread.sleep(6000);

        cache.get("A");
        cache.get("B");

        assertEquals(4, DataObject.getCounter());
    }

    @Test
    public void refreshAfterWrite() throws InterruptedException {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .refreshAfterWrite(2, TimeUnit.SECONDS)
                .build(k -> DataObject.get("Data for " + k));

        System.out.println(cache.get("A"));

        Thread.sleep(2000);

        System.out.println(cache.get("A"));

        assertEquals(2, DataObject.getCounter());

        Thread.sleep(500);

        System.out.println(cache.get("A"));
    }

    @Test
    public void weakRefs() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .weakKeys()
                .weakValues()
                .build(k -> DataObject.get("Data for " + k));

        assertEquals("Data for B", cache.get("B").getData());
        assertEquals("Data for A", cache.get("A").getData());
        Runtime.getRuntime().gc();
        assertEquals("Data for A", cache.get("A").getData());
    }

    @Test
    public void stats() {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .maximumSize(10)
                .recordStats()
                .build(k -> DataObject.get(k));

        cache.get("A");
        cache.get("A");

        assertEquals(1, cache.stats().hitCount());
        assertEquals(1, cache.stats().missCount());
    }

    @Test
    public void evictionPolicy() throws InterruptedException {
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .maximumSize(3)
                .removalListener(new RemovalListener<String, DataObject>() {
                    @Override
                    public void onRemoval(String key, DataObject value, RemovalCause cause) {
                        System.out.println("removing key:" + key);
                    }
                })
                .build(DataObject::get);

        cache.get("A");
        cache.get("B");
        cache.get("C");
        cache.get("D");

        Thread.sleep(3000);

        System.out.println(cache.asMap());
    }

    @After
    public void tearDown() throws Exception {
        DataObject.init();
    }
}









