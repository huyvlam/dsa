package myheap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

class VirtualBinaryHeapTest {
    int size = 200;
    int[] a, b;

    @BeforeEach
    void setUp() {
        a = new int[size];
        b = new int[size / 2];
        Random rand = new Random();
        for (int i = 0; i < a.length; i++) a[i] = rand.nextInt();
        for (int j = 0; j < b.length; j++) b[j] = rand.nextInt();
    }

    @Test
    @DisplayName("Should rearrange two arrays into one contiguous block of max heap")
    void testMaxMerge() {
        VirtualBinaryHeap.mergeMax(a, b);

        int n = a.length + b.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            int child = VirtualBinaryHeap.get(a, b, i);
            int par = VirtualBinaryHeap.get(a, b, (i - 1) / 2);
            assertTrue(par >= child);
        }
    }

    @Test
    @DisplayName("Should rearrange two arrays into one contiguous block of min heap")
    void testMinMerge() {
        VirtualBinaryHeap.mergeMin(a, b);

        int n = a.length + b.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            int child = VirtualBinaryHeap.get(a, b, i);
            int par = VirtualBinaryHeap.get(a, b, (i - 1) / 2);
            assertTrue(par <= child);
        }
    }

    @Test
    @DisplayName("Should sort two arrays into one contiguous block of max heap")
    void testMaxSort() {
        VirtualBinaryHeap.sortMax(a, b);
        int[] expected = BinaryHeapUtil.mergeMax(a, b);
        BinaryHeapUtil.sortMax(expected, expected.length);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], VirtualBinaryHeap.get(a, b, i));
        }
    }

    @Test
    @DisplayName("Should sort two arrays into one contiguous block of min heap")
    void testMinSort() {
        VirtualBinaryHeap.sortMin(a, b);
        int[] expected = BinaryHeapUtil.mergeMin(a, b);
        BinaryHeapUtil.sortMin(expected, expected.length);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], VirtualBinaryHeap.get(a, b, i));
        }
    }
}