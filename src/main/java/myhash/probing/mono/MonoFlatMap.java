package myhash.probing.mono;

import myhash.HashUtil;
import myhash.probing.FlatNode;

import java.util.Objects;

public abstract class MonoFlatMap<K, V> {
    protected FlatNode<K, V>[] table;
    protected double threshold;
    protected int mask;
    protected int size;

    private long totalOperations;
    private long totalSearches;
    private long totalStorageDistance;

    protected final int INIT_CAPACITY;
    protected final double LOAD_FACTOR;

    abstract int nextIndex(int orig, int gap, int mask);

    public MonoFlatMap(int capacity, double factor) {
        if (capacity < 0) throw new IllegalArgumentException("Illegal capacity: " + capacity);

        INIT_CAPACITY = HashUtil.tableSize(capacity);
        LOAD_FACTOR = factor;
        table = new FlatNode[INIT_CAPACITY];
        threshold = table.length * LOAD_FACTOR;
        mask = table.length - 1;
        size = 0;
        totalSearches = 0;
        totalOperations = 0;
        totalStorageDistance = 0;
    }

    public void clear() {
        table = new FlatNode[INIT_CAPACITY];
        threshold = table.length * LOAD_FACTOR;
        mask = table.length - 1;
        size = 0;
        totalSearches = 0;
        totalOperations = 0;
        totalStorageDistance = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void resize() {
        int newCapacity = table.length * 2;
        FlatNode<K, V>[] newTable = (FlatNode<K, V>[]) new FlatNode[newCapacity];

        for (FlatNode<K, V> cur : table) {
            // Filter out deleted nodes for garbage collection (Tombstone Purging)
            if (cur != null && !cur.deleted) {
                probe(cur.key, cur.value, newTable);
            }
        }
        table = newTable;
        threshold = table.length * LOAD_FACTOR;
        mask = table.length - 1;
    }

    public V put(K key, V value) {
        if (size >= threshold) resize();

        V result = probe(key, value, table);
        if (result == null) size++;

        return result;
    }

    public V remove(K key) {
        int origIndex = indexFor(key);
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
                recordMetrics(gap, gap);

                return removed;
            }
            index = nextIndex(origIndex, gap, mask);
            gap++;
        }
        recordMetrics(gap, gap);

        return null;
    }

    public V get(K key) {
        int origIndex = indexFor(key);
        int gap = 1;
        int index = origIndex;

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            if (!cur.deleted && Objects.equals(cur.key, key)) {
                recordMetrics(gap, gap);
                return cur.value;
            }
            index = nextIndex(origIndex, gap, mask);
            gap++;
        }
        recordMetrics(gap, gap);

        return null;
    }

    public boolean containsKey(K key) {
        int origIndex = indexFor(key);
        int gap = 1;
        int index = origIndex;

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            if (!cur.deleted && Objects.equals(cur.key, key)) {
                recordMetrics(gap, gap);
                return true;
            }
            index = nextIndex(origIndex, gap, mask);
            gap++;
        }
        recordMetrics(gap, gap);

        return false;
    }

    public boolean containsValue(V value) {
        for (FlatNode<K, V> node : table) {
            if (node != null && !node.deleted && Objects.equals(node.value, value)) {
                return true;
            }
        }
        return false;
    }

    public double searchAverage() {
        return totalOperations == 0 ? 0 : (double) totalSearches / totalOperations;
    }

    public double storageAverage() {
        return totalOperations == 0 ? 0 : (double) totalStorageDistance / totalOperations;
    }

    protected V probe(K key, V value, FlatNode<K, V>[] hashtable) {
        int htMask = hashtable.length - 1;
        int origIndex = HashUtil.hashIndex(key, htMask);
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
            index = nextIndex(origIndex, gap, htMask);
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

    protected void recordMetrics(int searchGap, int storageGap) {
        totalSearches += searchGap;
        totalStorageDistance += storageGap;
        totalOperations++;
    }

    protected int indexFor(K key) {
        return HashUtil.hashIndex(key, mask);
    }
}
