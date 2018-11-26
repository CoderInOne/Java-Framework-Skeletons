package cache;

import java.lang.ref.ReferenceQueue;

/**
 * Factory to build node. For simplicity, node has two kinds:
 * - Simple node for strong reference
 * - Weak node for weak reference
 */
public enum NodeFactory {
    /**
     * This style is good for factory
     */
    SIMPLE {
        <K, V> Node<K, V> newNode(K key, V value, long now) {
            return new SimpleNode<>(key, value, now);
        }

        <K, V> Node<K, V> newNode(Object key, V value, long now, ReferenceQueue<K> keyRefQueue) {
            return new SimpleNode(key, value, now);
        }
    },

    WEAK {
        <K, V> Node<K, V> newNode(K key, V value, long now) {
            return new WeakNode<>(key, value, now, null);
        }

        <K, V> Node<K, V> newNode(Object key, V value, long now, ReferenceQueue<K> keyRefQueue) {
            return new WeakNode(key, value, now, keyRefQueue);
        }

        /**
         * check {@link com.github.benmanes.caffeine.cache.References}
         */
        <K> Object newLookupKey(K key) {
            return new KeyWeakReference<>(key, null);
        }

        <K> Object newReferenceKey(K key, ReferenceQueue<K> referenceQueue) {
            return new KeyWeakReference<>(key, referenceQueue);
        }
    };

    abstract <K, V> Node<K, V> newNode(K key, V value, long now);
    abstract <K, V> Node<K, V> newNode(Object key, V value, long now, ReferenceQueue<K> keyRefQueue);
    <K> Object newLookupKey(K key) {
        return key;
    }

    <K> Object newReferenceKey(K key, ReferenceQueue<K> referenceQueue) {
        return key;
    }
}
