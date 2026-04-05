package myhash;

import java.util.HashMap;

public class HashUtil {
    /**
     * Compute the hashcode index using mod table size
     *
     * @param key       key to be converted
     * @param tableSize table size must be a Power of 2
     * @return          a valid index in the range 0 - tableSize
     * @param <K>       key type
     */
    public static <K> int hashIndex(K key, int tableSize) {
        int hash = key == null ? 0 : key.hashCode();

        // mix up bits for even distribution (minimize clustering)
        hash = hash ^ (hash >>> 16);

        // 0x7FFFFFFF ensures the hash is positive
//        return (hash & 0x7FFFFFFF) % size;

        // & (size - 1) optimize mod % size (MUST be a power of 2)
        return hash & (tableSize - 1);
    }

    public static int linearProbe(int orig, int gap, int size) {
        return (orig + gap) & (size - 1);
    }

    public static int quadraticProbe(int orig, int gap, int size) {
        return (orig + gap * gap) & (size - 1);
    }

    public static int doubleHash(int orig, int gap, int size, int stride) {
        return (orig + gap * stride) & (size - 1);
    }

    /**
     * Compute a positive odd number for Power of 2 table size
     *
     * @param key   the key to be converted
     * @return      a prime number to use in conjunction with double hash method
     * @param <K>   key type
     */
    public static <K> int stride(K key) {
        int hash = key == null ? 0 : key.hashCode();

        // & 0x7FFFFFFF clears sign bit and ensure positive
        // | 1 ensure the number is odd
        return ((hash & 0x7FFFFFFF) % 32) | 1;
    }

    /**
     * Check and return a Power of 2 table size
     *
     * @param tableSize the original table size
     * @return          Power of 2 table size
     */
    public static int tableSize(int tableSize) {
        if ((tableSize & (tableSize - 1)) == 0) return tableSize;

        int cap = tableSize - 1;
        cap |= cap >>> 1;
        cap |= cap >>> 2;
        cap |= cap >>> 4;
        cap |= cap >>> 8;
        cap |= cap >>> 16;

        return (cap < 0) ? 1 : cap + 1;
    }
}
