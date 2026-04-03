package myhash.probing;

import myhash.HashUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class FlatUtilTest<K, V> {
    private FlatNode<K, V>[] table;
    private String[] keys;
    private int keysSize, addIndex, idIndex;

    @BeforeEach
    void setUp() {
        table = new FlatNode[4];
        keys = new String[]{"address","name","id","city","profession","phone"};
        keysSize = keys.length;
        addIndex = HashUtil.hashIndex(keys[0], keysSize);
        idIndex = HashUtil.hashIndex(keys[2], keysSize);
    }

    @Test
    @DisplayName("Should add/update data in available slot")
    void testProbeAddUpdate() {
        assertNull(FlatUtil.probe((K) "apple", (V) "red", table, FlatUtil.LINEAR, null));
        assertNull(FlatUtil.probe((K) "banana", (V) "yellow", table, FlatUtil.LINEAR, null));
        assertNull(FlatUtil.probe((K) "peach", (V) "orange", table, FlatUtil.LINEAR, null));
        assertNull(FlatUtil.probe((K) "kiwi", (V) "green", table, FlatUtil.LINEAR, null));

        assertNotNull(table[0]);
        assertNotNull(table[1]);
        assertNotNull(table[2]);
        assertNotNull(table[3]);

        assertEquals("green", FlatUtil.probe((K) "kiwi", (V) "yellow", table, FlatUtil.LINEAR, null));
    }


    @Test
    @DisplayName("Should reuse deleted slot to add new data")
    void testProbeReuseTombstone() {
        assertNull(FlatUtil.probe((K) "apple", (V) "red", table, FlatUtil.LINEAR, null));
        assertNull(FlatUtil.probe((K) "banana", (V) "yellow", table, FlatUtil.LINEAR, null));
        assertNull(FlatUtil.probe((K) "peach", (V) "orange", table, FlatUtil.LINEAR, null));
        assertNull(FlatUtil.probe((K) "kiwi", (V) "green", table, FlatUtil.LINEAR, null));

        FlatNode<K, V> node = table[1];
        node.key = null;
        node.value = null;
        node.deleted = true;

        assertNull(FlatUtil.probe((K) "berry", (V) "blue", table, FlatUtil.LINEAR, null));
        assertEquals("blue", FlatUtil.probe((K) "berry", (V) "black", table, FlatUtil.LINEAR, null));
    }

    @Test
    @DisplayName("Should throw exception when the table is full")
    void testProbeCapacityException() {
        assertNull(FlatUtil.probe((K) "apple", (V) "red", table, FlatUtil.LINEAR, null));
        assertNull(FlatUtil.probe((K) "banana", (V) "yellow", table, FlatUtil.LINEAR, null));
        assertNull(FlatUtil.probe((K) "peach", (V) "orange", table, FlatUtil.LINEAR, null));
        assertNull(FlatUtil.probe((K) "kiwi", (V) "green", table, FlatUtil.LINEAR, null));

        int[] tracker = new int[1];
        assertThrows(IllegalStateException.class, () -> FlatUtil.probe((K) "berry", (V) "blue", table, FlatUtil.LINEAR, tracker));
        assertTrue(tracker[0] > 0);
    }

    @Test
    @DisplayName("Should perform custom action on each returned node")
    void testProbeCustomAction() {
        FlatUtil.probe((K) "apple", (V) "red", table, FlatUtil.LINEAR, null);
        FlatUtil.probe((K) "banana", (V) "yellow", table, FlatUtil.LINEAR, null);
        FlatUtil.probe((K) "peach", (V) "orange", table, FlatUtil.LINEAR, null);
        FlatUtil.probe((K) "kiwi", (V) "green", table, FlatUtil.LINEAR, null);

        V[] result = (V[]) new Object[]{null};

        K unknownKey = (K) "berry";

        FlatUtil.probe(unknownKey, table, FlatUtil.LINEAR, (node) -> {
            if (Objects.equals(unknownKey, node.key)) {
                result[0] = node.value;
                return false;
            }
            return true;
        }, null);
        assertNull(result[0]);

        int i = 3;
        K tableKey = table[i].key;
        V tableValue = table[i].value;

        FlatUtil.probe(tableKey, table, FlatUtil.LINEAR, (node) -> {
            if (Objects.equals(tableKey, node.key)) {
                result[0] = node.value;
                return false;
            }
            return true;
        }, null);
        assertEquals(tableValue, result[0]);
    }

    @Test
    @DisplayName("Both probe methods should have parallel hash collision result")
    void testBothProbesShouldHaveParallelHashResult() {
        FlatUtil.probe((K) "apple", (V) "red", table, FlatUtil.LINEAR, null);
        FlatUtil.probe((K) "banana", (V) "yellow", table, FlatUtil.LINEAR, null);
        FlatUtil.probe((K) "peach", (V) "orange", table, FlatUtil.LINEAR, null);
        FlatUtil.probe((K) "kiwi", (V) "green", table, FlatUtil.LINEAR, null);

        for (int i = 0; i < table.length; i++) {
            V[] result = (V[]) new Object[]{null};
            K tableKey = table[i].key;
            V tableValue = table[i].value;

            FlatUtil.probe(tableKey, table, FlatUtil.LINEAR, (node) -> {
                if (Objects.equals(tableKey, node.key)) {
                    result[0] = node.value;
                    return false;
                }
                return true;
            }, null);

            assertEquals(tableValue, result[0]);
        }
    }

    @Test
    @DisplayName("Should linear probe to find available slot")
    void testLinearIndex() {
        int linearI = FlatUtil.linearIndex(idIndex, 1, keysSize);

        assertEquals(addIndex, idIndex);
        assertTrue(linearI > idIndex);
        assertTrue(FlatUtil.linearIndex(idIndex, 2, keysSize) > linearI);
    }

    @Test
    @DisplayName("Should quadratic probe to find available slot")
    void testQuadraticIndex() {
        int quadI = FlatUtil.quadraticIndex(idIndex, 1, keysSize);

        assertEquals(addIndex, idIndex);
        assertTrue(quadI > idIndex);
        assertTrue(FlatUtil.quadraticIndex(idIndex, 2, keysSize) > quadI);
    }

    @Test
    @DisplayName("Should double hash to find available slot")
    void testDoubleIndex() {
        int stride = FlatUtil.stride(keys[2]);
        int doubleI = FlatUtil.doubleIndex(idIndex, 1, stride, keysSize);

        assertEquals(addIndex, idIndex);
        assertNotEquals(doubleI, idIndex);
        assertTrue(doubleI >= 0);
        assertTrue(doubleI < keysSize);
    }

    @Test
    @DisplayName("Should compute a positive odd number for a Power of 2 table")
    void testStrideHash2() {
        int stride = FlatUtil.stride(keys[2]);

        assertTrue(stride > 0);
        assertTrue(stride % 2 != 0);
    }
}