package myhash.probed;

import myhash.HashUtil;

public class FlatHashMap<K, V> {
    private Flat<K, V>[] table;

    private final int initialCapacity;
    private final double loadFactor;

    private double resizeThreshold;
    private int size;

    public FlatHashMap(int capacity, double factor) {
        if (capacity <= 0 || (capacity & (capacity - 1)) != 0) throw new IllegalArgumentException("Capacity must be power of 2");

        initialCapacity = capacity;
        loadFactor = factor;
        size = 0;
        table = (Flat<K, V>[]) new Flat[initialCapacity];
        resizeThreshold = table.length * loadFactor;
    }

    public FlatHashMap(int capacity) {
        this(capacity, HashUtil.DEFAULT_LOAD_FACTOR);
    }

    public FlatHashMap() {
        this(HashUtil.DEFAULT_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
    }

    public void clear() {
        table = new Flat[initialCapacity];
        resizeThreshold = table.length * loadFactor;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public V put(K key, V value) {
        if (size >= resizeThreshold) resize();

        V result = HashUtil.probe(key, value, table);

        if (result == null) size++;

        return result;
    }

    @SuppressWarnings("unchecked")
    public void resize() {
        int newCapacity = table.length * 2;
        Flat<K, V>[] newTable = (Flat<K, V>[]) new Flat[newCapacity];

        for (Flat<K, V> cur : table) {
            if (cur != null && !cur.deleted) HashUtil.probe(cur.key, cur.value, newTable);
        }

        this.table = newTable;
        this.resizeThreshold = table.length * loadFactor;
    }

    public V get(K key) {
        final V[] result = (V[]) new Object[]{null};

        HashUtil.probe(key, table, (node) -> {
            if (!node.deleted && HashUtil.areEqualKeys(node.key, key)) {
                result[0] = node.value;

                return false;
            };
            return true;
        });

        return (V) result[0];
    }

    public V remove(K key) {
        final V[] removed = (V[]) new Object[]{null};

        HashUtil.probe(key, table, (node) -> {
            if (!node.deleted && HashUtil.areEqualKeys(node.key, key)) {
                removed[0] = node.value;
                node.key = null;
                node.value = null;
                node.deleted = true;
                size--;

                return false;
            }
            return true;
        });

        return (V) removed[0];
    }
}
