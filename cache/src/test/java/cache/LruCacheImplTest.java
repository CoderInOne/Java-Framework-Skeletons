package cache;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LruCacheImplTest {
    private LruCache<Integer, Integer> cache;
    private final static int SIZE = 3;

    @Before
    public void setUp() throws Exception {
        cache = new LruCacheImpl<>(SIZE);
    }

    @Test
    public void basic() {
        cache.put(3, 3);
        cache.put(2, 2);
        cache.put(1, 1);
        cache.put(0, 0);
        cache.put(-1, -1);
        assertEquals(0, cache.get(0).intValue());

        cache.clear();
        assertNull(cache.get(0));
    }

    @Test
    public void eviction() {
        for (int i = 0; i < 6; i++) {
            cache.put(i, i);
        }

        cache.contains(3);
        cache.contains(4);
        cache.contains(5);

        cache.get(4);
        cache.get(3);
        cache.put(6, 6);

        cache.contains(6);
        cache.contains(4);
        cache.contains(5);
    }
}