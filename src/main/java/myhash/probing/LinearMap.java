package myhash.probing;

import myhash.HashUtil;

import java.util.Objects;

public class LinearMap<K, V> {
    private FlatNode<K, V>[] table;
    private double threshold;
    private int size;

    private long totalProbes;
    private long totalOperations;

    private final int INIT_CAPACITY;
    private final double LOAD_FACTOR;

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LINEAR_LOAD_FACTOR = 0.7;

    public LinearMap(int capacity, double factor) {
        if (capacity < 0) throw new IllegalArgumentException("Illegal capacity: " + capacity);

        INIT_CAPACITY = HashUtil.tableSize(capacity);
        LOAD_FACTOR = factor;
        table = (FlatNode<K, V>[]) new FlatNode[INIT_CAPACITY];
        threshold = table.length * LOAD_FACTOR;;
        size = 0;
        totalProbes = 0;
        totalOperations = 0;
    }

    public LinearMap(int capacity) {
        this(capacity, LINEAR_LOAD_FACTOR);
    }

    public LinearMap() {
        this(DEFAULT_CAPACITY, LINEAR_LOAD_FACTOR);
    }

    public void clear() {
        table = new FlatNode[INIT_CAPACITY];
        threshold = table.length * LOAD_FACTOR;;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public void resize() {
        int newCapacity = table.length * 2;
        FlatNode<K, V>[] newTable = (FlatNode<K, V>[]) new FlatNode[newCapacity];

        for (FlatNode<K, V> cur : table) {
            // Filter out deleted nodes for garbage collection (Tombstone Purging)
            if (cur != null && !cur.deleted) probe(cur.key, cur.value, newTable);
        }

        table = newTable;
        threshold = table.length * LOAD_FACTOR;;
    }

    public V put(K key, V value) {
        if (size >= threshold) resize();

        V result = probe(key, value, table);
        if (result == null) size++;

        return result;
    }

    public V remove(K key) {
        int origIndex = HashUtil.hashIndex(key, table.length);
        int gap = 1;
        int index = origIndex;

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            if (!cur.deleted && Objects.equals(cur.key, key)) {
                V removed = cur.value;
                cur.deleted = true;
                cur.key = null;
                cur.value = null;

                size--;
                recordProbes(gap);

                return removed;
            }

            index = FlatUtil.linearIndex(origIndex, gap, table.length);
            gap++;
        }
        recordProbes(gap);

        return null;
    }

    public V get(K key) {
        int origIndex = HashUtil.hashIndex(key, table.length);
        int gap = 1;
        int index = origIndex;

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            if (!cur.deleted && Objects.equals(cur.key, key)) {
                recordProbes(gap);
                return cur.value;
            }

            index = FlatUtil.linearIndex(origIndex, gap, table.length);
            gap++;
        }
        recordProbes(gap);

        return null;
    }

    public boolean containsKey(K key) {
        int origIndex = HashUtil.hashIndex(key, table.length);
        int gap = 1;
        int index = origIndex;

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            if (!cur.deleted && Objects.equals(cur.key, key)) {
                recordProbes(gap);
                return true;
            }

            index = FlatUtil.linearIndex(origIndex, gap, table.length);
            gap++;
        }
        recordProbes(gap);

        return false;
    }

    public boolean containsValue(V  value) {
        for (FlatNode<K, V> node : table) {
            if (node != null && !node.deleted && Objects.equals(node.value, value)) {
                return true;
            }
        }

        return false;
    }

    public double probeAverage() {
        return totalOperations == 0 ? 0 : (double) totalProbes / totalOperations;
    }

    private V probe(K key, V value, FlatNode<K, V>[] table) {
        int origIndex = HashUtil.hashIndex(key, table.length);
        int gap = 1;

        int index = origIndex;
        int deletedIndex = -1;

        while (gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            // Found empty slot
            if (cur == null) break;

            // Found the key
            if (!cur.deleted && Objects.equals(cur.key, key)) {
                V prevValue = cur.value;
                cur.value = value;
                recordProbes(gap);

                return prevValue;
            }

            // Found a tombstone for reuse
            if (cur.deleted && deletedIndex == -1) deletedIndex = index;

            // Probe for next slot
            index = FlatUtil.linearIndex(origIndex, gap, table.length);
            gap++;
        }
        recordProbes(gap);

        // Reuse the tombstone we found (Lazy Substitution)
        if (deletedIndex != -1) {
            FlatNode<K, V> reuse = table[deletedIndex];
            reuse.key = key;
            reuse.value = value;
            reuse.deleted = false;

            return null;
        }

        // Use the slot if it is empty
        if (table[index] == null) {
            table[index] = new FlatNode<K, V>(key, value);
            return null;
        }

        // All slots are checked but none available
        throw new IllegalStateException("Capacity is exceeded");
    }

    private void recordProbes(int gap) {
        totalProbes += gap;
        totalOperations++;
    }
}
