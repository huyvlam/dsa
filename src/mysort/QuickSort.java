package mysort;

import myhelper.Checker;

import java.util.Comparator;

public class QuickSort<T extends Comparable<T>> {
    public static <T> void sort(T[] arr, Comparator<? super T> comp) {
        Checker.checkNullArgument(comp);

        int n = arr.length;
        if (n <= 1) return;

        recurseSort(arr, 0, n - 1, comp);
    }

    private static <T> void recurseSort(T[] arr, int lo, int hi, Comparator<? super T> comp) {
        // 1. Partition the array until it becomes a single element
        if (lo < hi) {
            // Note: Pivot point is equivalent to the root in red black tree
            int p = partition(arr, lo, hi, comp);

            // 2. Use returned pivot point to recurse sorting left/right half
            recurseSort(arr, lo, p - 1, comp); // smaller elements
            recurseSort(arr, p + 1, hi, comp); // larger elements
        }
    }

    private static <T> int partition(T[] arr, int lo, int hi, Comparator<? super T> comp) {
        Comparator<? super T> safeComp = Comparator.nullsLast(comp);

        // 3. We use the last element as pivot
        T pivotElement = arr[hi];

        // 4. Initialize the position of swapped element (we start out of bound since no swap occurs initially)
        int swapIndex = lo - 1;

        // 5. Iterate left to right
        for (int j = lo; j < hi; j++) {
            // 6. If current element is the winner, pivot to the left
            if (safeComp.compare(arr[j], pivotElement) < 0) {
                swapIndex++; // move swap position forward

                T temp = arr[swapIndex];

                // Move the winner into swap position
                arr[swapIndex] = arr[j];
                arr[j] = temp;
            }
        }

        int pivotIndex = swapIndex + 1;
        T temp = arr[pivotIndex];

        // 5. Move pivot element right behind the last element that was swapped
        // All elements left of pivot are smaller, right of pivot are larger
        arr[pivotIndex] = arr[hi];
        arr[hi] = temp;

        // 6. Return the pivot position to use as starting point for subsequent partition
        return pivotIndex;
    }
}
