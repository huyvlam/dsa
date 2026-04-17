package myarray;

import myhelper.Checker;
import myutil.ComparatorUtil;
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

    public void clear() {
        dataList = new Object[DEFAULT_CAPACITY];
        size = 0;
        sorted = false;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return dataList.length;
    }

    public void ensureCapacity(int capacity) {
        grow(capacity);
    }

    private void grow() {
        int curCap = dataList.length;
        // Grow capacity by 1.5
        int newCap = curCap + (curCap >> 1);
        // Ensure capacity grow by at least 1 for edge case, where initial cap is 0 or 1
        newCap = (newCap <= curCap) ? curCap + 1 : newCap;
        grow(newCap);
    }

    private void grow(int capacity) {
        if (capacity <= dataList.length) return;

        Object[] newList = new Object[capacity];
        System.arraycopy(dataList, 0, newList, 0, size);
        dataList = newList;
    }

    public boolean add(E data) {
        if (size == dataList.length) {
            grow();
        }
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
        if (size < dataList.length) {
            int newCap = size > DEFAULT_CAPACITY ? size : DEFAULT_CAPACITY;
            Object[] newList = new Object[newCap];
            System.arraycopy(dataList, 0, newList, 0, size);
            dataList = newList;
        }
    }

    public E get(int index) {
        Checker.checkBounds(index, size);

        return (E) dataList[index];
    }

    public int indexOf(E data) {
        return indexOf(data, ComparatorUtil.equalsComparator);
    }

    public int indexOf(E data, Comparator<? super E> comp) {
        Checker.checkNullArgument(data);

        return sorted
                ? BinarySearch.findIndex((E[]) dataList, data, 0, size - 1, ComparatorUtil.nullsLastComparator(comp))
                : LinearSearch.findIndex((E[]) dataList, data, 0, size - 1, ComparatorUtil.nullsLastComparator(comp));
    }

    public void sort(Comparator<? super E> comp) {
        if (size <= 1) return;

        if (comp == null && !ComparatorUtil.isComparable(dataList, size))
            throw new IllegalArgumentException("Comparator cannot be null for non-Comparable elements");

        QuickSort.sort((E[]) dataList, 0, size - 1, ComparatorUtil.nullsLastComparator(comp));
        sorted = true;
    }
}
