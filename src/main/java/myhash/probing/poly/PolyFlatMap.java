package myhash.probing.poly;

import myhash.HashUtil;
import myhash.probing.FlatNode;

import java.util.Objects;
import java.util.function.Predicate;

public class PolyFlatMap<K, V> {
    public enum Probe { LINEAR, QUADRATIC, DOUBLE }
    private static final ProbeStrategy LINEAR = (orig, gap, mask, stride) -> (orig + gap) & mask;
    private static final ProbeStrategy QUADRATIC = (orig, gap, mask, stride) -> (orig + gap * gap) & mask;
    private static final ProbeStrategy DOUBLE =  (orig, gap, mask, stride) -> (orig + gap * stride) & mask;

    private final ProbeStrategy strategy;
    private FlatNode<K, V>[] table;
    private double threshold;
    private int mask;
    private int size;

    private long totalOperations;
    private long totalSearches;
    private long totalStorageDistance;

    private final int INIT_CAPACITY;
    private final double LOAD_FACTOR;

    private static final int DEFAULT_CAPACITY = 16;

    public PolyFlatMap(Probe probe, int capacity, double factor) {
        if (capacity < 0) throw new IllegalArgumentException("Illegal capacity: " + capacity);

        INIT_CAPACITY = HashUtil.tableSize(capacity);
        LOAD_FACTOR = factor > 0 ? factor : getLoadFactor(probe);
        this.strategy = getProbeStrategy(probe);
        table = new FlatNode[INIT_CAPACITY];
        threshold = table.length * LOAD_FACTOR;
        mask = table.length - 1;
        size = 0;
        totalSearches = 0;
        totalOperations = 0;
        totalStorageDistance = 0;
    }

    public PolyFlatMap(Probe probe, int capacity) {
        this(probe, capacity, 0);
    }

    public PolyFlatMap(Probe probe) {
        this(probe, DEFAULT_CAPACITY, 0);
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

    @SuppressWarnings("unchecked")
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
        final V[] removed = (V[]) new Object[]{null};

        probe(key, (node) -> {
            if (!node.deleted && Objects.equals(node.key, key)) {
                removed[0] = (V) node.value;
                node.key = null;
                node.value = null;
                node.deleted = true;

                size--;

                return false; // stopping the loop
            }
            return true;
        });

        return (V) removed[0];
    }

    public V get(K key) {
        final V[] result = (V[]) new Object[]{null};

        probe(key, (node) -> {
            if (!node.deleted && Objects.equals(node.key, key)) {
                result[0] = (V) node.value;
                return false;
            };
            return true;
        });

        return (V) result[0];
    }

    public boolean containsKey(K key) {
        final boolean[] found = {false};

        probe(key, (node) -> {
            if (!node.deleted && Objects.equals(node.key, key)) {
                found[0] = true;
                return false; // stop the loop
            }
            return true;
        });

        return found[0];
    }

    public boolean containsValue(V  value) {
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

    private <K, V> V probe(K key, V value, FlatNode<K, V>[] tab) {
        int tabMask = tab.length - 1;
        int origIndex = HashUtil.hash(key) & tabMask;
        int gap = 1;
        int stride = strategy == DOUBLE ? HashUtil.stride(key) : 0;

        int index = origIndex;
        int deletedIndex = -1;
        int deletedGap = -1;

        while (gap <= tab.length) {
            FlatNode<K, V> node = tab[index];

            // Found empty slot, stop
            if (node == null) break;

            // Found the key, replace data
            if (!node.deleted && Objects.equals(node.key, key)) {
                V prevValue = node.value;
                node.value = value;
                recordMetrics(gap, gap);

                return prevValue;
            }

            // Found a tombstone, save the slot for reuse
            if (node.deleted && deletedIndex == -1) {
                deletedIndex = index;
                deletedGap = gap;
            }

            // Probe for next slot
            index = strategy.nextIndex(origIndex, gap, tabMask, stride);
            gap++;
        }

        // Reuse the tombstone we found (Lazy Substitution)
        if (deletedIndex != -1) {
            FlatNode<K, V> reuse = tab[deletedIndex];
            reuse.key = key;
            reuse.value = value;
            reuse.deleted = false;
            recordMetrics(gap, deletedGap);

            return null;
        }

        // Use the slot if it is empty
        if (tab[index] == null) {
            tab[index] = new FlatNode<K, V>(key, value);
            recordMetrics(gap, gap);

            return null;
        }

        // All slots are checked but none available
        throw new IllegalStateException("Capacity is exceeded");
    }

    private <K, V> void probe(K key, Predicate<FlatNode<K, V>> action) {
        int origIndex = HashUtil.hash(key) & mask;
        int gap = 1;
        int stride = strategy == DOUBLE ? HashUtil.stride(key) : 0;
        int index = origIndex;

        while (table[index] != null && gap <= table.length) {
            if (!action.test((FlatNode<K, V>) table[index])) return;

            index = strategy.nextIndex(origIndex, gap, mask, stride);
            gap++;
        }
        recordMetrics(gap, gap);
    }

    private void recordMetrics(int searchGap, int storageGap) {
        totalSearches += searchGap;
        totalStorageDistance += storageGap;
        totalOperations++;
    }

    private static ProbeStrategy getProbeStrategy(Probe probe) {
        return switch (probe) {
            case LINEAR -> LINEAR;
            case QUADRATIC -> QUADRATIC;
            case DOUBLE -> DOUBLE;
            default -> throw new IllegalArgumentException("Unsupported probe strategy: " + probe);
        };
    }

    private static double getLoadFactor(Probe probe) {
        return switch (probe) {
            case LINEAR -> 0.7;
            case QUADRATIC -> 0.5;
            case DOUBLE -> 0.75;
            default -> throw new IllegalArgumentException("Unsupported probe strategy: " + probe);
        };
    }
}
