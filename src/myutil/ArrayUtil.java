package myutil;

import myinterface.Printer;

import java.util.Random;

public class ArrayUtil {
    // Fill the array w/ random numbers from 0 to the given bound
    public static void fillRandomNumbers(Integer[] arr, int bound) {
        Random rand = new Random();

        for (int i = 0; i < arr.length; i++)
            arr[i] = rand.nextInt(bound);
    }

    // Flexible toString with custom print of data
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
