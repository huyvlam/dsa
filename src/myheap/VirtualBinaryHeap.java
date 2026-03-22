package myheap;

import java.util.Comparator;

public class VirtualBinaryHeap {
    public enum HeapOrder { MIN, MAX }
    public enum SortOrder { ASC, DESC }

    public static <E> void merge(E[] a, E[] b, HeapOrder heapOrder, Comparator<? super E> comparator) {
        int n = a.length + b.length;

        if (n <= 1) return;

        build(a, b, n, heapOrder, comparator);
    }

    /**
     * Standard sort using natural order
     * @param a array of elements
     * @param b array of elements
     * @param <E> element type
     */
    public static <E> void sort(E[] a, E[] b) {
        Comparator<? super E> comparator = (E x, E y) -> {
            if (x instanceof Comparable && y instanceof Comparable)
                return ((Comparable) x).compareTo(y);
            return x.equals(y) ? 0 : -1;
        };
        sort(a, b, SortOrder.ASC, comparator);
    }

    /**
     * Flexible sort using custom comparator
     * @param a array of elements
     * @param b array of elements
     * @param sortOrder [ASC, DESC]
     * @param comparator custom comparator
     * @param <E> element type
     */
    public static <E> void sort(E[] a, E[] b, SortOrder sortOrder, Comparator<? super E> comparator) {
        if (a == null || b == null || comparator == null || sortOrder == null) {
            throw new IllegalArgumentException("Arrays, Order, and Comparator must not be null.");
        }

        int n = a.length + b.length;

        if (n <= 1) return;

        HeapOrder heapOrder = (sortOrder == SortOrder.ASC) ? HeapOrder.MAX : HeapOrder.MIN;

        build(a, b, n, heapOrder, comparator);

        for (int i = n - 1; i > 0; i--) {
            swap(a, b, 0, i);
            heapify(a, b, 0, i, heapOrder, comparator);
        }
    }

    private static <E> void build(E[] a, E[] b, int n, HeapOrder heapOrder, Comparator<? super E> comparator) {
        if (n <= 1) return;

        for (int i = (n / 2) - 1; i >= 0; i--)
            heapify(a, b, i, n, heapOrder, comparator);
    }

    private static <E> void heapify(E[] a, E[] b, int i, int n, HeapOrder heapOrder, Comparator<? super E> comparator) {
        while (true) {
            int winner = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int sign = (heapOrder == HeapOrder.MIN) ? -1 : 1;

            if (left < n && comparator.compare(get(a, b, left), get(a, b, winner)) * sign > 0)
                winner = left;

            if (right < n && comparator.compare(get(a, b, right), get(a, b, winner)) * sign > 0)
                winner = right;

            if (winner == i) break;

            swap(a, b, i, winner);
            i = winner;
        }
    }

    private static <T> T get(T[] a, T[] b, int i) {
        return (i < a.length) ? a[i] : b[i - a.length];
    }

    private static <T> void swap(T[] a, T[] b, int i, int j) {
        T valI = get(a, b, i);
        T valJ = get(a, b, j);

        if (i < a.length) a[i] = valJ;
        else b[i - a.length] = valJ;

        if (j < a.length) a[j] = valI;
        else b[j - a.length] = valI;
    }
}
