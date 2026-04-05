package myhash;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashUtilTest<K, V> {
    @Test
    @DisplayName("Should compute a hash index within bounds of the table size")
    void testHashIndex() {
        String keyS = "address";
        int size = 4;
        int mask = size - 1;

        int index = HashUtil.hashIndex(keyS, mask);
        assertTrue(index >= 0);
        assertTrue(index < size);
        assertEquals(0, HashUtil.hashIndex(null, mask));
    }

    @Test
    @DisplayName("Should compute invalid output if table size is not power of 2")
    void testPowerOf2TableSize() {
        int pow2 = 2;
        int notPow2 = 3;
        int res = HashUtil.tableSize(notPow2);

        assertEquals(pow2, HashUtil.tableSize(pow2));
        assertTrue(res != notPow2);
        assertTrue((res & (res - 1)) == 0);
    }

    @Test
    @DisplayName("Should compute an odd number greater than 0")
    void testStrideHash2() {
        int stride = HashUtil.stride("cardamom");

        assertTrue(stride > 0);
        assertTrue((stride & (stride - 1)) != 0);
    }
}