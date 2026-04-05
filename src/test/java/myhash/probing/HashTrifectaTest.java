package myhash.probing;

import myhash.probing.mono.DoubleMap;
import myhash.probing.mono.LinearMap;
import myhash.probing.mono.MonoFlatMap;
import myhash.probing.mono.QuadraticMap;
import myhash.probing.poly.PolyFlatMap;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

class HashTrifectaTest {
    static int size = 262144;

    static Stream<Arguments> monoProvider() {
        return Stream.of(
                Arguments.of("MONO LINEAR   ", new LinearMap<Integer, Integer>(size)),
                Arguments.of("MONO QUADRATIC", new QuadraticMap<Integer, Integer>(size)),
                Arguments.of("MONO DOUBLE   ", new DoubleMap<Integer, Integer>(size))
        );
    }

    @ParameterizedTest(name = "Testing Mono Trifecta")
    @MethodSource("monoProvider")
    void finalMonoMetrics(String label, MonoFlatMap<Integer, Integer> monoMap) { // Use BaseMap here
        Integer[] keys = new Integer[size];
        StringBuilder sb = new StringBuilder();

        double fillTo = 0.9;
        double deleteTo = 0.4;
        double refillFrom = 0.25;
        double refillTo = 0.9;

        sb.append("\n--- ").append(label).append(" ---\n");

        // 1. Initial Fill
        for (int i = 0; i < size * fillTo; i++) {
            keys[i] = i;
            monoMap.put(i, i);
        }
        sb.append(String.format("Initial Fill (90%%): Search: %.2f | Storage: %.2f\n",
                monoMap.searchAverage(), monoMap.storageAverage()));

        // 2. High Deletion (Churn)
        for (int i = 0; i < size * deleteTo; i++) {
            monoMap.remove(keys[i]);
        }
        sb.append(String.format("Post-Delete (40%%):  Search: %.2f | Storage: %.2f\n",
                monoMap.searchAverage(), monoMap.storageAverage()));

        // 3. The Refill ("Graveyard" test)
        for (int i = (int) Math.floor(size * refillFrom); i < size * refillTo; i++) {
            keys[i] = i;
            monoMap.put(i, i);
        }
        sb.append(String.format("Final Refill (90%%): Search: %.2f | Storage: %.2f\n",
                monoMap.searchAverage(), monoMap.storageAverage()));

        IO.println(sb.toString());
    }

    static Stream<Arguments> polyProvider() {
        return Stream.of(
                Arguments.of("POLY LINEAR   ", new PolyFlatMap<String, String>(PolyFlatMap.Probe.LINEAR, size)),
                Arguments.of("POLY QUADRATIC", new PolyFlatMap<String, String>(PolyFlatMap.Probe.QUADRATIC, size)),
                Arguments.of("POLY DOUBLE   ", new PolyFlatMap<String, String>(PolyFlatMap.Probe.DOUBLE, size))
        );
    }

    @ParameterizedTest(name = "Testing Poly Trifecta")
    @MethodSource("polyProvider")
    void finalPolyMetrics(String label, PolyFlatMap<Integer, Integer> polyMap) { // Use BaseMap here
        Integer[] keys = new Integer[size];
        StringBuilder sb = new StringBuilder();

        double fillTo = 0.9;
        double deleteTo = 0.4;
        double refillFrom = 0.25;
        double refillTo = 0.9;

        sb.append("\n--- ").append(label).append(" ---\n");

        // 1. Initial Fill
        for (int i = 0; i < size * fillTo; i++) {
            keys[i] = i;
            polyMap.put(i, i);
        }
        sb.append(String.format("Initial Fill (90%%): Search: %.2f | Storage: %.2f\n",
                polyMap.searchAverage(), polyMap.storageAverage()));

        // 2. High Deletion (Churn)
        for (int i = 0; i < size * deleteTo; i++) {
            polyMap.remove(keys[i]);
        }
        sb.append(String.format("Post-Delete (40%%):  Search: %.2f | Storage: %.2f\n",
                polyMap.searchAverage(), polyMap.storageAverage()));

        // 3. The Refill ("Graveyard" test)
        for (int i = (int) Math.floor(size * refillFrom); i < size * refillTo; i++) {
            keys[i] = i;
            polyMap.put(i, i);
        }
        sb.append(String.format("Final Refill (90%%): Search: %.2f | Storage: %.2f\n",
                polyMap.searchAverage(), polyMap.storageAverage()));

        IO.println(sb.toString());
    }
}