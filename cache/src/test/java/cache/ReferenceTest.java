package cache;

import org.junit.Test;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ReferenceTest {
    @Test
    public void jvmWeakRefs() throws InterruptedException {
        WeakHashMap<UniqueKey, BigImage> map = new WeakHashMap<>();

        UniqueKey key = new UniqueKey("abc");
        map.put(key, new BigImage());
        BigImage value = map.get(key);
        assertNotNull(value);

        key = null;
        System.gc();

        Thread.sleep(1000);
        assertTrue(map.isEmpty());
    }

    @Test
    public void jvmStrongRefs() {
        HashMap<UniqueKey, BigImage> map = new HashMap<>();

        UniqueKey key = new UniqueKey("abc");
        map.put(key, new BigImage());
        // System.out.println("image exists:" + (map.get(key) != null));

        key = null;
        System.out.println(map.get(new UniqueKey("abc")));
        System.out.println(map.get(null));
        System.out.println(map.toString());
        System.out.println(map.size());

        /// await().atMost(10, TimeUnit.SECONDS).until(map::isEmpty);
    }

    @Test
    public void refQueue() throws InterruptedException {
        // strong reference
        BigImage bigImage = new BigImage();
        ReferenceQueue<BigImage> refQueue = new ReferenceQueue<>();
        WeakReference<BigImage> refBigImage = new WeakReference<>(bigImage, refQueue);

        Runtime.getRuntime().gc();

        assertNull(refQueue.poll());

        bigImage = null;
        Runtime.getRuntime().gc();

        assertSame(refBigImage, refQueue.remove());

        assertNull(refBigImage.get());

        assertFalse(refBigImage.isEnqueued());
    }

    @Test
    public void referenceCastToObject() {
        WeakReference<String> ref = new WeakReference<>("hello");
        System.out.println(ref);
        // System.out.println((String) ref);
    }
}
