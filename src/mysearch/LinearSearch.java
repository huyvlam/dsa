package mysearch;

import java.util.Comparator;

public class LinearSearch {
    /**
     * Pairwise comparison search for min/max values
     * @param arr array of elements to search
     * @param comp custom comparator for generic elements
     *             elements w/ natural ordering can use Comparator.naturalOrder()
     * @return Pair<T> with min/max field
     * @param <T> type of element being searched
     */
    public static <T> Pair<T> findMinMax(T[] arr, Comparator<? super T> comp) {
        int n = arr.length;
        if (n == 0) return null;

        Pair<T> result = new Pair<>();
        int i;

        // initialize min/max
        if (n % 2 == 0) { // if number of elements are even
            if (comp.compare(arr[0], arr[1]) > 0) {
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
            if (comp.compare(arr[i], arr[i + 1]) > 0) {
                if (comp.compare(arr[i], result.max) > 0)
                    result.max = arr[i];
                if (comp.compare(arr[i+ 1], result.min) < 0)
                    result.min = arr[i + 1];
            } else {
                if (comp.compare(arr[i + 1], result.max) > 0)
                    result.max = arr[i + 1];
                if (comp.compare(arr[i], result.min) < 0)
                    result.min = arr[i];
            }
            i += 2;
        }

        return result;
    }
}
