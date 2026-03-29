package myhelper;

import java.util.ConcurrentModificationException;

public class Checker<T> {
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
}
