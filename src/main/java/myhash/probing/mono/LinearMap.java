package myhash.probing.mono;

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

    // Backshifting enables faster 'put' for linear probe
    @Override
    public V remove(K key) {
        int origIndex = indexFor(key);
        int gap = 1;
        int index = origIndex;

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> cur = table[index];

            // Found the key
            if (Object.equals(cur.key, key)) {
                V removed = cur.value;
                int vacant = index;

                // Backshift
                while (true) {
                    index = (index + 1) & mask;
                    cur = table[index];

                    if (cur == null) break;

                    int ideal = indexFor(cur.key);

                    // Move current forward
                    if (!isBetween(vacant, index, ideal)) {
                        table[vacant] = table[index];
                        vacant = index;
                    }
                }
                // Release the vacant spot
                table[vacant] = null;
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

    private boolean isBetween(int vacant, int cur, int ideal) {
        if (vacant <= cur) return vacant < ideal && ideal <= cur;

        return vacant < ideal || ideal <= cur;
    }
}
