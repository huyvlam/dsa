package myarray;

import myinterface.Printer;

import java.util.Arrays;
import java.util.Random;

public class ArrayUtil {
    /**
     * Fill the given array w/ random numbers ranged from 0 to the given bound
     * @param arr array to be filled
     * @param bound upper limit (number generated does not exceed this)
     */
    public static void fillRandomNumbers(Integer[] arr, int bound) {
        Random rand = new Random();

        for (int i = 0; i < arr.length; i++)
            arr[i] = rand.nextInt(bound);
    }

    /**
     * Standard toString
     * @param arr array of elements
     * @return string value
     * @param <E> element type
     */
    public static <E> String toString(E[] arr) {
        return toString(arr, null);
    }

    /**
     * Flexible toString w/ custom print of data
     * @param arr array of elements
     * @param printer custom way to print element
     * @return string value of every element in the given array
     * @param <E> type of element
     */
    public static <E> String toString(E[] arr, Printer<E> printer) {
        int n = arr.length;
        if (n == 0) return "[ empty ]";

        // check and provide fallback print method
        Printer<E> safePrinter = (printer == null) ? (Object::toString) : printer;

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (E e : arr) {
            if (e.equals(arr[n - 1])) sb.append(safePrinter.print(e));
            else sb.append(safePrinter.print(e)).append(", ");
        }

        sb.append("]");
        return sb.toString();
    }
}
