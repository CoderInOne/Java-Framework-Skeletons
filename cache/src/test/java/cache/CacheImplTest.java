package cache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CacheImplTest {
    @Before
    public void setUp() throws Exception {
        // reset getCounter to 0
        DataObject.init();
    }

    /* -- synchronized test -- */

    @Test
    public void getAndLoad() {
        Cache<String, DataObject> cache = CacheBuilder.newBuilder()
                .build(k -> DataObject.get("Data for " + k));

        assertEquals("Data for A", cache.get("A").getData());
    }

    @Test
    public void expireAndLoad() throws InterruptedException {
        Cache<String, DataObject> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(3, TimeUnit.SECONDS)
                .build(k -> DataObject.get("Data for " + k));

        cache.get("A");
        cache.get("A");
        cache.get("B");
        cache.get("B");

        assertEquals(2, DataObject.getCounter());

        Thread.sleep(3500);

        assertEquals("Data for A", cache.get("A").getData());
        assertEquals("Data for B", cache.get("B").getData());

        assertEquals(4, DataObject.getCounter());

        Thread.sleep(3500);

        cache.get("A");
        cache.get("A");
        cache.get("B");
        cache.get("B");

        assertEquals(6, DataObject.getCounter());
    }

    @Test
    public void getIfPresent() {
        Cache<String, DataObject> cache = CacheBuilder.newBuilder()
                .build(k -> DataObject.get("Data for " + k));

        assertEquals("Data for A", cache.get("A").getData());
        assertNull(cache.getIfPresent("B"));
    }

    @Test
    public void put() {
        Cache<String, DataObject> cacheWithoutMappingFunction = CacheBuilder.newBuilder()
                .build();

        cacheWithoutMappingFunction.put("B", DataObject.get("Data for B"));
        assertEquals("Data for B", cacheWithoutMappingFunction.getIfPresent("B").getData());

        Cache<String, DataObject> cacheWithMappingFunction = CacheBuilder.newBuilder()
                .build(k -> DataObject.get("Data for " + k));
        cacheWithMappingFunction.put("B", DataObject.get("Data for B"));
        assertEquals("Data for B", cacheWithMappingFunction.getIfPresent("B").getData());
    }

    @Test
    public void invalidate() {
        Cache<String, DataObject> cache = CacheBuilder.newBuilder()
                .build(k -> DataObject.get("Data for " + k));
        assertEquals("Data for A", cache.get("A").getData());

        cache.invalidate("A");
        assertEquals("Data for A", cache.get("A").getData());
        assertEquals(2, DataObject.getCounter());
    }

    @Test
    public void invalidateAll() {
        Cache<String, DataObject> cache = CacheBuilder.newBuilder()
                .build(k -> DataObject.get("Data for " + k));

        cache.get("A");
        cache.get("B");
        cache.get("C");

        cache.invalidateAll();

        cache.get("A");
        cache.get("B");
        cache.get("C");

        assertEquals(6, DataObject.getCounter());
    }

    @Test
    public void size() {
        Cache<String, DataObject> cache = CacheBuilder.newBuilder()
                .build(k -> DataObject.get("Data for " + k));

        cache.get("A");
        cache.get("B");

        assertEquals(2, cache.size());
    }

    @Test
    public void refresh() throws InterruptedException {
        @SuppressWarnings("unchecked")
        Function<String, DataObject> reloadFunction = Mockito.mock(Function.class);
        Cache<String, DataObject> cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(2, TimeUnit.SECONDS)
                // mapping function as reload function
                .build(reloadFunction);

        when(reloadFunction.apply("A"))
                .thenReturn(DataObject.get("Data for A1"))
                .thenReturn(DataObject.get("Data for A2"));

        assertEquals("Data for A1", cache.get("A").getData());

        Thread.sleep(2000);

        // detect stale value for key A, return old value first
        // and trigger async reload data for A
        assertEquals("Data for A1", cache.get("A").getData());

        Thread.sleep(500);
        // data for key A is refreshed, so will return new data
        assertEquals("Data for A2", cache.get("A").getData());

        verify(reloadFunction, times(2)).apply("A");
    }

    @Test
    public void refreshButFail() throws InterruptedException {
        @SuppressWarnings("unchecked")
        Function<String, DataObject> reloadFunction = Mockito.mock(Function.class);
        Cache<String, DataObject> cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(2, TimeUnit.SECONDS)
                // mapping function as reload function
                .build(reloadFunction);


        when(reloadFunction.apply("A"))
                .thenReturn(DataObject.get("Data for A1"))
                .thenThrow(new RuntimeException());

        assertEquals("Data for A1", cache.get("A").getData());

        Thread.sleep(2000);

        // detect stale value for key A, return old value first
        // and trigger async reload data for A
        assertEquals("Data for A1", cache.get("A").getData());

        Thread.sleep(500);
        // data for key A is refreshed, so will return new data
        assertEquals("Data for A1", cache.get("A").getData());

        verify(reloadFunction, times(3)).apply("A");
    }

    @Test
    public void async() {
        fail();
    }

    @Test
    public void maxSize() {
        fail();
    }

    @Test
    public void stats() {
        Cache<String, DataObject> cache = CacheBuilder.newBuilder()
                .maxSize(10)
                .build(DataObject::get);

        cache.get("A");
        cache.get("A");

        assertEquals(1, cache.stats().getHitsCount());
        assertEquals(1, cache.stats().getMissesCount());
    }

    @Test
    public void weak() throws InterruptedException {
        Cache<String, DataObject> cache = CacheBuilder.newBuilder()
                .maxSize(10)
                .weak()
                .build(k -> DataObject.get("Data for " + k));

        DataObject ref = cache.get("A");
        ref = null;

        System.gc();
        Thread.sleep(3000);

        assertEquals("Data for A", cache.get("A").getData());
    }
}