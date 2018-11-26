package cache;

import org.junit.Test;

import java.lang.ref.WeakReference;

import static org.junit.Assert.*;

public class KeyWeakReferenceTest {
    @Test
    public void referenceEquals() {
        System.out.println(new WeakReference<>("hello").equals(new WeakReference<>("hello")));
        System.out.println(new KeyWeakReference<>("hello").equals(new KeyWeakReference<>("hello")));
        System.out.println(new KeyWeakReference<>("hello").equals(new KeyWeakReference<>("hello1")));
    }
}