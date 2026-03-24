package mylinked.doubly;

import java.util.Comparator;

public class CircularDoublyLinkedList<E> {
    private final DoublyNode<E> sentinel;
    private int count;
    private final Comparator<? super E> comparator;

    public CircularDoublyLinkedList(Comparator<? super E> comparator) {
        sentinel = new DoublyNode<>(null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        count = 0;
        this.comparator = (comparator != null) ? comparator : (a, b) -> {
            if (a instanceof Comparable && b instanceof Comparable)
                return ((Comparable) a).compareTo(b);

            return a.equals(b) ? 0 : -1;
        };
    }

    public void clear() {
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        count = 0;
    }

    public int size() {
        return count;
    }
}
