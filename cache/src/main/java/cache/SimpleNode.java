package cache;

public class SimpleNode<K, V> implements Node<K, V> {
    K key;
    V value;
    long createTime;
    long accessTime;
    long writeTime;

    public SimpleNode(K key, V value, long now) {
        this.key = key;
        this.value = value;
        this.createTime = now;
        this.writeTime = now;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public Object keyRef() {
        return key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public void setValue(V v) {
        this.value = v;
    }

    @Override
    public long getWriteTime() {
        return this.writeTime;
    }

    @Override
    public void setWriteTime(long now) {
        this.writeTime = now;
    }

    @Override
    public long getAccessTime() {
        return this.accessTime == 0 ? this.createTime : this.accessTime;
    }

    @Override
    public void setAccessTime(long now) {
        this.accessTime = now;
    }

    @Override
    public String toString() {
        return "SimpleNode{" +
                "key=" + key +
                ", value=" + value +
                ", createTime=" + createTime +
                ", accessTime=" + accessTime +
                ", writeTime=" + writeTime +
                '}';
    }
}
