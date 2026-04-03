package myhash.probing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinearMapTest<K, V> {
    private LinearMap<K, V> linear;
    private int initCap;

    @BeforeEach
    void setUp() {
        initCap = 4;
        linear = new LinearMap<>(initCap);
    }

    @Test
    @DisplayName("Should throw exception when capacity is not power of 2")
    void testCapacityNotPowerOf2() {
        assertThrows(IllegalArgumentException.class, () -> new LinearMap<>(0));
        assertThrows(IllegalArgumentException.class, () -> new LinearMap<>(3));
    }

    @Test
    @DisplayName("Should add data by key")
    void testAddData() {
        assertNull(linear.put((K) "name", (V) "nelly"));
        assertEquals("nelly", linear.get((K) "name"));
        assertEquals(1, linear.size());
    }

    @Test
    @DisplayName("Should update data by key")
    void testUpdateData() {
        assertNull(linear.put((K) "name", (V) "nelly"));
        assertEquals("nelly", linear.put((K) "name", (V) "molly"));
        assertEquals("molly", linear.get((K) "name"));
    }

    @Test
    @DisplayName("Should remove data by key")
    void testRemoveData() {
        assertNull(linear.put((K) "name", (V) "molly"));
        assertEquals("molly", linear.remove((K) "name"));
        assertTrue(linear.isEmpty());
    }

    @Test
    @DisplayName("Should resize table as needed")
    void testResizeTable() {
        linear.put((K) "name", (V) "molly");
        linear.put((K) "age", (V) "20");
        linear.put((K) "hobby", (V) "yoga");
        linear.put((K) "hair", (V) "black");
        linear.put((K) "eyes", (V) "brown");

        assertTrue(linear.size() > initCap);

        linear.clear();

        assertTrue(linear.isEmpty());
    }

    @Test
    @DisplayName("Should check whether the table contains key/value")
    void testContainsKeyValue() {
        linear.put((K) "name", (V) "nelly");
        linear.put((K) "hair", (V) "black");
        linear.put((K) "eyes", (V) "black");

        assertTrue(linear.containsKey((K) "hair"));
        assertFalse(linear.containsKey((K) "hobby"));
        assertTrue(linear.containsValue((V) "black"));
        assertFalse(linear.containsValue((V) "brown"));
    }
}