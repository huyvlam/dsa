package myheap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;


class BinaryHeapUtilTest {
    Random rand;
    int[] arr;
    int n;

    @BeforeEach
    void setUp() {
        n = 15;
        arr = new int[n];
        rand = new Random();
    }

    @Test
    @DisplayName("Should turn unordered array into a heap structure")
    void testBuildHeap() {
        for (int i = 0; i < n; i++) {
            arr[i] = rand.nextInt(30);
        }

        BinaryHeapUtil.buildMaxHeap(arr, n);
        int i = n - 1;
        while (true) {
            int p = (i - 1) / 2;
            if (p < n) break;
            assertTrue(arr[p] > arr[i]);
            i = p;
        }

        BinaryHeapUtil.buildMinHeap(arr, n);
        int j = n - 1;
        while (true) {
            int p = (j - 1) / 2;
            if (p < n) return;
            assertTrue(arr[p] < arr[j]);
            j = p;
        }
    }

    @Test
    @DisplayName("Should push max/min element toward the top")
    void testBubbleUp() {
        arr[0] = -5;
        arr[1] = 6;
        arr[4] = 13;
        arr[9] = 27;

        BinaryHeapUtil.bubbleMax(arr, 9);

        assertEquals(27, arr[0]);
        assertEquals(-5, arr[1]);
        assertEquals(6, arr[4]);
        assertEquals(13, arr[9]);

        BinaryHeapUtil.bubbleMin(arr, 1);

        assertEquals(-5, arr[0]);
        assertEquals(27, arr[1]);
        assertEquals(6, arr[4]);
        assertEquals(13, arr[9]);
    }
}