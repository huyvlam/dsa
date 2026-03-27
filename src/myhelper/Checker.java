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

    public static void checkBounds(int i, int n) {
        if (i < 0 || i >= n)
            throw new IndexOutOfBoundsException("Index cannot be out of bound");
    }

    public static void checkModCount(int expected, int count) {
        if (expected != count)
            throw new ConcurrentModificationException("Data is modified");
    }

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
