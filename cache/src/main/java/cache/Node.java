package cache;

/**
 * Node stores key, value and access/write time
 * Once created key should not be changed
 * @param <K>
 * @param <V>
 */
public interface Node<K, V> {
    K getKey();

    Object keyRef();

    V getValue();

    void setValue(V v);

    long getWriteTime();

    void setWriteTime(long now);

    long getAccessTime();

    void setAccessTime(long now);
}
