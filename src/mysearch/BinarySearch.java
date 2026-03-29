package mysearch;

import myhelper.MyComparator;

import java.util.Comparator;

public class BinarySearch {
    /**
     * Find the index of given data in the sorted array
     *
     * @param arr   array of sorted elements to be searched
     * @param value target element to look for
     * @param lo    the lower bound must not be less than 0
     * @param hi    the upper bound must be less than the array size
     * @param comp  custom comparator for generics, or natural order for Comparables
     * @return      the position where the element is found, or -1 if not found
     * @param <E>   type of elements stored in the array
     */
    public static <E> int findIndex(E[] arr, E value, int lo, int hi, Comparator<? super E> comp) {
        Comparator<? super E> safeComp = MyComparator.nullsLastComparator(comp);

        int index = -1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int res = safeComp.compare(arr[mid], value);

            if (res == 0) {
                index = mid;
                hi = mid - 1; // Keep searching left for duplicate until first occurrence found
            } else if (res > 0) {
                hi = mid - 1; // Target is smaller, search left
            } else {
                lo = mid + 1; // Target is larger, search right
            }
        }

        return index;
    }
}