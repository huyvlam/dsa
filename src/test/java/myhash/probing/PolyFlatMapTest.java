package myhash.probing;

import myhash.probing.poly.PolyFlatMap;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

class PolyFlatMapTest {
    static int capacity = 4;

    static Stream<Arguments> mapProvider() {
        return Stream.of(
                Arguments.of("LINEAR   ", new PolyFlatMap<String, String>(PolyFlatMap.Probe.LINEAR, capacity)),
                Arguments.of("QUADRATIC", new PolyFlatMap<String, String>(PolyFlatMap.Probe.QUADRATIC, capacity)),
                Arguments.of("DOUBLE   ", new PolyFlatMap<String, String>(PolyFlatMap.Probe.DOUBLE, capacity))
        );
    }

    @ParameterizedTest(name = "Should throw exception when capacity is negative")
    @MethodSource("mapProvider")
    void testIllegalCapacityException(String label, PolyFlatMap<String, String> map) {
        assertThrows(IllegalArgumentException.class, () -> new PolyFlatMap<>(PolyFlatMap.Probe.LINEAR, -2));
        assertThrows(IllegalArgumentException.class, () -> new PolyFlatMap<>(PolyFlatMap.Probe.QUADRATIC, -2));
        assertThrows(IllegalArgumentException.class, () -> new PolyFlatMap<>(PolyFlatMap.Probe.DOUBLE, -2));
    }

    @ParameterizedTest(name = "Should add data by key")
    @MethodSource("mapProvider")
    void testAddData(String label, PolyFlatMap<String, String> map) {
        assertNull(map.put("name", "jolly"));
        assertEquals("jolly", map.get("name"));
        assertEquals(1, map.size());
    }

    @ParameterizedTest(name = "Should update data by key")
    @MethodSource("mapProvider")
    void testUpdateData(String label, PolyFlatMap<String, String> map) {
        assertNull(map.put("name", "jolly"));
        assertEquals("jolly", map.put("name", "molly"));
        assertEquals("molly", map.get("name"));
    }

    @ParameterizedTest(name = "Should remove data by key")
    @MethodSource("mapProvider")
    void testRemoveData(String label, PolyFlatMap<String, String> map) {
        assertNull(map.put("name", "jolly"));
        assertEquals("jolly", map.remove("name"));
        assertNull(map.remove("name"));
        assertTrue(map.isEmpty());
    }

    @ParameterizedTest(name = "Should resize table as needed")
    @MethodSource("mapProvider")
    void testResizeTable(String label, PolyFlatMap<String, String> map) {
        map.put("name", "jolly");
        map.put("age", "20");
        map.put("hobby", "gaming");
        map.put("hair", "black");
        map.put("eyes", "black");

        assertTrue(map.size() > capacity);
        map.clear();
        assertTrue(map.isEmpty());
    }

    @ParameterizedTest(name = "Should check if the table contains key/value")
    @MethodSource("mapProvider")
    void testContainsKeyValue(String label, PolyFlatMap<String, String> map) {
        map.put("name", "jolly");
        map.put("age", "20");
        map.put("hobby", "gaming");

        assertTrue(map.containsKey("name"));
        assertFalse(map.containsKey("hair"));
        assertTrue(map.containsValue("gaming"));
        assertFalse(map.containsValue("34"));
    }
}