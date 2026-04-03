package myqueue;

import mylinked.singly.SinglyCircularLinkedList;

import java.util.NoSuchElementException;

public class Queue<E> {
    private final SinglyCircularLinkedList<E> sclist;
    private final int CAPACITY;

    public Queue(int capacity) {
        sclist = new SinglyCircularLinkedList<>();
        CAPACITY = capacity;
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
        if (sclist.size() == CAPACITY) throw new IllegalStateException("No available space");

        sclist.addLast(data);
        return true;
    }

    public boolean offer(E data) {
        if (sclist.size() == CAPACITY) return false;

        sclist.addLast(data);
        return true;
    }

    public E peek() {
        return sclist.peekFirst();
    }

    public E element() {
        if (sclist.size() == 0) throw new NoSuchElementException("Queue is empty");

        return sclist.peekFirst();
    }

    public E poll() {
        return sclist.pollFirst();
    }

    public E remove() {
        if (sclist.size() == 0) throw new NoSuchElementException("Queue is empty");

        return sclist.pollFirst();
    }
}
