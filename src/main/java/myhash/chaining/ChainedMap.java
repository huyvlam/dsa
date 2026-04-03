package myhash.chaining;

import myhash.HashUtil;

import java.util.Objects;

public class ChainedMap<K, V> {
    private Entry<K, V>[] entries;
    private double resizeThreshold;
    private int size;

    private final int INIT_CAPACITY;
    private final double LOAD_FACTOR;

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    public ChainedMap(int capacity, double factor) {
        if (capacity <= 0 || (capacity & (capacity - 1)) != 0) throw new IllegalArgumentException("Capacity must be power of 2");

        INIT_CAPACITY = capacity;
        LOAD_FACTOR = factor;
        entries = (Entry<K, V>[]) new Entry[INIT_CAPACITY];
        resizeThreshold = entries.length * LOAD_FACTOR;
        size = 0;
    }

    public ChainedMap(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public ChainedMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public void clear() {
        entries = (Entry<K, V>[]) new Entry[INIT_CAPACITY];
        resizeThreshold = entries.length * LOAD_FACTOR;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
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

        this.entries = newEntries;
        this.resizeThreshold = entries.length * LOAD_FACTOR;
    }

    public V put(K key, V value) {
        int index = HashUtil.hashIndex(key, entries.length);

        Entry<K, V> head = entries[index];
        Entry<K, V> cur = head;

        while (cur != null) {
            if (Objects.equals(cur.key, key)) {
                V prevValue = cur.value;
                cur.value = value;
                return prevValue;
            }
            cur = cur.next;
        }

        Entry<K, V> node = new Entry<>(key, value, head);
        entries[index] = node;
        size++;

        if (size >= resizeThreshold) resize();

        return null;
    }

    public V get(K key) {
        int index = HashUtil.hashIndex(key, entries.length);
        Entry<K, V> cur = entries[index];

        while (cur != null) {
            if (Objects.equals(cur.key, key)) return cur.value;

            cur = cur.next;
        }

        return null;
    }

    public V remove(K key) {
        int index = HashUtil.hashIndex(key, entries.length);
        Entry<K, V> cur = entries[index];
        Entry<K, V> prev = null;

        while (cur != null) {
            if (Objects.equals(cur.key, key)) {
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

    public boolean containsKey(K key) {
        int index = HashUtil.hashIndex(key, entries.length);
        Entry<K, V> cur = entries[index];

        while (cur != null) {
            if (Objects.equals(cur.key, key)) return true;
            cur = cur.next;
        }

        return false;
    }

    public boolean containsValue(V value) {
        for (int i = 0; i < entries.length; i++) {
            Entry<K, V> cur = entries[i];

            while (cur != null) {
                if (Objects.equals(cur.value, value)) return true;
                cur = cur.next;
            }
        }

        return false;
    }
}
