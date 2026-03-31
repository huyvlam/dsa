package myhash.chain;

import myhash.HashUtil;

public class ChainHashMap<K, V> {
    private ChainHashNode<K, V>[] table;
    private int size;
    private final int initialCapacity;
    private final double loadFactor;

    public ChainHashMap(int capacity, double factor) {
        if (capacity <= 0 || (capacity & (capacity - 1)) != 0) throw new IllegalArgumentException("Capacity must be power of 2");

        initialCapacity = capacity;
        loadFactor = factor;
        table = (ChainHashNode<K, V>[]) new ChainHashNode[initialCapacity];
    }

    public ChainHashMap(int capacity) {
        this(capacity, HashUtil.DEFAULT_LOAD_FACTOR);
    }

    public ChainHashMap() {
        this(HashUtil.DEFAULT_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
    }

    public void clear() {
        table = (ChainHashNode<K, V>[]) new ChainHashNode[initialCapacity];
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V put(K key, V value) {
        int index = HashUtil.hashIndex(key, table.length);

        ChainHashNode<K, V> head = table[index];
        ChainHashNode<K, V> cur = head;

        while (cur != null) {
            if (HashUtil.areEqualKeys(cur, key)) {
                V prevValue = cur.value;
                cur.value = value;
                return prevValue;
            }
            cur = cur.next;
        }

        ChainHashNode<K, V> node = new ChainHashNode<>(key, value, head);
        table[index] = node;
        size++;

        if (HashUtil.needsResize(size, table.length, loadFactor)) resize();

        return null;
    }

    public void resize() {
        int newCapacity = table.length * 2;
        ChainHashNode<K, V>[] newEntries = (ChainHashNode<K, V>[]) new ChainHashNode[newCapacity];

        for (int i = 0; i < table.length; i++) {
            ChainHashNode<K, V> cur = table[i];

            while (cur != null) {
                ChainHashNode<K, V> next = cur.next;

                int index = HashUtil.hashIndex(cur.key, newCapacity);

                cur.next = newEntries[index];
                newEntries[index] = cur;

                cur = next;
            }
        }

        table = newEntries;
    }

    public V get(K key) {
        int index = HashUtil.hashIndex(key, table.length);
        ChainHashNode<K, V> cur = table[index];

        while (cur != null) {
            if (HashUtil.areEqualKeys(cur, key)) return cur.value;

            cur = cur.next;
        }

        return null;
    }

    public V remove(K key) {
        int index = HashUtil.hashIndex(key, table.length);
        ChainHashNode<K, V> cur = table[index];
        ChainHashNode<K, V> prev = null;

        while (cur != null) {
            if (HashUtil.areEqualKeys(cur, key)) {
                V removed = cur.value;

                if (prev == null) table[index] = cur.next;
                else prev.next = cur.next;

                size--;
                return removed;
            }
            prev = cur;
            cur = cur.next;
        }
        return null;
    }
}
