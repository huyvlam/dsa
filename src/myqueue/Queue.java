package myqueue;

import mylinked.singly.SinglyCircularLinkedList;

import java.util.NoSuchElementException;

public class Queue<E> {
    private final SinglyCircularLinkedList<E> cslist;
    private final int CAPACITY;

    public Queue(int capacity) {
        cslist = new SinglyCircularLinkedList<>();
        CAPACITY = capacity;
    }

    public boolean add(E data) {
        if (cslist.size() == CAPACITY) throw new IllegalStateException("No available space");

        cslist.addLast(data);
        return true;
    }

    public boolean offer(E data) {
        if (cslist.size() == CAPACITY) return false;

        cslist.addLast(data);
        return true;
    }

    public E peek() {
        return cslist.peekFirst();
    }

    public E element() {
        if (cslist.size() == 0) throw new NoSuchElementException("Queue is empty");

        return cslist.peekFirst();
    }

    public E poll() {
        return cslist.pollFirst();
    }

    public E remove() {
        if (cslist.size() == 0) throw new NoSuchElementException("Queue is empty");

        return cslist.pollFirst();
    }
}
