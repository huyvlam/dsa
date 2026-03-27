package mysort;

import java.util.Comparator;

public class QuickSort {
    public static <T> void sort(T[] arr, int lo, int hi, Comparator<? super T> comp) {
        if (lo >= hi || arr == null) return;

        // Handle null data inside array
        Comparator<? super T> safeComp = (comp == null)
                ? (Comparator<T>) Comparator.nullsLast(Comparator.naturalOrder())
                : Comparator.nullsLast(comp);

        recursiveSort(arr, lo, hi, safeComp);
    }

    private static <T> void recursiveSort(T[] arr, int lo, int hi, Comparator<? super T> comp) {
        // 1. Partition the array until it becomes a single element
        if (lo < hi) {
            // Pivot point behaves like the root in red black tree
            int p = partition(arr, lo, hi, comp);

            // 2. Use returned pivot point to recur sorting left/right half
            recursiveSort(arr, lo, p - 1, comp); // smaller elements
            recursiveSort(arr, p + 1, hi, comp); // larger elements
        }
    }

    private static <T> int partition(T[] arr, int lo, int hi, Comparator<? super T> comp) {
        // 3. Use the middle as pivot
        int mid = lo + (hi - lo) / 2;
        swap(arr, mid, hi);

        T pivot = arr[hi];
        int i = lo - 1; // Index of element recently swapped

        // 4. Iterate left to right
        for (int j = lo; j < hi; j++) {
            // 5. If current element is the winner, move it to the left
            if (comp.compare(arr[j], pivot) < 0) {
                i++;
                swap(arr, i, j);
            }
        }

        // 5. Move leftmost element right behind the element recently swapped
        swap(arr, i + 1, hi);

        // 6. Return the pivot position to use as starting point for subsequent partition
        return i + 1;
    }

    private static <T> void swap(T[] arr, int a, int b) {
        T temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}
