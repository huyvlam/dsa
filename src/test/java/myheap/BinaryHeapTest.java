package myheap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class BinaryHeapTest {
    static int capacity = 100;
    int[] data;

    @BeforeEach
    public void setup() {
        data = new int[capacity];
        Random rand = new Random();
        for (int i = 0; i < capacity; i++) {
            data[i] = rand.nextInt();
        }
    }

    static Stream<Arguments> heapProvider() {
        return Stream.of(
                Arguments.of("MAX HEAP", new MaxBinaryHeap(capacity)),
                Arguments.of("MIN HEAP", new MinBinaryHeap(capacity))
        );
    }

    @ParameterizedTest(name = "Should insert element in max/min heap order")
    @MethodSource("heapProvider")
    void testInsert(String streamArg, BinaryHeap heap) {
        for (int d : data) heap.insert(d);
        for (int i = capacity / 2; i > 0; i--) {
            int child = heap.root[i];
            int parent = heap.root[(i - 1) / 2];
            if (streamArg.equals("MAX HEAP")) {
                assertTrue(parent >= child);
            }
            if (streamArg.equals("MIN HEAP")) {
                assertTrue(parent <= child);
            }
        }
    }

    @ParameterizedTest(name = "Should retrieve, replace, remove the root element")
    @MethodSource("heapProvider")
    void testPeekPopReplace(String streamArg, BinaryHeap heap) {
        for (int d : data) heap.insert(d);
        if (streamArg.equals("MIN HEAP")) {
            Arrays.sort(data);
            int smallest = data[0];
            assertEquals(smallest, heap.pop());
            assertEquals(heap.peek(), heap.replace(10));
        }
        if (streamArg.equals("MAX HEAP")) {
            Arrays.sort(data);
            int largest = data[capacity - 1];
            assertEquals(largest, heap.pop());
            assertEquals(heap.peek(), heap.replace(-10));
        }
    }

    @ParameterizedTest(name = "Should sort elements in max/min heap order")
    @MethodSource("heapProvider")
    void testSort(String streamArg, BinaryHeap heap) {
        for (int d : data) heap.insert(d);
        if (streamArg.equals("MAX HEAP")) {
            Arrays.sort(data);
            heap.sort();

            for (int i = 0; i < capacity; i++) {
                assertEquals(data[i], heap.root[i]);
            }
            assertThrows(IllegalStateException.class, heap::pop);

            heap.rebuild();
            for (int i = capacity / 2; i > 0; i--) {
                int child = heap.root[i];
                int parent = heap.root[(i - 1) / 2];
                assertTrue(parent >= child);
            }
        }
        if (streamArg.equals("MIN HEAP")) {
            Arrays.sort(data);
            heap.sort();

            for (int i = 0; i < capacity; i++) {
                assertEquals(data[capacity - 1 - i], heap.root[i]);
            }
            assertThrows(IllegalStateException.class, heap::pop);

            heap.rebuild();
            for (int i = capacity / 2; i > 0; i--) {
                int child = heap.root[i];
                int parent = heap.root[(i - 1) / 2];
                assertTrue(parent <= child);
            }
        }
    }

    @ParameterizedTest(name = "Should build/merge random array into a max/min heap")
    @MethodSource("heapProvider")
    void testBuildMerge(String streamArg, BinaryHeap heap) {
        data = new int[capacity];
        Random rand = new Random();
        for (int i = 0; i < capacity; i++) data[i] = rand.nextInt();

        if (streamArg.equals("MAX HEAP")) {
            heap.build(data, capacity);
            for (int i = capacity / 2; i > 0; i--) {
                int child = heap.root[i];
                int parent = heap.root[(i - 1) / 2];
                assertTrue(parent >= child);
            }

            for (int i = 0; i < capacity; i++) data[i] = rand.nextInt();

            heap.merge(data, 0, capacity - 1);
            for (int i = capacity; i > 0; i--) {
                int child = heap.root[i];
                int parent = heap.root[(i - 1) / 2];
                assertTrue(parent >= child);
            }
        }
        if (streamArg.equals("MIN HEAP")) {
            heap.build(data, capacity);
            for (int i = capacity / 2; i > 0; i--) {
                int child = heap.root[i];
                int parent = heap.root[(i - 1) / 2];
                assertTrue(parent <= child);
            }

            for (int i = 0; i < capacity; i++) data[i] = rand.nextInt();

            heap.merge(data, 0, capacity - 1);
            for (int i = capacity; i > 0; i--) {
                int child = heap.root[i];
                int parent = heap.root[(i - 1) / 2];
                assertTrue(parent <= child);
            }
        }
    }
}
