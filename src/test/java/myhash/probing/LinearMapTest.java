package myhash.probing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinearMapTest<K, V> {
    private LinearMap<String, String> sMap;
    private LinearMap<Integer, Integer> iMap;
    private Integer[] keys;
    private int initCap;

    @BeforeEach
    void setUp() {
        initCap = 4;
        sMap = new LinearMap<>(initCap);
    }

    @Test
    @DisplayName("Should throw exception when capacity is a negative number")
    void testCapacityNotPowerOf2() {
        assertThrows(IllegalArgumentException.class, () -> new LinearMap<>(-2));
    }

    @Test
    @DisplayName("Should add data by key")
    void testAddData() {
        assertNull(sMap.put("name", "nelly"));
        assertEquals("nelly", sMap.get("name"));
        assertEquals(1, sMap.size());
    }

    @Test
    @DisplayName("Should update data by key")
    void testUpdateData() {
        assertNull(sMap.put("name", "nelly"));
        assertEquals("nelly", sMap.put("name", "molly"));
        assertEquals("molly", sMap.get("name"));
    }

    @Test
    @DisplayName("Should remove data by key")
    void testRemoveData() {
        assertNull(sMap.put("name", "molly"));
        assertNull(sMap.remove("age"));
        assertEquals("molly", sMap.remove("name"));
        assertTrue(sMap.isEmpty());
    }

    @Test
    @DisplayName("Should resize table as needed")
    void testResizeTable() {
        sMap.put("name", "molly");
        sMap.put("age", "20");
        sMap.put("hobby", "yoga");
        sMap.put("hair", "black");
        sMap.put("eyes", "brown");

        assertTrue(sMap.size() > initCap);

        sMap.clear();

        assertTrue(sMap.isEmpty());
    }

    @Test
    @DisplayName("Should check whether the table contains key/value")
    void testContainsKeyValue() {
        sMap.put("name", "nelly");
        sMap.put("hair", "black");
        sMap.put("eyes", "black");

        assertTrue(sMap.containsKey("hair"));
        assertFalse(sMap.containsKey("hobby"));
        assertTrue(sMap.containsValue("black"));
        assertFalse(sMap.containsValue("brown"));
    }

    @Test
    @DisplayName("Should print out the probe average")
    void printProbeAverage() {
        int size = 262144;
        int i;
        iMap = new LinearMap<>(size);
        keys = new Integer[size];
        
        for (i = 0; i < size * 0.8; i++) {
            keys[i] = i;
            iMap.put(i, i);
        }
        IO.println("Put avg: " + iMap.probeAverage());

        for (i = 0; i < size * 0.5; i++) {
            iMap.remove(keys[i]);
        }
        IO.println("Remove avg: " + iMap.probeAverage());

        for (i = (int) Math.floor(size * 0.4); i < size * 0.9; i++) {
            keys[i] = i;
            iMap.put(i, i);
        }
        
        IO.println("Reuse avg: " + iMap.probeAverage());
    }
}