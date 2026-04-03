package myhash.probing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;

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
    @DisplayName("Should print out the performance stats for all three strategies")
    public void compareStrategies() {
        int cap = (int) Math.pow(2, 14);
        FlatMap<Integer, Integer> linearM = new FlatMap<>(FlatMap.Probe.LINEAR, cap);
        FlatMap<Integer, Integer> quadraticM = new FlatMap<>(FlatMap.Probe.QUADRATIC, cap);
        FlatMap<Integer, Integer> doubleM = new FlatMap<>(FlatMap.Probe.DOUBLE, cap);

        // Fill both to 80% capacity
        for(int i = 0; i < cap * .8; i++) {
            linearM.put(i, i);
            quadraticM.put(i, i);
            doubleM.put(i, i);
        }

        System.out.println("Linear Avg Probe: " + linearM.averageProbeLength());
        System.out.println("Quadratic Avg Probe: " + quadraticM.averageProbeLength());
        System.out.println("Double Avg Probe: " + doubleM.averageProbeLength());
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