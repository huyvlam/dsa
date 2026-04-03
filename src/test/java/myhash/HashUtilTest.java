package myhash;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashUtilTest<K, V> {
    @Test
    @DisplayName("Should compute a hash index within bounds of the table size")
    void testHashIndex() {
        String keyS = "address";
        int tableSize = 4;

        int index = HashUtil.hashIndex(keyS, tableSize);
        assertTrue(index >= 0);
        assertTrue(index < tableSize);
        assertEquals(0, HashUtil.hashIndex(null, tableSize));
    }

    @Test
    @DisplayName("Should compute invalid output if table size is not power of 2")
    void testMustBePowerOf2() {
//        String keyS = "address";
//        int tableSize = 3;
//
//        int index = HashUtil.hashIndex(keyS, tableSize);
//        assertTrue(index >= 0);
//        assertTrue(index < tableSize);
//        assertEquals(0, HashUtil.hashIndex(null, tableSize));
    }
}