package cache;

public interface Cache<K, V> {
    V get(K k);

    /**
     * return value if key exists or null
     * @param k key
     * @return value
     */
    V getIfPresent(K k);

    /**
     * put value manually
     * @param k key
     * @param v value
     */
    void put(K k, V v);

    void invalidate(K k);

    void invalidateAll();

    long size();

    StatsCounter stats();
}
