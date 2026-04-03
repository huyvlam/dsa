package myhash.probing;

import java.util.Objects;

public class FlatMap<K, V> {
    public enum Probe { LINEAR, QUADRATIC, DOUBLE }

    private FlatNode<K, V>[] table;
    private double resizeThreshold;
    private int size;

    // track performance records of each strategy
    private long totalProbes;
    private long totalOperations;

    private final ProbeStrategy strategy;
    private final int INIT_CAPACITY;
    private final double LOAD_FACTOR;

    private static final int DEFAULT_CAPACITY = 16;

    public FlatMap(Probe probe, int capacity, double factor) {
        if (capacity <= 0 || (capacity & (capacity - 1)) != 0) throw new IllegalArgumentException("Capacity must be a power of 2");

        INIT_CAPACITY = capacity;
        LOAD_FACTOR = factor > 0 ? factor : getLoadFactor(probe);
        this.strategy = getProbeStrategy(probe);
        table = new FlatNode[INIT_CAPACITY];
        resizeThreshold = table.length * LOAD_FACTOR;
        size = 0;
        totalProbes = 0;
        totalOperations = 0;
    }

    public FlatMap(Probe probe, int capacity) {
        this(probe, capacity, 0);
    }

    public FlatMap(Probe probe) {
        this(probe, DEFAULT_CAPACITY, 0);
    }

    public void clear() {
        table = new FlatNode[INIT_CAPACITY];
        resizeThreshold = table.length * LOAD_FACTOR;
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

        int[] gapTracker = new int[1];

        for (FlatNode<K, V> cur : table) {
            // Filter out deleted nodes for garbage collection (Tombstone Purging)
            if (cur != null && !cur.deleted) {
                FlatUtil.probe(cur.key, cur.value, newTable, strategy, gapTracker);
            }
        }

        table = newTable;
        resizeThreshold = table.length * LOAD_FACTOR;

        totalOperations++;
        totalProbes += gapTracker[0];
    }

    public V put(K key, V value) {
        if (size >= resizeThreshold) resize();

        int[] gapTracker = new int[1];
        V result = FlatUtil.probe(key, value, table, strategy, gapTracker);

        if (result == null) size++;

        totalOperations++;
        totalProbes += gapTracker[0];

        return result;
    }

    public V remove(K key) {
        final V[] removed = (V[]) new Object[]{null};
        int[] gapTracker = new int[1];

        FlatUtil.probe(key, table, strategy, (node) -> {
            if (!node.deleted && Objects.equals(node.key, key)) {
                removed[0] = node.value;
                node.key = null;
                node.value = null;
                node.deleted = true;
                size--;

                return false; // stopping the loop
            }
            return true;
        }, gapTracker);

        totalOperations++;
        totalProbes += gapTracker[0];

        return (V) removed[0];
    }

    public V get(K key) {
        final V[] result = (V[]) new Object[]{null};
        int[] gapTracker = new int[1];

        FlatUtil.probe(key, table, strategy, (node) -> {
            if (!node.deleted && Objects.equals(node.key, key)) {
                result[0] = node.value;
                return false;
            };
            return true;
        }, gapTracker);

        totalOperations++;
        totalProbes += gapTracker[0];

        return (V) result[0];
    }

    public boolean containsKey(K key) {
        final boolean[] found = {false};
        int[] gapTracker = new int[1];

        FlatUtil.probe(key, table, strategy, (node) -> {
            if (!node.deleted && Objects.equals(node.key, key)) {
                found[0] = true;
                return false; // stopping the loop
            }
            return true;
        }, gapTracker);

        totalOperations++;
        totalProbes += gapTracker[0];

        return found[0];
    }

    public boolean containsValue(V  value) {
        for (FlatNode<K, V> node : table) {
            if (node != null && !node.deleted && Objects.equals(node.value, value)) return true;
        }

        return false;
    }

    public double averageProbeLength() {
        return totalOperations == 0 ? 0 : (double) totalProbes / totalOperations;
    }

    private static ProbeStrategy getProbeStrategy(Probe probe) {
        return switch (probe) {
            case LINEAR -> FlatUtil.LINEAR;
            case QUADRATIC -> FlatUtil.QUADRATIC;
            case DOUBLE -> FlatUtil.DOUBLE;
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
