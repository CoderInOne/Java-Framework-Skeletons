package cache;

import com.google.testing.threadtester.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// TODO
public class ConcurrentSafetyTest {
    private CacheImpl<String, DataObject> cache;
    @ThreadedBefore
    public void before() {
//        cache = CacheBuilder.newBuilder()
//                .build(k -> DataObject.get("Data for " + k));
        CacheBuilder<String, DataObject> cacheBuilder = new CacheBuilder<>();

        cache = new CacheImpl<>(cacheBuilder, k -> DataObject.get("Data for " + k));
    }

    @ThreadedMain
    public void main() {
        cache.put("A", DataObject.get("New data for A"));
    }

    @ThreadedSecondary
    public void secondary() {
        cache.get("A");
        cache.invalidate("A");
    }

    @ThreadedAfter
    public void after() {
        assertEquals("New data for A", cache.getIfPresent("A").getData());
    }

    @Test
    public void test() {
        AnnotatedTestRunner runner = new AnnotatedTestRunner();
        runner.runTests(getClass(), CacheImpl.class);
    }
}
