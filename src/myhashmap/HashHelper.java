package myhashmap;

public class HashHelper {
    /**
     * Compute an available slot using mod table size
     *
     * @param key   key to convert to hash code (% S)
     * @param size  size of hash table
     * @return      an index within bounds of the hash table size
     * @param <K>   key type accepts: string, integer, object, etc.
     */
    public static <K> int hash(K key, int size) {
        return (key.hashCode() & 0x7FFFFFFF) % size;
    }

    /**
     * Provide the next available slot in the hash table using linear probe
     *
     * @param n     original location of the computed hash
     * @param i     gap from original location to next slot
     * @param size  size of hash table
     * @return      the next available index in the hash table
     */
    public static int linearHash(int n, int i, int size) {
        return (n + i) % size;
    }

    /**
     * Provide the next available slot in the hash table using quadratic probe
     *
     * @param n     original location of the computed hash
     * @param i     gap from original location to next slot
     * @param size  size of hash table
     * @return      the next available index in the hash table
     */
    public static int quadraticHash(int n, int i, int size) {
        return (int) (n + Math.pow(i, i)) % size;
    }

    /**
     * Provide the next available slot in the hash table using double hash
     *
     * @param key   key to convert to hash code (% 5)
     * @param n     original location of the computed hash
     * @param i     gap from original location to next slot
     * @param size  size of hash table
     * @return      the next available index in the hash table
     * @param <K>   key type accepts: string, integer, object, etc.
     */
    public static <K> int doubleHash(K key, int n, int i, int size) {
        return (n + i * (1 + hash(key, 5))) % size;
    }
}
