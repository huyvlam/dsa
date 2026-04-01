package myhash;

import myhash.probed.FlatNode;

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
     * Probe for the next available slot then update/add to the table
     *
     * @param key   key to search for
     * @param value value to add/update
     * @param table the table to be modified
     * @return      the previous value for an update, or null for new insert
     * @param <K>   type of key
     * @param <V>   type of value
     *
     * IMPORTANT    this probe method and the one below MUST produce parallel hash result
     */
    public static <K, V> V probe(K key, V value, FlatNode<K, V>[] table) {
        int initialIndex = hashIndex(key, table.length);
        int index = initialIndex;
        int gap = 1;

        int deletedIndex = -1;

        while (table[index] != null && gap <= table.length) {
            FlatNode<K, V> node = table[index];

            if (!node.deleted && areEqualKeys(node.key, key)) {
                V prevValue = node.value;
                node.value = value;
                return prevValue;
            }

            // Tombstone reuse deleted index for new entry
            if (node.deleted && deletedIndex == -1) deletedIndex = index;

            index = linearHashIndex(initialIndex, gap, table.length);
            gap++;
        }

        if (table[index] == null) {
            table[index] = new FlatNode<K, V>(key, value);
        } else {
            if (deletedIndex != -1) {
                FlatNode<K, V> reuse = table[deletedIndex];
                reuse.key = key;
                reuse.value = value;
                reuse.deleted = false;
            } else {
                throw new IllegalStateException("Capacity is exceeded");
            }
        }

        return null;
    }

    /**
     * Probe each possible slot and return the node where custom action can be performed
     *
     * @param key       key to search for
     * @param table     table of data to be searched
     * @param action    custom action to perform on the returned node
     * @param <K>       type of key
     * @param <V>       type of value
     *
     * Note: While this DRY improves readability and testability, it carries performance overhead in large dataset.
     *       Using simple looping is better for JVM optimization.
     */
    public static <K, V> void probe(K key, FlatNode<K, V>[] table, Predicate<FlatNode<K, V>> action) {
        int initialIndex = HashUtil.hashIndex(key, table.length);
        int index = initialIndex;
        int gap = 1;

        while (table[index] != null && gap <= table.length) {
            if (!action.test(table[index])) return;

            index = HashUtil.linearHashIndex(initialIndex, gap, table.length);
            gap++;
        }
    }
}
