package mysearch;

import myhelper.MyComparator;

import java.util.Comparator;

/**
 * Pairwise comparison linear search for unsorted array
 */
public class LinearSearch {
    /**
     * Find the index of given data in the unsorted array
     *
     * @param arr   array of unsorted elements to be searched
     * @param value target element to look for
     * @param lo    lower bound must not be less than 0
     * @param hi    upper bound must be less than the array size
     * @param comp  custom comparator for generics, or natural order for Comparables
     * @return      the position where the given data is found, or -1 if not found
     * @param <E>   type of element stored in the array
     */
    public static <E> int findIndex(E[] arr, E value, int lo, int hi, Comparator<? super E> comp) {
        if (arr == null || value == null || hi < lo) return -1;

        int n = hi - lo + 1;

        Comparator<? super E> safeComp = MyComparator.nullsLastComparator(comp);
        int i = lo;

        if (n % 2 == 1) {
            if (arr[lo] != null && safeComp.compare(arr[lo], value) == 0) return lo;
            i = lo + 1;
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

        Comparator<? super E> safeComp = MyComparator.nullsLastComparator(comp);
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
