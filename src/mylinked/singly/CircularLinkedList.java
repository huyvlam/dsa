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


    static void main() {
        CircularLinkedList<String> list = new CircularLinkedList<>(null);
    }
}
