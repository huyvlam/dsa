package mylinked.doubly;

import java.util.Comparator;

public class MyCircularDoublyLinkedList<E> {
    private final DoublyNode<E> sentinel;
    private int count;
    private final Comparator<? super E> comparator;

    public MyCircularDoublyLinkedList(Comparator<? super E> comparator) {
        sentinel = new DoublyNode<>(null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        count = 0;
        this.comparator = comparator;
    }

    public MyCircularDoublyLinkedList() {
        this((a, b) -> {
            if (a instanceof Comparable && b instanceof Comparable)
                return ((Comparable) a).compareTo(b);

            return a.equals(b) ? 0 : -1;
        });
    }

    public void clear() {
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        count = 0;
    }

    public int size() {
        return count;
    }

    public E peekFirst() {
        return (count == 0) ? null : sentinel.next.data;
    }

    public E peekLast() {
        return (count == 0) ? null : sentinel.prev.data;
    }

    public void addFirst(E data) {
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

        DoublyNode<E> node = new DoublyNode<>(data);

        node.next = sentinel.next;
        node.prev = sentinel;

        sentinel.next.prev = node;
        sentinel.next = node;

        count++;
    }

    public void addLast(E data) {
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

        DoublyNode<E> node = new DoublyNode<>(data);

        node.next = sentinel;
        node.prev = sentinel.prev;

        sentinel.prev.next = node;
        sentinel.prev = node;

        count++;
    }

    public E pollFirst() {
        if (count == 0) return null;

        DoublyNode<E> first = sentinel.next;
        E data = first.data;

        sentinel.next = first.next;
        first.next.prev = sentinel;

        first.prev = null;
        first.next = null;

        count--;
        return data;
    }

    public E pollLast() {
        if (count == 0) return null;

        DoublyNode<E> last = sentinel.prev;
        E data = last.data;

        sentinel.prev = last.prev;
        last.prev.next = sentinel;

        last.prev = null;
        last.next = null;

        count--;
        return data;
    }
}
