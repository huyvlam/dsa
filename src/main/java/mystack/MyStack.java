package mystack;

import myarray.MyArrayList;

import java.util.EmptyStackException;

public class MyStack<E> {
    private final MyArrayList<E> arlist;

    public MyStack() {
        arlist = new MyArrayList<>();
    }

    public boolean empty() {
        return arlist.size() == 0;
    }

    public E peek() {
        if (empty()) throw new EmptyStackException();
        return arlist.get(arlist.size() - 1);
    }

    public E pop() {
        if (empty()) throw new EmptyStackException();
        return arlist.remove(arlist.size() - 1);
    }

    public E push(E data) {
        arlist.add(data);
        return data;
    }

    public int search(E data) {
        int index = arlist.indexOf(data);
        return index == -1 ? -1 : arlist.size() - index;
    }
}
