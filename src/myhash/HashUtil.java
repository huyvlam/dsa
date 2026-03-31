package myhash;

import myhash.chain.ChainHashNode;

import java.util.Objects;

public class HashUtil {
    public static final int DEFAULT_CAPACITY = 16;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;

    /**
     * Equally compare the field key of given object with the given key value
     *
     * @param node the hash node must contain field key
     * @param key   key value to compare with
     * @return      return true if matched, otherwise false
     * @param <K>   key type
     * @param <V>   value type
     */
    public static <K, V> boolean areEqualKeys(ChainHashNode<K, V> node, K key) {
        if (node == null)  return false;

        return Objects.equals(node.key, key);
    }

    /**
     * Determine whether the current size of hash table has reached the resize threshold
     *
     * @param curSize   the number of elements currently in the hash table
     * @param tableSize size of the hash table
     * @param factor    the load factor
     * @return          return true if the hash table reached the resize threshold
     */
    public static boolean needsResize(int curSize, int tableSize, double factor) {
        return curSize >= tableSize * factor;
    }

    /**
     * Compute an available slot using mod table size
     *
     * @param key       key to convert to hash code (% S)
     * @param tableSize size of hash table must be power of 2
     * @return          an index within bounds of the hash table
     * @param <K>       key type accepts: string, integer, object, etc.
     */
    public static <K> int hashIndex(K key, int tableSize) {
        if (key == null) return 0;

        int hash = key.hashCode();

        // mix up bits ensuring even distribution
        hash = hash ^ (hash >>> 16);

        // 0x7FFFFFFF ensures the hash is positive
//        return (hash & 0x7FFFFFFF) % size;

        // & (size - 1) optimize mode (size MUST be a power of 2)
        return hash & (tableSize - 1);
    }

    /**
     * Provide the next available slot in the hash table using linear probe
     *
     * @param original  original location of the computed hash
     * @param gap       gap from original location to next slot
     * @param tableSize size of hash table
     * @return          the next available index in the hash table
     */
    public static int linearHashIndex(int original, int gap, int tableSize) {
        return (original + gap) % tableSize;
    }

    /**
     * Provide the next available slot in the hash table using quadratic probe
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
     * Provide the next available slot in the hash table using double hash
     *
     * @param key       key to convert to hash code (% 5)
     * @param original  original location of the computed hash
     * @param gap       gap from original location to next slot
     * @param tableSize size of hash table
     * @return          the next available index in the hash table
     * @param <K>       key type accepts: string, integer, object, etc.
     */
    public static <K> int doubleHashIndex(K key, int original, int gap, int tableSize) {
        int prime = tableSize - 1;
        int step = prime - ((key.hashCode() & 0x7FFFFFFF) % prime);

        return (original + gap * step) % tableSize;
    }
}
