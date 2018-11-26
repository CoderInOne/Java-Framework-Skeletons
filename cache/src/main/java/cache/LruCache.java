package cache;

public interface LruCache<K, V> {
    V get(K k);
    void put(K k, V v);
    void clear();
    boolean contains(K k);
}
