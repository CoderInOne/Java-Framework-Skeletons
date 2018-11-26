package cache;

import com.github.benmanes.caffeine.base.UnsafeAccess;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakNode<K, V> implements Node<K, V> {
    /**
     * why Unsafe to set and get value and key?
     * because weak reference can only get, can not change
     */
    private final long KEY_OFFSET = UnsafeAccess.objectFieldOffset(WeakNode.class, "key");
    private final long VALUE_OFFSET = UnsafeAccess.objectFieldOffset(WeakNode.class, "value");
    private Reference<K> key;
    private Reference<V> value;
    private ReferenceQueue<K> keyRererenceQueue;

    long createTime;
    long accessTime;
    long writeTime;

    public WeakNode(K key, V value, long now, ReferenceQueue<K> referenceQueue) {
        this.createTime = now;
        this.writeTime = now;
        this.keyRererenceQueue = referenceQueue;

        this.key = new KeyWeakReference<>(key, referenceQueue);
        this.value = new WeakReference<V>(value);
    }

    @Override
    public K getKey() {
        return ((Reference<K>) UnsafeAccess.UNSAFE.getObject(this, KEY_OFFSET)).get();
    }

    @Override
    public Object keyRef() {
        return key;
    }

    @Override
    public V getValue() {
        return value.get();
    }

    @Override
    public void setValue(V v) {
        UnsafeAccess.UNSAFE.putObject(this, VALUE_OFFSET, this.value);
    }

    @Override
    public long getWriteTime() {
        return 0;
    }

    @Override
    public void setWriteTime(long now) {

    }

    @Override
    public long getAccessTime() {
        return 0;
    }

    @Override
    public void setAccessTime(long now) {

    }
}
