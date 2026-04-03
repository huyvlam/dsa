package mystack;

import mylinked.doubly.CircularDoublyLinkedList;

import java.util.EmptyStackException;

public class DropoutStack<E> {
    private final CircularDoublyLinkedList<E> cdlist;
    private final int CAPACITY;

    public DropoutStack(int capacity) {
        cdlist = new CircularDoublyLinkedList<>();
        CAPACITY = capacity;
    }

    public boolean empty() {
        return cdlist.size() == 0;
    }

    public E peek() {
        if (empty()) throw new EmptyStackException();
        return cdlist.peekFirst();
    }

    public E pop() {
        if (empty()) throw new EmptyStackException();
        return cdlist.pollFirst();
    }

    public E push(E data) {
        if (cdlist.size() == CAPACITY) cdlist.pollLast();

        cdlist.addFirst(data);
        return data;
    }

    public int search(E data) {
        int index = cdlist.indexOf(data);
        return (index == -1) ? -1 : index + 1;
    }
}
