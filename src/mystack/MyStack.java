package mystack;

import myarray.MyArrayList;

import java.util.EmptyStackException;

public class MyStack<E> {
    private final MyArrayList<E> stack;

    public MyStack() {
        stack = new MyArrayList<>();
    }

    public boolean empty() {
        return stack.size() == 0;
    }

    public E peek() {
        return stack.get(stack.size() - 1);
    }

    public E pop() {
        if (empty()) throw new EmptyStackException();
        return stack.remove(stack.size() - 1);
    }

    public E push(E data) {
        stack.add(data);
        return data;
    }

    public int search(E data) {
        int index = stack.indexOf(data);
        return index == -1 ? -1 : stack.size() - index;
    }
}
