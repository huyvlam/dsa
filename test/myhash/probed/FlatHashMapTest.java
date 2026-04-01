package myhash.probed;

import myhash.chained.ChainedHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlatHashMapTest<K, V> {
    private FlatHashMap<K, V> map;
    private int initCap;

    @BeforeEach
    void setUp() {
        initCap = 4;
        map = new FlatHashMap<>(initCap);
    }

    @Test
    @DisplayName("Should throw exception when capacity is not power of 2")
    void testCapacityNotPowerOf2() {
        assertThrows(IllegalArgumentException.class, () -> new ChainedHashMap<>(0));
        assertThrows(IllegalArgumentException.class, () -> new ChainedHashMap<>(3));
    }

    @Test
    @DisplayName("Should put and get data by key")
    void testPutGetData() {
        assertNull(map.put((K) "name", (V) "nelly"));
        assertEquals("nelly", map.put((K) "name", (V) "molly"));

        assertEquals("molly", map.get((K) "name"));
        assertEquals(1, map.size());
    }

    @Test
    @DisplayName("Should remove data by key")
    void testRemoveData() {
        map.put((K) "name", (V) "nelly");

        assertEquals("nelly", map.remove((K) "name"));
        assertTrue(map.isEmpty());
    }

    @Test
    @DisplayName("Should resize table as needed")
    void testResizeTable() {
        map.put((K) "name", (V) "nelly");
        map.put((K) "age", (V) "20");
        map.put((K) "hobby", (V) "yoga");
        map.put((K) "hair", (V) "black");
        map.put((K) "eyes", (V) "hazel");

        assertTrue(map.size() > initCap);

        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    @DisplayName("Should check whether the table contains key/value")
    void testContainsKeyValue() {
        map.put((K) "name", (V) "nelly");
        map.put((K) "age", (V) "20");
        map.put((K) "hair", (V) "black");
        map.put((K) "eyes", (V) "black");

        assertTrue(map.containsKey((K) "hair"));
        assertFalse(map.containsKey((K) "hobby"));

        assertTrue(map.containsValue((V) "black"));
        assertFalse(map.containsValue((V) "brown"));
    }
}