package myarray;

import myhelper.Checker;
import myhelper.MyComparator;
import mysearch.BinarySearch;
import mysearch.LinearSearch;
import mysort.QuickSort;

import java.util.Arrays;
import java.util.Comparator;

public class MyArrayList<E> {
    private Object[] dataList;
    private boolean sorted;

    private final int DEFAULT_CAPACITY;
    private int size;

    public MyArrayList(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative");
        DEFAULT_CAPACITY = capacity;
        dataList = new Object[DEFAULT_CAPACITY];
        size = 0;
        sorted = false;
    }

    public MyArrayList() {
        this(10);
    }

    public int size() {
        return size;
    }

    private void grow() {
        int curCap = dataList.length;

        if (size < curCap) return;

        int newCap = curCap + (curCap >> 1);
        // ensure new capacity grow by at least 1 for edge case
        newCap = (newCap <= curCap) ? curCap + 1 : newCap;

        dataList = Arrays.copyOf(dataList, newCap);
    }

    public boolean add(E data) {
        grow();
        dataList[size++] = data;

        return true;
    }

    public E remove(int index) {
        Checker.checkBounds(index, size);

        E removed = (E) dataList[index];

        int numShift = size - index - 1;
        if (numShift > 0)
            System.arraycopy(dataList, index + 1, dataList, index, numShift);

        dataList[--size] = null;

        return removed;
    }

    public void trimToSize() {
        if (size < dataList.length)
            dataList = (size == 0) ? new Object[DEFAULT_CAPACITY] : Arrays.copyOf(dataList, size);
    }

    public E get(int index) {
        Checker.checkBounds(index, size);

        return (E) dataList[index];
    }

    public int indexOf(E data) {
        return indexOf(data, MyComparator.equalsComparator);
    }

    public int indexOf(E data, Comparator<? super E> comp) {
        Checker.checkNullArgument(data);

        return sorted
                ? BinarySearch.findIndex((E[]) dataList, data, 0, size - 1, MyComparator.nullsLastComparator(comp))
                : LinearSearch.findIndex((E[]) dataList, data, MyComparator.nullsLastComparator(comp));
    }

    public void sort(Comparator<? super E> comp) {
        if (size <= 1) return;

        if (comp == null && !MyComparator.isComparable(dataList, size))
            throw new IllegalArgumentException("Comparator cannot be null for non-Comparable elements");

        QuickSort.sort((E[]) dataList, 0, size - 1, MyComparator.nullsLastComparator(comp));
        sorted = true;
    }
}
