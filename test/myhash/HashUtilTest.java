package myhash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class HashUtilTest<K, V> {
    private String[] keys;
    private int S, addressI, idI;

    @BeforeEach
    void setup() {
        keys = new String[]{"address","name","id","city","profession","phone"};
        S = keys.length;
        addressI = HashUtil.hashIndex(keys[0], S);
        idI = HashUtil.hashIndex(keys[2], S);
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
        int stride = HashUtil.stride(keys[2]);
        int doubleI = HashUtil.doubleHashIndex(idI, 1, stride, S);

        assertEquals(addressI, idI);
        assertNotEquals(doubleI, idI);
        assertTrue(doubleI >= 0);
        assertTrue(doubleI < S);
    }
}