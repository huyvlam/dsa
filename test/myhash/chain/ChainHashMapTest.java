package myhash.chain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChainHashMapTest<K, V> {
    private ChainHashMap<K, V> map;
    private int initialCapacity;

    @BeforeEach
    void setUp() {
        initialCapacity = 2;
        map = new ChainHashMap<>(initialCapacity);
    }

    @Test
    @DisplayName("Should throw exception when capacity is not power of 2")
    void testCapacityNotPowerOf2() {
        assertThrows(IllegalArgumentException.class, () -> new ChainHashMap<>(0));
        assertThrows(IllegalArgumentException.class, () -> new ChainHashMap<>(3));
    }

    @Test
    @DisplayName("Should put and get data by the key specified")
    void testPutGetData() {
        map.put((K) "name", (V) "nelly");

        assertEquals("nelly", map.get((K) "name"));
        assertEquals(1, map.size());
    }

    @Test
    @DisplayName("Should remove data by the key specified")
    void testRemoveData() {
        map.put((K) "name", (V) "nelly");

        assertEquals("nelly", map.remove((K) "name"));
        assertTrue(map.isEmpty());
    }

    @Test
    @DisplayName("Should remove data by the key specified")
    void testResizeData() {
        map.put((K) "name", (V) "nelly");
        map.put((K) "age", (V) "20");
        map.put((K) "hobby", (V) "yoga");

        assertTrue(map.size() > initialCapacity);

        map.clear();
        assertTrue(map.isEmpty());
    }
}