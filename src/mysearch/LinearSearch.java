package mysearch;

import myhelper.Checker;

import java.util.Comparator;

/**
 * Pairwise comparison linear search for unsorted array
 */
public class LinearSearch {
    /**
     * Find the index of given element in the array
     * @param arr array of elements to be searched
     * @param value target element
     * @param comp custom comparator, if null the method resolve to natural order
     * @return the index of given element, or -1 if not found
     * @param <E> type of element
     */
    public static <E> int findIndex(E[] arr, E value, Comparator<? super E> comp) {
        if (arr == null || value == null) return -1;

        int n = arr.length;
        if (n == 0) return -1;

        Comparator<? super E> safeComp = Checker.nullsLastComparator(comp);
        int i = 0;

        if (n % 2 == 1) {
            if (arr[0] != null && safeComp.compare(arr[0], value) == 0) return 0;
            i = 1;
        }

        while (i < n - 1) {
            if (arr[i] != null && safeComp.compare(arr[i], value) == 0) return i;
            if (arr[i + 1] != null && safeComp.compare(arr[i + 1], value) == 0) return i + 1;

            i += 2;
        }

        return -1;
    }

    /**
     * Search for min/max elements in the given array
     * @param arr array of elements to search
     * @param comp custom comparator for generic elements
     *             Comparator.naturalOrder() may be used for elements w/ natural ordering
     * @return Pair<T> with min/max field
     * @param <E> type of element
     */
    public static <E> Pair<E> findMinMax(E[] arr, Comparator<? super E> comp) {
        if (arr == null) return null;

        int n = arr.length;
        if (n == 0) return null;

        Comparator<? super E> safeComp = Checker.nullsLastComparator(comp);
        Pair<E> result = new Pair<>();
        int i;

        // Initialize min/max
        if (n % 2 == 0) { // if number of elements are even
            if (safeComp.compare(arr[0], arr[1]) > 0) {
                result.max = arr[0];
                result.min = arr[1];
            } else {
                result.max = arr[1];
                result.min = arr[0];
            }
            i = 2;
        } else {
            result.max = arr[0];
            result.min = arr[0];
            i = 1;
        }

        // compare in pair
        while (i < n - 1) {
            if (safeComp.compare(arr[i], arr[i + 1]) > 0) {
                if (safeComp.compare(arr[i], result.max) > 0)
                    result.max = arr[i];
                if (safeComp.compare(arr[i+ 1], result.min) < 0)
                    result.min = arr[i + 1];
            } else {
                if (safeComp.compare(arr[i + 1], result.max) > 0)
                    result.max = arr[i + 1];
                if (safeComp.compare(arr[i], result.min) < 0)
                    result.min = arr[i];
            }
            i += 2;
        }

        return result;
    }
}
