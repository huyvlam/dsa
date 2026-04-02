package myhash;

import myhash.probing.FlatNode;

import java.util.Objects;
import java.util.function.Predicate;

public class HashUtil {
    /**
     * Compute the hashcode index using mod table size
     *
     * @param key       key to be converted
     * @param tableSize size of hash table must be a power of 2
     * @return          a valid index within bounds
     * @param <K>       key type accepts: string, integer, object, etc.
     */
    public static <K> int hashIndex(K key, int tableSize) {
        if (key == null) return 0;

        int hash = key.hashCode();

        // mix up bits ensuring even distribution
        hash = hash ^ (hash >>> 16);

        // 0x7FFFFFFF ensures the hash is positive
//        return (hash & 0x7FFFFFFF) % size;

        // & (size - 1) optimize mod % size (MUST be a power of 2)
        return hash & (tableSize - 1);
    }

    /**
     * Using linear probe to find the next available slot in the hash table
     *
     * @param original  original location of the computed hash
     * @param gap       gap from original location to next slot
     * @param tableSize size of hash table
     * @return          the next available index in the table
     */
    public static int linearHashIndex(int original, int gap, int tableSize) {
        return (original + gap) % tableSize;
    }

    /**
     * Using quadratic probe to find the next available slot in the hash table
     *
     * @param original  original location of the computed hash
     * @param gap       gap from original location to next slot
     * @param tableSize size of hash table
     * @return          the next available index in the hash table
     */
    public static int quadraticHashIndex(int original, int gap, int tableSize) {
        return (original + gap * gap) % tableSize;
    }

    /**
     * Using double hash to find the next available slot in the hash table
     *
     * @param original  original location of the computed hash
     * @param gap       gap from original location to next slot
     * @param tableSize size of hash table
     * @return          the next available index in the hash table
     * @param <K>       key type accepts: string, integer, object, etc.
     */
    public static <K> int doubleHashIndex(int original, int gap, int stride, int tableSize) {
        return (original + gap * stride) % tableSize;
    }

    public static <K> int stride(K key) {
        return (Math.abs(key.hashCode()) % 32) | 1;
    }
}
