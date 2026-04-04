package myhash.probing;

public class QuadraticMap <K, V> extends BaseProbeMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double QUADRATIC_LOAD_FACTOR = 0.5;

    public QuadraticMap(int capacity, double factor) {
        super(capacity, factor);
    }

    public QuadraticMap(int capacity) {
        this(capacity, QUADRATIC_LOAD_FACTOR);
    }

    public QuadraticMap() {
        this(DEFAULT_CAPACITY, QUADRATIC_LOAD_FACTOR);
    }

    @Override
    int nextIndex(int orig, int gap, int size) {
        return (orig + gap * gap) % size;
    }
}
