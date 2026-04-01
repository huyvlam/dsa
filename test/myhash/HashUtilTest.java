package myhash;

import myhash.probed.HashNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashUtilTest<K, V> {
    private HashNode<K, V>[] table;
    private String[] keys;
    private int S, addressI, idI;
    private int capacity;

    @BeforeEach
    void setup() {
        capacity = 4;
        table = (HashNode<K, V>[]) new HashNode[capacity];
        keys = new String[]{"address","name","id","city","profession","phone"};
        S = keys.length;
        addressI = HashUtil.hashIndex(keys[0], S);
        idI = HashUtil.hashIndex(keys[2], S);
    }

    @Test
    @DisplayName("Should compare the field key in the object with key value")
    <K, V> void testEqualKeys() {
        K k1 = (K) "address";
        K k2 = (K) "name";

        assertTrue(HashUtil.areEqualKeys(k1, k1));
        assertTrue(HashUtil.areEqualKeys(null, null));
        assertFalse(HashUtil.areEqualKeys(k1, k2));
        assertFalse(HashUtil.areEqualKeys(null, k2));
    }

    @Test
    @DisplayName("Should compare to the current size to the computing threshold")
    void testNeedResize() {
        double factor = 0.75;
        int tableSize = 12;
        int curSize = 9;

        assertTrue(HashUtil.needsResize(curSize, tableSize, factor));
        assertTrue(HashUtil.needsResize(curSize + 2, tableSize, factor));
        assertFalse(HashUtil.needsResize(curSize - 1, tableSize, factor));
    }

    @Test
    @DisplayName("Should compute a hash index within legal bounds of the table size")
    void testHashIndex() {
        String keyS = "address";
        int tableSize = 3;

        int index = HashUtil.hashIndex(keyS, tableSize);
        assertTrue(index >= 0);
        assertTrue(index < tableSize);
        assertEquals(0, HashUtil.hashIndex(null, tableSize));
    }

    @Test
    @DisplayName("Should compute a linear hash index greater than the original index")
    void testLinearHashIndex() {
        int linearI = HashUtil.linearHashIndex(idI, 1, S);

        assertEquals(addressI, idI);
        assertTrue(linearI > idI);
        assertTrue(HashUtil.linearHashIndex(idI, 2, S) > linearI);
    }

    @Test
    @DisplayName("Should compute a quadratic hash index greater than the original index")
    void testQuadraticHashIndex() {
        int quadI = HashUtil.quadraticHashIndex(idI, 1, S);

        assertEquals(addressI, idI);
        assertTrue(quadI > idI);
        assertTrue(HashUtil.quadraticHashIndex(idI, 2, S) > quadI);
    }

    @Test
    @DisplayName("Should compute a double hash index within legal bounds of the table size")
    void testDoubleHashIndex() {
        int doubleI = HashUtil.doubleHashIndex(keys[2], idI, 1, S);

        assertEquals(addressI, idI);
        assertNotEquals(doubleI, idI);
        assertTrue(doubleI >= 0);
        assertTrue(doubleI < S);
    }

    @Test
    @DisplayName("Should find available slot to add/update data")
    void testProbeAddUpdate() {
        assertNull(HashUtil.probe((K) "apple", (V) "red", table));
        assertNull(HashUtil.probe((K) "banana", (V) "yellow", table));
        assertNull(HashUtil.probe((K) "peach", (V) "orange", table));
        assertNull(HashUtil.probe((K) "kiwi", (V) "green", table));

        assertNotNull(table[0]);
        assertNotNull(table[1]);
        assertNotNull(table[2]);
        assertNotNull(table[3]);

        assertEquals("green", HashUtil.probe((K) "kiwi", (V) "yellow", table));
    }


    @Test
    @DisplayName("Should reuse deleted slot to add new data")
    void testProbeTombstoneReuse() {
        assertNull(HashUtil.probe((K) "apple", (V) "red", table));
        assertNull(HashUtil.probe((K) "banana", (V) "yellow", table));
        assertNull(HashUtil.probe((K) "peach", (V) "orange", table));
        assertNull(HashUtil.probe((K) "kiwi", (V) "green", table));

        HashNode<K, V> node = table[1];
        node.key = null;
        node.value = null;
        node.deleted = true;

        assertNull(HashUtil.probe((K) "berry", (V) "blue", table));
        assertEquals("blue", HashUtil.probe((K) "berry", (V) "black", table));
    }

    @Test
    @DisplayName("Should throw exception when the table is full")
    void testProbeCapacityException() {
        assertNull(HashUtil.probe((K) "apple", (V) "red", table));
        assertNull(HashUtil.probe((K) "banana", (V) "yellow", table));
        assertNull(HashUtil.probe((K) "peach", (V) "orange", table));
        assertNull(HashUtil.probe((K) "kiwi", (V) "green", table));

        assertThrows(IllegalStateException.class, () -> HashUtil.probe((K) "berry", (V) "blue", table));
    }
}