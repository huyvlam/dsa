package myhash.probing.mono;

import myhash.HashUtil;
import myhash.probing.FlatNode;

import java.util.Objects;

public class DoubleMap<K, V> extends MonoFlatMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DOUBLE_LOAD_FACTOR = 0.75;

    public DoubleMap(int capacity, double factor) {
        super(capacity, factor);
    }

    public DoubleMap(int capacity) {
        this(capacity, DOUBLE_LOAD_FACTOR);
    }

    public DoubleMap() {
        this(DEFAULT_CAPACITY, DOUBLE_LOAD_FACTOR);
    }

    @Override
    public V remove(K key) {
        int origIndex = hashIndex(key);
        int gap = 1;
        int index = origIndex;
        int stride = HashUtil.stride(key); // Cache to avoid repetitive computing

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            if (!cur.deleted && Objects.equals(cur.key, key)) {
                V removed = cur.value;
                cur.deleted = true;
                cur.key = null;
                cur.value = null;

                size--;
                recordMetrics(gap, gap);

                return removed;
            }

            index = nextIndex(origIndex, gap, mask, stride);
            gap++;
        }
        recordMetrics(gap, gap);

        return null;
    }

    @Override
    public V get(K key) {
        int origIndex = hashIndex(key);
        int gap = 1;
        int index = origIndex;
        int stride = HashUtil.stride(key);

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            if (!cur.deleted && Objects.equals(cur.key, key)) {
                recordMetrics(gap, gap);
                return cur.value;
            }

            index = nextIndex(origIndex, gap, mask, stride);
            gap++;
        }
        recordMetrics(gap, gap);

        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int origIndex = hashIndex(key);
        int gap = 1;
        int index = origIndex;
        int stride = HashUtil.stride(key);

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            if (!cur.deleted && Objects.equals(cur.key, key)) {
                recordMetrics(gap, gap);
                return true;
            }

            index = nextIndex(origIndex, gap, mask, stride);
            gap++;
        }
        recordMetrics(gap, gap);

        return false;
    }

    @Override
    protected V probe(K key, V value, FlatNode<K, V>[] hashtable) {
        int htMask = hashtable.length - 1;
        int origIndex = HashUtil.hashIndex(key, htMask);
        int stride = HashUtil.stride(key);
        int gap = 1;

        int index = origIndex;
        int deletedIndex = -1;
        int deletedGap = -1;

        while (gap <= hashtable.length) {
            FlatNode<K, V> cur = hashtable[index];

            // Found empty slot
            if (cur == null) break;

            // Found the key
            if (!cur.deleted && Objects.equals(cur.key, key)) {
                V prevValue = cur.value;
                cur.value = value;
                recordMetrics(gap, gap);

                return prevValue;
            }

            // Found a tombstone for reuse
            if (cur.deleted && deletedIndex == -1) {
                deletedIndex = index;
                deletedGap = gap;
            }

            // Probe for next slot
            index = nextIndex(origIndex, gap, htMask, stride);
            gap++;
        }

        // Reuse the tombstone we found (Lazy Substitution)
        if (deletedIndex != -1) {
            FlatNode<K, V> reuse = hashtable[deletedIndex];
            reuse.key = key;
            reuse.value = value;
            reuse.deleted = false;
            recordMetrics(gap, deletedGap);

            return null;
        }

        // Use the slot if it is empty
        if (hashtable[index] == null) {
            hashtable[index] = new FlatNode<K, V>(key, value);
            recordMetrics(gap, gap);

            return null;
        }

        // All slots are checked but none available
        throw new IllegalStateException("Capacity is exceeded");
    }

    private int nextIndex(int orig, int gap, int mask, int stride) {
        return (orig + gap * stride) & mask;
    }

    @SuppressWarnings("unused")
    int nextIndex(int orig, int gap, int mask) {
        return 0;
    }
}
