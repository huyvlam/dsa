package myhash.probing;

import myhash.HashUtil;

import java.util.Objects;
import java.util.function.Predicate;

public class FlatUtil {
    public static final ProbeStrategy LINEAR = (orig, gap, size, stride) -> (orig + gap) % size;
    public static final ProbeStrategy QUADRATIC = (orig, gap, size, stride) -> (orig + gap * gap) % size;
    public static final ProbeStrategy DOUBLE =  (orig, gap, size, stride) -> (orig + gap * stride) % size;

    /**
     * Probe for the next available slot then update/add data to the table
     *
     * @param key       key to search for
     * @param value     value to add/update
     * @param table     the table to be modified
     * @param strategy  the probe strategy being used: LINEAR|QUADRATIC|DOUBLE
     * @return          previous value if the key is updated, or null for new insert
     * @param <K>       type of key
     * @param <V>       type of value
     *
     * IMPORTANT: This and the probe method below MUST produce parallel hash result
     */
    public static <K, V> V probe(K key, V value, FlatNode<K, V>[] table, ProbeStrategy strategy) {
        int origIndex = HashUtil.hashIndex(key, table.length);
        int gap = 1;
        int stride = stride(key);

        int index = origIndex;
        int deletedIndex = -1;

        while (gap <= table.length) {
            FlatNode<K, V> node = table[index];

            // Found empty slot, stop
            if (node == null) break;

            // Found the key, replace data
            if (!node.deleted && Objects.equals(node.key, key)) {
                V prevValue = node.value;
                node.value = value;

                return prevValue;
            }

            // Found a tombstone, save the slot for reuse
            if (node.deleted && deletedIndex == -1) deletedIndex = index;

            // Probe for next slot
            index = strategy.nextIndex(origIndex, gap, table.length, stride);
            gap++;
        }

        // Reuse the tombstone we found (Lazy Substitution)
        if (deletedIndex != -1) {
            FlatNode<K, V> reuse = table[deletedIndex];
            reuse.key = key;
            reuse.value = value;
            reuse.deleted = false;

            return null;
        }

        // Use the slot if it is empty
        if (table[index] == null) {
            table[index] = new FlatNode<K, V>(key, value);
            return null;
        }

        // All slots are checked but none available
        throw new IllegalStateException("Capacity is exceeded");
    }

    /**
     * Probe each possible slot and return the node where custom action can be performed
     *
     * @param key       key to search for
     * @param table     table of data to be searched
     * @param strategy  the probe strategy being used: LINEAR|QUADRATIC|DOUBLE
     * @param action    custom action to perform on the returned node
     * @param <K>       type of key
     * @param <V>       type of value
     *
     * Note: While this DRY improves reading, testing and maintaining, it carries performance overhead in large dataset.
     *       Using simple looping is better for JVM optimization.
     */
    public static <K, V> void probe(K key, FlatNode<K, V>[] table, ProbeStrategy strategy, Predicate<FlatNode<K, V>> action) {
        int origIndex = HashUtil.hashIndex(key, table.length);
        int gap = 1;
        int stride = stride(key);
        int index = origIndex;

        while (table[index] != null && gap <= table.length) {
            if (!action.test(table[index])) return;

            index = strategy.nextIndex(origIndex, gap, table.length, stride);
            gap++;
        }
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
     * @param stride    computed value using the key hashcode and a prime number
     * @param tableSize size of hash table
     * @return          the next available index in the hash table
     * @param <K>       key type accepts: string, integer, object, etc.
     */
    public static <K> int doubleHashIndex(int original, int gap, int stride, int tableSize) {
        return (original + gap * stride) % tableSize;
    }

    /**
     * Generate an odd number using the key hashcode given the table must be a power of 2
     *
     * @param key   the key to be converted
     * @return      an odd number only if the table size is a power of 2
     * @param <K>   key type
     */
    public static <K> int stride(K key) {
        return (Math.abs(key.hashCode()) % 32) | 1;
    }

    public static <K, V> boolean isActiveKeyEqual(FlatNode<K, V> node, K key) {
        return node != null && !node.deleted && Objects.equals(node.key, key);
    }
}
