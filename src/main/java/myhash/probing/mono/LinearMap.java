package myhash.probing.mono;

import myhash.probing.FlatNode;
import myhash.HashUtil;

import java.util.Objects;

public class LinearMap<K, V> extends MonoFlatMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LINEAR_LOAD_FACTOR = 0.7;

    public LinearMap(int capacity, double factor) {
        super(capacity, factor);
    }

    public LinearMap(int capacity) {
        this(capacity, LINEAR_LOAD_FACTOR);
    }

    public LinearMap() {
        this(DEFAULT_CAPACITY, LINEAR_LOAD_FACTOR);
    }

    @Override
    int nextIndex(int orig, int gap, int mask) {
        return (orig + gap) & mask;
    }

    @Override
    public V get(K key) {
        int orig = HashUtil.hash(key) & mask;
        int gap = 1;
        int index = orig;

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            if (Objects.equals(cur.key, key)) {
                recordMetrics(gap, gap);
                return cur.value;
            }
            index = nextIndex(orig, gap, mask);
            gap++;
        }
        recordMetrics(gap, gap);

        return null;
    }

    @Override
    protected void resize() {
        int newCap = table.length * 2;
        FlatNode<K, V>[] newTable = (FlatNode<K, V>[]) new FlatNode[newCap];

        for (FlatNode<K, V> cur : table) {
            if (cur != null) probe(cur.key, cur.value, newTable);
        }

        table = newTable;
        threshold = table.length * LOAD_FACTOR;
        mask = table.length - 1;
    }

    // Backshifting reduces the number of probes when performing 'put'
    @Override
    public V remove(K key) {
        int orig = HashUtil.hash(key) & mask;
        int gap = 1;
        int index = orig;

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            if (Objects.equals(cur.key, key)) {
                V removed = cur.value;
                int vacant = index;
                int deletedGap = gap;

                while (true) {
                    index = (index + 1) & mask;
                    cur = table[index];

                    if (cur == null) break;

                    int ideal = HashUtil.hash(cur.key) & mask;

                    if (!isBetween(vacant, index, ideal)) {
                        table[vacant] = table[index];
                        vacant = index;
                    }
                    gap++;
                }
                table[vacant] = null;
                size--;
                recordMetrics(gap, deletedGap);

                return removed;
            }
            index = nextIndex(orig, gap, mask);
        }
        recordMetrics(gap, gap);

        return null;
    }

    private boolean isBetween(int vacant, int cur, int ideal) {
        if (vacant <= cur) return vacant < ideal && ideal <= cur;
        return vacant < ideal || ideal <= cur;
    }

    @Override
    public boolean containsKey(K key) {
        int orig = HashUtil.hash(key) & mask;
        int gap = 1;
        int index = orig;

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            if (Objects.equals(cur.key, key)) {
                recordMetrics(gap, gap);
                return true;
            }

            index = nextIndex(orig, gap, mask);
            gap++;
        }
        recordMetrics(gap, gap);

        return false;
    }

    @Override
    public boolean containsValue(V value) {
        for (FlatNode<K, V> node : table) {
            if (node != null && Objects.equals(node.value, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected V probe(K key, V val, FlatNode<K, V>[] tab) {
        int tabMask = tab.length - 1;
        int orig = HashUtil.hash(key) & tabMask;
        int gap = 1;
        int index = orig;

        while (gap <= tab.length) {
            FlatNode<K, V> cur = tab[index];

            if (cur == null) break;

            if (Objects.equals(cur.key, key)) {
                V prevVal = cur.value;
                cur.value = val;
                recordMetrics(gap, gap);

                return prevVal;
            }

            index = nextIndex(orig, gap, tabMask);
            gap++;
        }

        if (tab[index] == null) {
            tab[index] = new FlatNode<K, V>(key, val);
            recordMetrics(gap, gap);

            return null;
        }

        throw new IllegalStateException("Capacity is exceeded");
    }
}
