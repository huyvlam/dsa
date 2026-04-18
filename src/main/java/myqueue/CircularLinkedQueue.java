package myqueue;

import mylinked.singly.SinglyCircularLinkedList;

import java.util.NoSuchElementException;

public class CircularLinkedQueue<E> {
    private final SinglyCircularLinkedList<E> sclist;
    private final int capacity;

    public CircularLinkedQueue(int capacity) {
        sclist = new SinglyCircularLinkedList<>();
        this.capacity = capacity;
    }

    public void clear() {
        sclist.clear();
    }

    public int size() {
        return sclist.size();
    }

    public boolean isEmpty() {
        return sclist.size() == 0;
    }

    public boolean add(E data) {
        if (sclist.size() == capacity) throw new IllegalStateException("Capacity is reached");

        sclist.addLast(data);
        return true;
    }

    public boolean offer(E data) {
        if (sclist.size() == capacity) return false;

        sclist.addLast(data);
        return true;
    }

    public E poll() {
        return sclist.pollFirst();
    }

    public E remove() {
        if (sclist.size() == 0) throw new NoSuchElementException("Queue is empty");

        return sclist.pollFirst();
    }

    public E peek() {
        return sclist.peekFirst();
    }

    public E element() {
        if (sclist.size() == 0) throw new NoSuchElementException("Queue is empty");

        return sclist.peekFirst();
    }

    public boolean contains(E data) {
        return scList.indexOf(data) != -1;
    }
}
