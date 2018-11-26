package cache;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.sql.SQLOutput;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class CacheImpl<K, V> implements Cache<K, V> {
    private ConcurrentHashMap<Object, Node<K, V>> data;
    private CacheBuilder<K, V> param;
    private Function<? super K, V> mappingFunction;
    private NodeFactory nodeFactory;
    private StatsCounter statsCounter;

    private ReferenceQueue<K> keyReferenceQueue;
    private ReferenceQueue<V> valueReference;

    CacheImpl(CacheBuilder<K, V> param, Function<? super K, V> mappingFunction) {
        this.param = param;
        this.mappingFunction = mappingFunction;
        this.data = new ConcurrentHashMap<>(param.maxSize);
        this.nodeFactory = param.weakKeyValue ? NodeFactory.WEAK : NodeFactory.SIMPLE;
        this.statsCounter = new StatsCounter();

        this.keyReferenceQueue = param.weakKeyValue ? new ReferenceQueue<>() : null;
        this.valueReference = param.weakKeyValue ? new ReferenceQueue<>() : null;
    }

    @Override
    public V get(K k) {
        return doGet(k, true);
    }

    private V doGet(K k, boolean load) {
        Node<K, V> node = data.get(nodeFactory.newLookupKey(k));
        cleanUp();
        System.out.println("data size:" + data.size() + ", node:" + node);
        if (node != null) {
            V value = node.getValue();
            System.out.println("value:" + value);
            if (value != null && !hasExpired(node)) {
                node.setAccessTime(System.nanoTime());
                afterRead(node);
                return value;
            }
        }

        Object keyRef = nodeFactory.newReferenceKey(k, keyReferenceQueue);
        return load ? computeIfAbsent(k, keyRef, mappingFunction) : null;
    }

    private void afterRead(Node<K, V> n) {
        if (n != null && param.refreshAfterWriteTime > 0
                && (System.nanoTime() - n.getWriteTime()) > param.refreshAfterWriteTime) {
            // bad code: 1. no exceptional mechanism
            // 2. no clear define on async reload
            Object keyRef = nodeFactory.newReferenceKey(n.getKey(), new ReferenceQueue<>());
            CompletableFuture.runAsync(() -> computeIfAbsent(n.getKey(), keyRef, mappingFunction));
        }

        statsCounter.recordHits(1);
        cleanUp();
    }

    private boolean hasExpired(Node<K, V> node) {
        long now = System.nanoTime();
        return (param.expireAfterAccessTime > 0 && now - node.getAccessTime() > param.expireAfterAccessTime);
    }

    private void cleanUp() {
        if (keyReferenceQueue == null) {
            return;
        }
        CompletableFuture.runAsync(
                () -> {
                    while (true) {
                        Reference<? extends K> n = keyReferenceQueue.poll();
                        System.out.println("reference queue:" + n);
                        if (n == null) {
                            break;
                        }
                        K k = n.get();
                        if (k != null) {
                            data.remove(k);
                        }
                    }
                })
                .thenAccept(new Consumer<Void>() {
                    @Override
                    public void accept(Void aVoid) {
                        System.out.println("clean up finish! remaining data size:" + data.size());
                    }
                });
    }

    private V computeIfAbsent(K k, Object keyReference, Function<? super K, ? extends V> mappingFunction) {
        if (mappingFunction == null) {
            throw new IllegalStateException("You want me to load data, " +
                    "but didn't give me mapping function!");
        }

        // store value can be changed in inner class as well as returned
        @SuppressWarnings("unchecked")
        V[] oldValue = (V[]) new Object[1];
        @SuppressWarnings("unchecked")
        V[] newValue = (V[]) new Object[1];
        long now = System.nanoTime();

        Node<K, V> node = data.compute(keyReference,
                // when no such node mapping this key or key has expired
                (k1, n) -> {
                    if (n == null) {
                        newValue[0] = mappingFunction.apply(k);
                        if (newValue[0] == null) {
                            return null;
                        }

                        statsCounter.recordMisses(1);

                        return nodeFactory.newNode(k, newValue[0], now);
                    }

                    // update value
                    synchronized (n) {
                        newValue[0] = mappingFunction.apply(k);
                        n.setValue(newValue[0]);
                        n.setAccessTime(now);
                        n.setWriteTime(now);

                        statsCounter.recordMisses(1);
                    }

                    return n;
                });

        // post processors for read & write
        // node.setAccessTime(now);

        return newValue[0];
    }

    @Override
    public V getIfPresent(K k) {
        return doGet(k, false);
    }

    @Override
    public void put(K k, V v) {
        computeIfAbsent(k, nodeFactory.newReferenceKey(k, null), kk -> v);
    }

    @Override
    public void invalidate(K k) {
        data.remove(k);
    }

    @Override
    public void invalidateAll() {
        data.clear();
    }

    @Override
    public long size() {
        return data.size();
    }

    @Override
    public StatsCounter stats() {
        return this.statsCounter;
    }
}
