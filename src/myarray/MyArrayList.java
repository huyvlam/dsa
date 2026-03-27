package myarray;

import myhelper.Checker;

import java.util.Arrays;

public class MyArrayList<E> {
    private Object[] dataList;

    private final int DEFAULT_CAPACITY;
    private int size;

    public MyArrayList(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative");
        DEFAULT_CAPACITY = capacity;
        dataList = new Object[DEFAULT_CAPACITY];
        size = 0;
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

    static void main() {

    }
}
