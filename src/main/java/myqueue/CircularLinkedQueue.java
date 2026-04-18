package myqueue;

import mylinked.singly.SinglyCircularLinkedList;

import java.util.NoSuchElementException;

public class CircularLinkedQueue<E> {
    private final SinglyCircularLinkedList<E> sclQueue;
    private final int capacity;

    public CircularLinkedQueue(int capacity) {
        sclQueue = new SinglyCircularLinkedList<>();
        this.capacity = capacity;
    }

    public void clear() {
        sclQueue.clear();
    }

    public int size() {
        return sclQueue.size();
    }

    public boolean isEmpty() {
        return sclQueue.size() == 0;
    }

    public boolean add(E data) {
        if (sclQueue.size() == capacity) throw new IllegalStateException("Capacity is reached");

        sclQueue.addLast(data);
        return true;
    }

    public boolean offer(E data) {
        if (sclQueue.size() == capacity) return false;

        sclQueue.addLast(data);
        return true;
    }

    public E poll() {
        return sclQueue.pollFirst();
    }

    public E remove() {
        if (sclQueue.size() == 0) throw new NoSuchElementException("Queue is empty");

        return sclQueue.pollFirst();
    }

    public E peek() {
        return sclQueue.peekFirst();
    }

    public E element() {
        if (sclQueue.size() == 0) throw new NoSuchElementException("Queue is empty");

        return sclQueue.peekFirst();
    }

    public boolean contains(E data) {
        return sclQueue.indexOf(data) != -1;
    }
}
