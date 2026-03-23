package mysearch;

import java.util.Comparator;

public class LinearSearch {
    /**
     * Pairwise comparison
     * @param arr array of elements for searching
     * @return an object array w/ two elements: min(0), max(1)
     * @param <E> type of element
     */
    public static <E> Object[] findMinMax(E[] arr) {
        Comparator<? super E> comparator = (E a, E b) -> {
            if (a instanceof Comparable && b instanceof Comparable)
                return ((Comparable) a).compareTo(b);

            return a.equals(b) ? 0 : -1;
        };

        return findMinMax(arr, comparator);
    }

    public static <E> Object[] findMinMax(E[] arr, Comparator<? super E> comparator) {
        int n = arr.length;
        if (n == 0) return null;

        E min = null;
        E max = null;
        int i;

        // initialize min/max
        if (n % 2 == 0) { // if number of elements are even
            if (comparator.compare(arr[0], arr[1]) > 0) {
                max = arr[0];
                min = arr[1];
            } else {
                max = arr[1];
                min = arr[0];
            }
            i = 2;
        } else {
            max = arr[0];
            min = arr[0];
            i = 1;
        }

        // compare in pair
        while (i < n - 1) {
            if (comparator.compare(arr[i], arr[i + 1]) > 0) {
                if (comparator.compare(arr[i], max) > 0) max = arr[i];
                if (comparator.compare(arr[i+ 1], min) < 0) min = arr[i + 1];
            } else {
                if (comparator.compare(arr[i + 1], max) > 0) max = arr[i + 1];
                if (comparator.compare(arr[i], min) < 0) min = arr[i];
            }
            i += 2;
        }

        return new Object[]{min, max};
    }
}
