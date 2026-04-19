package myqueue;

import java.util.NoSuchElementException;

public class CircularArrayQueue<E> {
    private E[] queue;
    private final int capacity;
    private int size;
    private int front;
    private int end;

    public CircularArrayQueue(int capacity) {
        this.capacity = capacity;
        queue = (E[]) new Object[capacity];
        size = 0;
        front = 0;
        end = -1;
    }

    public void clear() {
        queue = (E[]) new Object[capacity];
        size = 0;
        front = 0;
        end = -1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public boolean add(E value) {
        if (size == queue.length) throw new IllegalStateException("Queue is full");

        end = (end + 1) % queue.length;
        queue[end] = value;
        size++;
        return true;
    }

    public boolean offer(E value) {
        if (size == queue.length) return false;

        end = (end + 1) % queue.length;
        queue[end] = value;
        size++;
        return true;
    }

    public E poll() {
        if (size == 0) return null;

        E polled = queue[front];
        front = (front + 1) % queue.length;
        size--;
        return polled;
    }

    public E remove() {
        if (size == 0) throw new NoSuchElementException("Queue is empty");

        E removed = queue[front];
        front = (front + 1) % queue.length;
        size--;
        return removed;
    }

    public E peek() {
        return size == 0 ? null : queue[front];
    }

    public E element() {
        if (size == 0) throw new NoSuchElementException("Queue is empty");

        return queue[front];
    }

    public boolean contains(E value) {
        for (int i = 0; i < size; i++) {
            if (queue[i] == value) return true;
        }
        return false;
    }
}
