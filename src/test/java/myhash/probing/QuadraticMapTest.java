package myhash.probing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuadraticMapTest {
    private QuadraticMap<String, String> sMap;
    private QuadraticMap<Integer, Integer> iMap;
    private StringBuilder builder;
    private Integer[] keys;
    private int initCap;

    @BeforeEach
    void setUp() {
        initCap = 4;
        sMap = new QuadraticMap<>(initCap);
    }

    @Test
    @DisplayName("Should throw exception when capacity is a negative number")
    void testCapacityException() {
        assertThrows(IllegalArgumentException.class, () -> new QuadraticMap<>(-2));
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

//    @Test
//    @DisplayName("Should print the performance metrics for quadratic probe")
//    void printQuadraticMetrics() {
//        int size = 262144;
//        int i;
//        iMap = new QuadraticMap<>(size);
//        keys = new Integer[size];
//        builder = new StringBuilder();
//
//        double fillTo = 0.9;
//        double deleteTo = 0.4;
//        double refillTo = 0.9;
//        double refillFrom = 0.25;
//
//        for (i = 0; i < size * fillTo; i++) {
//            keys[i] = i;
//            iMap.put(i, i);
//        }
//        builder.append("Fill avg: ").append(iMap.searchAverage()).append(" (search) ")
//                .append(iMap.storageAverage()).append(" (storage)\n");
//
//        for (i = 0; i < size * deleteTo; i++) {
//            iMap.remove(keys[i]);
//        }
//        builder.append("Delete avg: ").append(iMap.searchAverage()).append(" (search) ")
//                .append(iMap.storageAverage()).append(" (storage)\n");
//
//        for (i = (int) Math.floor(size * refillFrom); i < size * refillTo; i++) {
//            keys[i] = i;
//            iMap.put(i, i);
//        }
//        builder.append("Refill avg: ").append(iMap.searchAverage()).append(" (search) ")
//                .append(iMap.storageAverage()).append(" (storage)\n");
//
//        IO.println(builder.toString());
//    }
}