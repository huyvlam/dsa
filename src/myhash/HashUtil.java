package myhash;

import myhash.probed.HashNode;

import java.util.Objects;
import java.util.function.Predicate;

public class HashUtil {
    public static final int DEFAULT_CAPACITY = 16;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;
    public static enum Status { REMOVED };

    /**
     * Equally compare the field key of given object with the given key value
     *
     * @param nodeKey the hash node must contain field key
     * @param key   key value to compare with
     * @return      return true if matched, otherwise false
     * @param <K>   key type
     * @param <V>   value type
     */
    public static <K, V> boolean areEqualKeys(K nodeKey, K key) {
        return Objects.equals(nodeKey, key);
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

    /**
     * Probe for the next available slot and update the table
     *
     * @param key
     * @param value
     * @param table
     * @return
     * @param <K>
     * @param <V>
     */
    public static <K, V> V probe(K key, V value, HashNode<K, V>[] table) {
        int initialIndex = hashIndex(key, table.length);
        int index = initialIndex;
        int gap = 1;

        int deletedIndex = -1;

        while (table[index] != null && gap < table.length) {
            HashNode<K, V> node = table[index];

            if (!node.deleted && areEqualKeys(node.key, key)) {
                V prevValue = node.value;
                node.value = value;
                return prevValue;
            }

            // reuse deleted index for new entry
            if (node.deleted && deletedIndex == -1) deletedIndex = index;

            index = linearHashIndex(initialIndex, gap, table.length);
            gap++;
        }

        if (deletedIndex != -1) {
            HashNode<K, V> reuse = table[deletedIndex];
            reuse.key = key;
            reuse.value = value;
            reuse.deleted = false;
        } else {
            table[index] = new HashNode<K, V>(key, value);
        }

        return null;
    }

    /**
     * Iterate thru each available slot and perform custom action on the node data
     *
     * @param key
     * @param table
     * @param action
     * @param <K>
     * @param <V>
     */
    public static <K, V> void probe(K key, HashNode<K, V>[] table, Predicate<HashNode<K, V>> action) {
        int initialIndex = HashUtil.hashIndex(key, table.length);
        int index = initialIndex;
        int gap = 1;

        while (table[index] != null && gap < table.length) {
            if (!action.test(table[index])) return;

            index = HashUtil.linearHashIndex(initialIndex, gap, table.length);
            gap++;
        }
    }
}
