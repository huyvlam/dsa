package myarray;

import java.util.Arrays;

public class MyArrayList<E> {
    private Object[] arr;

    private final int DEFAULT_CAPACITY;
    private int size;

    public MyArrayList(int capacity) {
        DEFAULT_CAPACITY = (capacity == 0) ? 5 : capacity;
        arr = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public int capacity() {
        return arr.length;
    }

    public int size() {
        return size;
    }

    public void add(E data) {
        if (size == arr.length) grow();
        arr[size++] = data;
    }

    public void grow() {
        int newCapacity = size + (size >> 1);

        if (newCapacity <= size) newCapacity = size + 1;

        arr = Arrays.copyOf(arr, newCapacity);
    }
}
