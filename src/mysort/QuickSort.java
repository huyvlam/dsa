package mysort;

import java.util.Arrays;
import java.util.Comparator;

public class QuickSort {
    public static <E> void sort(E[] arr, int lo, int hi, Comparator<? super E> comp) {
        if (lo >= hi || arr == null) return;

        // Handle null data inside array
        Comparator<? super E> safeComp = (comp == null)
                ? (Comparator<E>) Comparator.nullsLast(Comparator.naturalOrder())
                : Comparator.nullsLast(comp);

        recursiveSort(arr, lo, hi, safeComp);
    }

    private static <E> void recursiveSort(E[] arr, int lo, int hi, Comparator<? super E> comp) {
        // 1. Partition the array until it becomes a single element
        if (lo < hi) {
            // This behaves like the root in red black tree
            int p = partition(arr, lo, hi, comp);

            // 2. Use returned pivot point to recur sorting left/right half
            recursiveSort(arr, lo, p - 1, comp); // smaller elements
            recursiveSort(arr, p + 1, hi, comp); // larger elements
        }
    }

    private static <E> int partition(E[] arr, int lo, int hi, Comparator<? super E> comp) {
        // 3. Use the middle pivot to avoid O(n2) on sorted array
        int mid = lo + (hi - lo) / 2;
        swap(arr, mid, hi);

        E pivot = arr[hi];
        int i = lo - 1; // Index of element recently swapped

        for (int j = lo; j < hi; j++) {
            // 4. If current element is smaller, move it to the left
            if (comp.compare(arr[j], pivot) < 0) {
                i++;
                swap(arr, i, j);
            }
        }

        // 5. Place leftmost element after the element recently swapped
        swap(arr, i + 1, hi);

        // 6. Return the pivot position to use as starting point for subsequent partition
        return i + 1;
    }

    private static <E> void swap(E[] arr, int a, int b) {
        E temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}
