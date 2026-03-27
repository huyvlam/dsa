package myarray;

import myhelper.Checker;

import java.util.Arrays;

public class MyArrayList<E> {
    private Object[] arrList;

    private final int DEFAULT_CAPACITY;
    private int size;

    public MyArrayList(int capacity) {
        DEFAULT_CAPACITY = (capacity == 0) ? 5 : capacity;
        arrList = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public int capacity() {
        return arrList.length;
    }

    public int size() {
        return size;
    }

    private void grow() {
        int curCap = arrList.length;

        if (size < curCap) return;

        int newCap = curCap + (curCap >> 1);
        // ensure new capacity grow by at least 1 for edge case
        newCap = (newCap <= curCap) ? curCap + 1 : newCap;

        arrList = Arrays.copyOf(arrList, newCap);
    }

    public void add(E data) {
        grow();
        arrList[size++] = data;
    }

    public E remove(int index) {
        Checker.checkBounds(index, size);

        E removed = (E) arrList[index];

        int numShift = size - index - 1;
        if (numShift > 0)
            System.arraycopy(arrList, index + 1, arrList, index, numShift);

        arrList[--size] = null;

        return removed;
    }

    public void trimToSize() {
        if (size < arrList.length)
            arrList = (size == 0) ? new Object[DEFAULT_CAPACITY] : Arrays.copyOf(arrList, size);
    }

    public E get(int index) {
        Checker.checkBounds(index, size);

        return (E) arrList[index];
    }
}
