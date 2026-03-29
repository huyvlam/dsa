package mystack;

import mylinked.doubly.MyCircularDoublyLinkedList;

import java.util.EmptyStackException;

public class MyDropoutStack<E> {
    private final MyCircularDoublyLinkedList<E> stack;
    private final int CAPACITY;

    public MyDropoutStack(int capacity) {
        stack = new MyCircularDoublyLinkedList<>();
        CAPACITY = capacity;
    }

    public boolean empty() {
        return stack.size() == 0;
    }

    public E peek() {
        if (empty()) throw new EmptyStackException();
        return stack.peekFirst();
    }

    public E pop() {
        if (empty()) throw new EmptyStackException();
        return stack.pollFirst();
    }

    public E push(E data) {
        if (stack.size() == CAPACITY) stack.pollLast();

        stack.addFirst(data);
        return data;
    }

    public int search(E data) {
        int index = stack.indexOf(data);
        return (index == -1) ? -1 : index + 1;
    }
}
