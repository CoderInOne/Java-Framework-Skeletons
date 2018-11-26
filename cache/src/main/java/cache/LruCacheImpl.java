package cache;

import java.util.*;

/**
 * O(1) get & put
 */
public class LruCacheImpl<K, V> implements LruCache<K, V> {
    private int capacity;
    private Map<K, Node<K, V>> data;
    private Node<K, V> head;
    private Node<K, V> tail;

    private class Node<K, V> {
        K k;
        V v;
        Node<K, V> prev;
        Node<K, V> next;
    }

    public LruCacheImpl(int capacity) {
        this.capacity = capacity;
        this.data = new HashMap<>(capacity);
    }

    @Override
    public V get(K k) {
        Node<K, V> node = data.get(k);
        if (node != null) {
            remove(node);
            setHead(node);
        }
        return node == null ? null : node.v;
    }

    @Override
    public void put(K k, V v) {
        Node<K, V> n = data.get(k);
        if (n == null) {
            n = new Node<>();
            n.k = k;
            n.v = v;
            data.put(k, n);
        } else {
            remove(n);
            n.v = v;
        }
        setHead(n);
    }

    private void setHead(Node<K, V> n) {
        if (head != null) {
            n.next = head;
            head.prev = n;
        } else {
            tail = n;
        }

        this.head = n;

        // check size
        if (data.size() > capacity) {
            data.remove(tail.k);
            remove(tail);
        }
    }

    private void remove(Node<K, V> n) {
        Node<K, V> p = n.prev;
        if (p == null) {
            return;
        }
        if (n.next == null) {
            this.tail = n.prev;
            this.tail.next = null;
        } else {
            n.next.prev = n.prev;
        }

        n.prev.next = n.next;
    }

    @Override
    public boolean contains(K k) {
        return this.data.containsKey(k);
    }

    @Override
    public void clear() {
        data.clear();
        this.head = null;
        this.tail = null;
    }
}
