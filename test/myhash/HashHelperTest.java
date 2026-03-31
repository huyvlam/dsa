package myhash;

import myhash.chaining.LinkedHashNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashHelperTest {
    private String[] keys;
    private int S, addressI, idI;

    @BeforeEach
    void setup() {
        keys = new String[]{"address","name","id","city","profession","phone"};
        S = keys.length;
        addressI = HashHelper.hashIndex(keys[0], S);
        idI = HashHelper.hashIndex(keys[2], S);
    }

    @Test
    @DisplayName("Should compare the field key in the object with key value")
    <K, V> void testEqualKeys() {
        LinkedHashNode<K, V> entry = new LinkedHashNode<>((K) "id", (V) "12345", null);

        assertTrue(HashHelper.areEqualKeys(entry, (K) "id"));
        assertFalse(HashHelper.areEqualKeys(entry, (K) "ids"));
        assertFalse(HashHelper.areEqualKeys(null, (K) "id"));
    }

    @Test
    @DisplayName("Should compare to the current size to the computing threshold")
    void testNeedResize() {
        double factor = 0.75;
        int tableSize = 12;
        int curSize = 9;

        assertTrue(HashHelper.needsResize(curSize, tableSize, factor));
        assertTrue(HashHelper.needsResize(curSize + 2, tableSize, factor));
        assertFalse(HashHelper.needsResize(curSize - 1, tableSize, factor));
    }

    @Test
    @DisplayName("Should compute a hash index within legal bounds of the table size")
    void testHashIndex() {
        String keyS = "address";
        int tableSize = 3;

        int index = HashHelper.hashIndex(keyS, tableSize);
        assertTrue(index >= 0);
        assertTrue(index < tableSize);
        assertEquals(0, HashHelper.hashIndex(null, tableSize));
    }

    @Test
    @DisplayName("Should compute a linear hash index greater than the original index")
    void testLinearHashIndex() {
        int linearI = HashHelper.linearHashIndex(idI, 1, S);

        assertEquals(addressI, idI);
        assertTrue(linearI > idI);
        assertTrue(HashHelper.linearHashIndex(idI, 2, S) > linearI);
    }

    @Test
    @DisplayName("Should compute a quadratic hash index greater than the original index")
    void testQuadraticHashIndex() {
        int quadI = HashHelper.quadraticHashIndex(idI, 1, S);

        assertEquals(addressI, idI);
        assertTrue(quadI > idI);
        assertTrue(HashHelper.quadraticHashIndex(idI, 2, S) > quadI);
    }

    @Test
    @DisplayName("Should compute a double hash index within legal bounds of the table size")
    void testDoubleHashIndex() {
        int doubleI = HashHelper.doubleHashIndex(keys[2], idI, 1, S);

        assertEquals(addressI, idI);
        assertNotEquals(doubleI, idI);
        assertTrue(doubleI >= 0);
        assertTrue(doubleI < S);
    }
}