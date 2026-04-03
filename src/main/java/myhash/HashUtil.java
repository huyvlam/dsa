package myhash;

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
        int hash = key == null ? 0 : key.hashCode();

        // mix up bits for even distribution (minimize clustering)
        hash = hash ^ (hash >>> 16);

        // 0x7FFFFFFF ensures the hash is positive
//        return (hash & 0x7FFFFFFF) % size;

        // & (size - 1) optimize mod % size (MUST be a power of 2)
        return hash & (tableSize - 1);
    }
}
