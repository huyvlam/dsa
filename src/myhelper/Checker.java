package myhelper;

import java.util.Comparator;
import java.util.ConcurrentModificationException;

public class Checker {
    public static <T> void checkNullArgument(T data) {
        checkNullArgument(data, "Data");
    }

    public static <T> void checkNullArgument(T data, String name) {
        if (data == null)
            throw new IllegalArgumentException(name + " cannot be null");
    }

    /**
     * Ensure the array index is within legal boundary
     * @param i lower bound (usually first element of the array)
     * @param n the array size
     */
    public static void checkBounds(int i, int n) {
        if (i < 0 || i >= n)
            throw new IndexOutOfBoundsException("Index cannot be out of bound");
    }

    /**
     * Ensure no change is made to the data structure (number of mod are the same)
     * @param expected the expected number of modifications
     * @param current the current number of modifications
     */
    public static void checkModCount(int expected, int current) {
        if (expected != current)
            throw new ConcurrentModificationException("Data is modified");
    }

    /**
     * Provide a comparator wrapped in nulls last object
     * @param comparator the comparator to be wrapped in nulls last
     * @return the given comparator or Comparator.naturalOrder if argument is null
     * @param <E> type of element compared by comparator
     */
    public static <E> Comparator<? super E> nullsLastComparator(Comparator<? super E> comparator) {
        return (comparator == null)
                ? (Comparator<E>) Comparator.nullsLast(Comparator.naturalOrder())
                : Comparator.nullsLast(comparator);
    }

    /**
     * Ensure the array elements implement Comparable interface
     * @param arr the array to examine
     * @param n the array size
     * @return true if the first non-null array element found is Comparable
     * @param <T> type of element
     */
    public static <T> boolean isComparable(T[] arr, int n) {
        T element = null;
        for (int i = 0; i < n; i++) {
            if (arr[i] != null) {
                element = (T) arr[i];
                break;
            }
        }

        return element instanceof Comparable;
    }
}
