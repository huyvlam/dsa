package myhash.probing;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

class HashTrifectaTest {

    static Stream<Arguments> mapProvider() {
        int size = 262144;
        return Stream.of(
                Arguments.of("LINEAR   ", new LinearMap<Integer, Integer>(size)),
                Arguments.of("QUADRATIC", new QuadraticMap<Integer, Integer>(size)),
                Arguments.of("DOUBLE   ", new DoubleMap<Integer, Integer>(size))
        );
    }

    @ParameterizedTest(name = "Testing {0}")
    @MethodSource("mapProvider")
    void finalBossMetrics(String label, BaseProbeMap<Integer, Integer> iMap) { // Use BaseMap here
        int size = 262144;
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
            iMap.put(i, i);
        }
        sb.append(String.format("Initial Fill (90%%): Search: %.2f | Storage: %.2f\n",
                iMap.searchAverage(), iMap.storageAverage()));

        // 2. High Deletion (Churn)
        for (int i = 0; i < size * deleteTo; i++) {
            iMap.remove(keys[i]);
        }
        sb.append(String.format("Post-Delete (40%%):  Search: %.2f | Storage: %.2f\n",
                iMap.searchAverage(), iMap.storageAverage()));

        // 3. The Refill ("Graveyard" test)
        for (int i = (int) Math.floor(size * refillFrom); i < size * refillTo; i++) {
            keys[i] = i;
            iMap.put(i, i);
        }
        sb.append(String.format("Final Refill (90%%): Search: %.2f | Storage: %.2f\n",
                iMap.searchAverage(), iMap.storageAverage()));

        IO.println(sb.toString());
    }
}