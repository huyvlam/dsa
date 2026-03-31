package myhash.chain;

import myhash.HashHelper;
import myhash.HashUtil;

import static myhash.HashUtil.*;

public class ChainHashMap<K, V> {
    private ChainHashNode<K, V>[] table;
    private int size;
    private final int initialCapacity;
    private final double loadFactor;

    public ChainHashMap(int capacity, double factor) {
        if (capacity <= 0 || (capacity & (capacity - 1)) != 0) throw new IllegalArgumentException("Capacity must be power of 2");

        initialCapacity = capacity;
        loadFactor = factor;
        table = new ChainHashNode[initialCapacity];
    }

    public ChainHashMap(int capacity) {
        this(capacity, HashUtil.DEFAULT_LOAD_FACTOR);
    }

    public ChainHashMap() {
        this(HashUtil.DEFAULT_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
    }

    public void clear() {
        table = new ChainHashNode[initialCapacity];
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void put(K key, V value) {
        int index = HashHelper.hashIndex(key, table.length);

        ChainHashNode<K, V> head = table[index];
        ChainHashNode<K, V> cur = head;

        while (cur != null) {
            if (HashHelper.areEqualKeys(cur, key)) {
                cur.value = value;
                return;
            }
            cur = cur.next;
        }

        ChainHashNode<K, V> node = new ChainHashNode<>(key, value, head);
        table[index] = node;
        size++;

        if (HashHelper.needsResize(size, table.length, loadFactor)) resize();
    }

    public void resize() {
        int newCapacity = table.length * 2;
        ChainHashNode<K, V>[] newEntries = new ChainHashNode[newCapacity];

        for (int i = 0; i < table.length; i++) {
            ChainHashNode<K, V> cur = table[i];

            while (cur != null) {
                ChainHashNode<K, V> next = cur.next;

                int index = HashHelper.hashIndex(cur.key, newCapacity);

                cur.next = newEntries[index];
                newEntries[index] = cur;

                cur = next;
            }
        }

        table = newEntries;
    }

    public V get(K key) {
        int index = HashHelper.hashIndex(key, table.length);
        ChainHashNode<K, V> cur = table[index];

        while (cur != null) {
            if (HashHelper.areEqualKeys(cur, key)) return cur.value;

            cur = cur.next;
        }

        return null;
    }

    public V remove(K key) {
        int index = HashHelper.hashIndex(key, table.length);
        ChainHashNode<K, V> cur = table[index];
        ChainHashNode<K, V> prev = null;

        while (cur != null) {
            if (HashHelper.areEqualKeys(cur, key)) {
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
