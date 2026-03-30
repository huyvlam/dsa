package myqueue;

import mylinked.singly.MyCircularLinkedList;

import java.util.NoSuchElementException;

public class MyQueue<E> {
    private final MyCircularLinkedList<E> queue;
    private final int CAPACITY;

    public MyQueue(int capacity) {
        queue = new MyCircularLinkedList<>();
        CAPACITY = capacity;
    }

    public boolean add(E data) {
        if (queue.size() == CAPACITY) throw new IllegalStateException("No available space");

        queue.addLast(data);
        return true;
    }

    public boolean offer(E data) {
        if (queue.size() == CAPACITY) return false;

        queue.addLast(data);
        return true;
    }

    public E peek() {
        return queue.peekFirst();
    }

    public E element() {
        if (queue.size() == 0) throw new NoSuchElementException("Queue is empty");

        return queue.peekFirst();
    }

    public E poll() {
        return queue.pollFirst();
    }

    public E remove() {
        if (queue.size() == 0) throw new NoSuchElementException("Queue is empty");

        return queue.pollFirst();
    }
}
