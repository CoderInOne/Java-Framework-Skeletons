package cache;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CacheBuilder<K, V> {
    int maxSize;
    long expireAfterAccessTime;
    /* -- private long writeExpiredTime; -- */
    long refreshAfterWriteTime;
    boolean weakKeyValue;
    // Function<? super K, ? extends V> mappingFunction;

    CacheBuilder() {}

    public static CacheBuilder<Object, Object> newBuilder() {
        return new CacheBuilder<Object, Object>();
    }

    public CacheBuilder<K, V> maxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit) {
        this.expireAfterAccessTime = unit.toNanos(duration);
        return this;
    }

    public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit) {
        this.refreshAfterWriteTime = unit.toNanos(duration);
        return this;
    }

    public CacheBuilder<K, V> weak() {
        this.weakKeyValue = true;
        return this;
    }

    public <K1 extends K, V1 extends V> Cache<K1, V1> build(Function<? super K1, V1> mappingFunction) {
        @SuppressWarnings("unchecked")
        CacheBuilder<K1, V1> self = (CacheBuilder<K1, V1>) this;
        /*
        should return different implementation based by config, such as bounded
        or unbounded
         */
        return new CacheImpl<>(self, mappingFunction);
    }

    public <K1 extends K, V1 extends V> Cache<K1, V1> buildAsync(Function<? super K1, V1> mappingFunction) {
        @SuppressWarnings("unchecked")
        CacheBuilder<K1, V1> self = (CacheBuilder<K1, V1>) this;
        /*
        should return different implementation based by config, such as bounded
        or unbounded
         */
        return new CacheImpl<>(self, mappingFunction);
    }

    public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
        @SuppressWarnings("unchecked")
        CacheBuilder<K1, V1> self = (CacheBuilder<K1, V1>) this;
        return new CacheImpl<>(self, null);
    }
}
