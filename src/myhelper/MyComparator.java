package myhelper;

import java.util.Comparator;
import java.util.Objects;

public class MyComparator {
    /**
     * Provide an equal comparison callback function
     *
     * @param   a/b the two elements that are to be compared
     * @return  null safe equals comparison for generic elements
     *          OR natural order comparator for Comparable elements
     */
    public static final Comparator equalsComparator = (a, b)
            -> (a instanceof Comparable && b instanceof Comparable)
            ? ((Comparable) a).compareTo(b)
            : Objects.equals(a, b) ? 0 : -1;

    /**
     * Provide a comparator wrapped in nulls last object
     *
     * @param comparator    the comparator to be wrapped in nulls last
     * @param <E>           type of element compared by comparator
     * @return              comparator argument or natural order if argument is null
     */
    public static <E> Comparator<? super E> nullsLastComparator(Comparator<? super E> comparator) {
        return (comparator == null)
                ? (Comparator<E>) Comparator.nullsLast(Comparator.naturalOrder())
                : Comparator.nullsLast(comparator);
    }

    /**
     * Ensure the array elements implement Comparable interface
     *
     * @param arr   the array to examine
     * @param bound upper bound must not exceed the size of array
     * @param <T>   type of element
     * @return      true if the first non-null element found is Comparable
     */
    public static <T> boolean isComparable(T[] arr, int bound) {
        T element = null;
        for (int i = 0; i < bound; i++) {
            if (arr[i] != null) {
                element = (T) arr[i];
                break;
            }
        }

        return element instanceof Comparable;
    }
}
