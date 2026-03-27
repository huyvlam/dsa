package myarray;

import myinterface.Printer;

import java.util.Random;

public class ArrayUtil {
    /**
     * Fill the given array w/ random numbers ranged from 0 to the given bound
     * @param arr array to be filled
     * @param bound upper limit
     */
    public static void fillRandomNumbers(Integer[] arr, int bound) {
        Random rand = new Random();

        for (int i = 0; i < arr.length; i++)
            arr[i] = rand.nextInt(bound);
    }

    /**
     * Flexible toString using custom printer
     * @param arr array of elements
     * @param custom way to print element
     * @return string value of array elements
     * @param <E> type of element
     */
    public static <E> String toString(E[] arr, Printer<E> custom) {
        int n = arr.length;
        if (n == 0) return "Array: [empty]";

        Printer<E> standard = (e) -> e == null ? null : e.toString();
        Printer<E> safePrinter = (custom == null) ? standard : custom;

        StringBuilder sb = new StringBuilder();
        sb.append("Array (size ").append(n).append("): [");

        for (E e : arr) {
            if (e == arr[n - 1]) sb.append(safePrinter.print(e));
            else sb.append(safePrinter.print(e)).append(", ");
        }

        sb.append("]");
        return sb.toString();
    }
}
