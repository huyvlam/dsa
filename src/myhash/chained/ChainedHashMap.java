package myhash.chained;

import myhash.HashUtil;

public class ChainedHashMap<K, V> {
    private Entry<K, V>[] entries;
    private int size;
    private final int initialCapacity;
    private final double loadFactor;

    public ChainedHashMap(int capacity, double factor) {
        if (capacity <= 0 || (capacity & (capacity - 1)) != 0) throw new IllegalArgumentException("Capacity must be power of 2");

        initialCapacity = capacity;
        loadFactor = factor;
        entries = (Entry<K, V>[]) new Entry[initialCapacity];
    }

    public ChainedHashMap(int capacity) {
        this(capacity, HashUtil.DEFAULT_LOAD_FACTOR);
    }

    public ChainedHashMap() {
        this(HashUtil.DEFAULT_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
    }

    public void clear() {
        entries = (Entry<K, V>[]) new Entry[initialCapacity];
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V put(K key, V value) {
        int index = HashUtil.hashIndex(key, entries.length);

        Entry<K, V> head = entries[index];
        Entry<K, V> cur = head;

        while (cur != null) {
            if (HashUtil.areEqualKeys(cur.key, key)) {
                V prevValue = cur.value;
                cur.value = value;
                return prevValue;
            }
            cur = cur.next;
        }

        Entry<K, V> node = new Entry<>(key, value, head);
        entries[index] = node;
        size++;

        if (HashUtil.needsResize(size, entries.length, loadFactor)) resize();

        return null;
    }

    @SuppressWarnings("unchecked")
    public void resize() {
        int newCapacity = entries.length * 2;
        Entry<K, V>[] newEntries = (Entry<K, V>[]) new Entry[newCapacity];

        for (int i = 0; i < entries.length; i++) {
            Entry<K, V> cur = entries[i];

            while (cur != null) {
                Entry<K, V> next = cur.next;

                int index = HashUtil.hashIndex(cur.key, newCapacity);

                cur.next = newEntries[index];
                newEntries[index] = cur;

                cur = next;
            }
        }

        entries = newEntries;
    }

    public V get(K key) {
        int index = HashUtil.hashIndex(key, entries.length);
        Entry<K, V> cur = entries[index];

        while (cur != null) {
            if (HashUtil.areEqualKeys(cur.key, key)) return cur.value;

            cur = cur.next;
        }

        return null;
    }

    public V remove(K key) {
        int index = HashUtil.hashIndex(key, entries.length);
        Entry<K, V> cur = entries[index];
        Entry<K, V> prev = null;

        while (cur != null) {
            if (HashUtil.areEqualKeys(cur.key, key)) {
                V removed = cur.value;

                if (prev == null) entries[index] = cur.next;
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
