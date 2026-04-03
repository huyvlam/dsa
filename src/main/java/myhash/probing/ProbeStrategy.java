package myhash.probing;

@FunctionalInterface
public interface ProbeStrategy {
    int nextIndex(int original, int gap, int tableSize, int stride);
}
