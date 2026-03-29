package mystack;

import mylinked.doubly.MyCircularDoublyLinkedList;

import java.util.EmptyStackException;

public class MyDropoutStack<E> {
    private final MyCircularDoublyLinkedList<E> stack;
    private final int MAX_CAPACITY;

    public MyDropoutStack(int maxCapacity) {
        stack = new MyCircularDoublyLinkedList<>();
        MAX_CAPACITY = maxCapacity;
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
        if (stack.size() == MAX_CAPACITY) stack.pollLast();

        stack.addFirst(data);
        return data;
    }

    public int search(E data) {
        int index = stack.indexOf(data);
        return (index == -1) ? -1 : index + 1;
    }
}
