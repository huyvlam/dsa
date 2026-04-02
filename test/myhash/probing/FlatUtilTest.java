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
    private int keysSize, addressI, idI;

    @BeforeEach
    void setUp() {
        table = new FlatNode[4];
        keys = new String[]{"address","name","id","city","profession","phone"};
        keysSize = keys.length;
        addressI = HashUtil.hashIndex(keys[0], keysSize);
        idI = HashUtil.hashIndex(keys[2], keysSize);
    }

    @Test
    @DisplayName("Should add and update given data in available slot")
    void testProbeAddUpdate() {
        assertNull(FlatUtil.probe((K) "apple", (V) "red", table, FlatUtil.LINEAR));
        assertNull(FlatUtil.probe((K) "banana", (V) "yellow", table, FlatUtil.LINEAR));
        assertNull(FlatUtil.probe((K) "peach", (V) "orange", table, FlatUtil.LINEAR));
        assertNull(FlatUtil.probe((K) "kiwi", (V) "green", table, FlatUtil.LINEAR));

        assertNotNull(table[0]);
        assertNotNull(table[1]);
        assertNotNull(table[2]);
        assertNotNull(table[3]);

        assertEquals("green", FlatUtil.probe((K) "kiwi", (V) "yellow", table, FlatUtil.LINEAR));
    }


    @Test
    @DisplayName("Should reuse deleted slot to add new data")
    void testProbeReuseTombstone() {
        assertNull(FlatUtil.probe((K) "apple", (V) "red", table, FlatUtil.LINEAR));
        assertNull(FlatUtil.probe((K) "banana", (V) "yellow", table, FlatUtil.LINEAR));
        assertNull(FlatUtil.probe((K) "peach", (V) "orange", table, FlatUtil.LINEAR));
        assertNull(FlatUtil.probe((K) "kiwi", (V) "green", table, FlatUtil.LINEAR));

        FlatNode<K, V> node = table[1];
        node.key = null;
        node.value = null;
        node.deleted = true;

        assertNull(FlatUtil.probe((K) "berry", (V) "blue", table, FlatUtil.LINEAR));
        assertEquals("blue", FlatUtil.probe((K) "berry", (V) "black", table, FlatUtil.LINEAR));
    }

    @Test
    @DisplayName("Should throw exception when the table is full")
    void testProbeCapacityException() {
        assertNull(FlatUtil.probe((K) "apple", (V) "red", table, FlatUtil.LINEAR));
        assertNull(FlatUtil.probe((K) "banana", (V) "yellow", table, FlatUtil.LINEAR));
        assertNull(FlatUtil.probe((K) "peach", (V) "orange", table, FlatUtil.LINEAR));
        assertNull(FlatUtil.probe((K) "kiwi", (V) "green", table, FlatUtil.LINEAR));

        assertThrows(IllegalStateException.class, () -> FlatUtil.probe((K) "berry", (V) "blue", table, FlatUtil.LINEAR));
    }

    @Test
    @DisplayName("Should perform custom action on each returned node")
    void testProbeCustomAction() {
        FlatUtil.probe((K) "apple", (V) "red", table, FlatUtil.LINEAR);
        FlatUtil.probe((K) "banana", (V) "yellow", table, FlatUtil.LINEAR);
        FlatUtil.probe((K) "peach", (V) "orange", table, FlatUtil.LINEAR);
        FlatUtil.probe((K) "kiwi", (V) "green", table, FlatUtil.LINEAR);

        V[] result = (V[]) new Object[]{null};

        K unknownKey = (K) "berry";

        FlatUtil.probe(unknownKey, table, FlatUtil.LINEAR, (node) -> {
            if (Objects.equals(unknownKey, node.key)) {
                result[0] = node.value;
                return false;
            }
            return true;
        });
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
        });
        assertEquals(tableValue, result[0]);
    }

    @Test
    @DisplayName("Both probe methods should have parallel hash collision result")
    void testBothProbesShouldHaveParallelHashResult() {
        FlatUtil.probe((K) "apple", (V) "red", table, FlatUtil.LINEAR);
        FlatUtil.probe((K) "banana", (V) "yellow", table, FlatUtil.LINEAR);
        FlatUtil.probe((K) "peach", (V) "orange", table, FlatUtil.LINEAR);
        FlatUtil.probe((K) "kiwi", (V) "green", table, FlatUtil.LINEAR);

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
            });

            assertEquals(tableValue, result[0]);
        }
    }


    @Test
    @DisplayName("Should compute a linear hash index greater than the original index")
    void testLinearHashIndex() {
        int linearI = FlatUtil.linearHashIndex(idI, 1, keysSize);

        assertEquals(addressI, idI);
        assertTrue(linearI > idI);
        assertTrue(FlatUtil.linearHashIndex(idI, 2, keysSize) > linearI);
    }

    @Test
    @DisplayName("Should compute a quadratic hash index greater than the original index")
    void testQuadraticHashIndex() {
        int quadI = FlatUtil.quadraticHashIndex(idI, 1, keysSize);

        assertEquals(addressI, idI);
        assertTrue(quadI > idI);
        assertTrue(FlatUtil.quadraticHashIndex(idI, 2, keysSize) > quadI);
    }

    @Test
    @DisplayName("Should compute a double hash index within legal bounds of the table size")
    void testDoubleHashIndex() {
        int stride = FlatUtil.stride(keys[2]);
        int doubleI = FlatUtil.doubleHashIndex(idI, 1, stride, keysSize);

        assertEquals(addressI, idI);
        assertNotEquals(doubleI, idI);
        assertTrue(doubleI >= 0);
        assertTrue(doubleI < keysSize);
    }
}