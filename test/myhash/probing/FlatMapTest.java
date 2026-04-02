package myhash.probing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlatMapTest<K, V> {
    private FlatMap<K, V> flat;
    private int initCap;

    @BeforeEach
    void setUp() {
        initCap = 4;
        flat = new FlatMap<>(FlatMap.Probe.LINEAR, initCap);
    }

    @Test
    @DisplayName("Should throw exception when capacity is not power of 2")
    void testCapacityNotPowerOf2() {
        assertThrows(IllegalArgumentException.class, () -> new FlatMap<>(FlatMap.Probe.LINEAR, 0));
        assertThrows(IllegalArgumentException.class, () -> new FlatMap<>(FlatMap.Probe.LINEAR, 3));
    }

    @Test
    @DisplayName("Should add data by key")
    void testAddData() {
        assertNull(flat.put((K) "name", (V) "nelly"));
        assertEquals("nelly", flat.get((K) "name"));
        assertEquals(1, flat.size());
    }

    @Test
    @DisplayName("Should update data by key")
    void testUpdateData() {
        assertNull(flat.put((K) "name", (V) "nelly"));
        assertEquals("nelly", flat.put((K) "name", (V) "molly"));
        assertEquals("molly", flat.get((K) "name"));
    }

    @Test
    @DisplayName("Should remove data by key")
    void testRemoveData() {
        assertNull(flat.put((K) "name", (V) "molly"));
        assertEquals("molly", flat.remove((K) "name"));
        assertTrue(flat.isEmpty());
    }

    @Test
    @DisplayName("Should resize table as needed")
    void testResizeTable() {
        flat.put((K) "name", (V) "molly");
        flat.put((K) "age", (V) "20");
        flat.put((K) "hobby", (V) "yoga");
        flat.put((K) "hair", (V) "black");
        flat.put((K) "eyes", (V) "brown");

        assertTrue(flat.size() > initCap);

        flat.clear();

        assertTrue(flat.isEmpty());
    }

    @Test
    @DisplayName("Should check whether the table contains key/value")
    void testContainsKeyValue() {
        flat.put((K) "name", (V) "nelly");
        flat.put((K) "hair", (V) "black");
        flat.put((K) "eyes", (V) "black");

        assertTrue(flat.containsKey((K) "hair"));
        assertFalse(flat.containsKey((K) "hobby"));
        assertTrue(flat.containsValue((V) "black"));
        assertFalse(flat.containsValue((V) "brown"));
    }
}