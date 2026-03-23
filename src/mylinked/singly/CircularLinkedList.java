package mylinked.singly;

import java.util.Comparator;

public class CircularLinkedList<E> {
    public SinglyNode<E> head, tail;
    private int count;
    private final Comparator<? super E> comparator;

    public CircularLinkedList(Comparator<? super E> comparator) {
        head = null;
        tail = null;
        count = 0;
        this.comparator = (comparator != null) ? comparator : (E a, E b) -> {
            if (a instanceof Comparable && b instanceof Comparable)
                return ((Comparable) a).compareTo(b);

            return a.equals(b) ? 0 : -1;
        };
    }

    public void clear() {
        head = null;
        tail = null;
        count = 0;
    }

    public int size() {
        return count;
    }

    public E peekFirst() {
        return head == null ? null : head.data;
    }

    public E peekLast() {
        return tail == null ? null : tail.data;
    }

    public void addFirst(E data) {
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

        SinglyNode<E> node = new SinglyNode<>(data);

        if (head == null) {
            head = node;
            tail = node;
            node.next = head;
        } else {
            node.next = head;
            head = node;
            tail.next = head;
        }
        count++;
    }

    public void addLast(E data) {
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

        if (head == null) {
            addFirst(data);
            return;
        }

        SinglyNode<E> node = new SinglyNode<>(data);
        tail.next = node;
        node.next = head;
        tail = node;
        count++;
    }

    static void main() {
        CircularLinkedList<String> list = new CircularLinkedList<>(null);
    }
}
