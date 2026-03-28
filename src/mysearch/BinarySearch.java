package mysearch;

import myhelper.Checker;

import java.util.Comparator;

public class BinarySearch {
    public static <E> int findIndex(E[] arr, E value, int lo, int hi, Comparator<? super E> comp) {
        Comparator<? super E> safeComp = Checker.nullsLastComparator(comp);

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