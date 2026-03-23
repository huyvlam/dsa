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

    public E pollFirst() {
        if (head == null) return null;

        E data = head.data;

        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            tail.next = head;
        }
        count--;

        return data;
    }

    public E pollLast() {
        if (tail == null) return null;

        if (head == tail) return pollFirst();

        E data = tail.data;
        SinglyNode<E> prev = head;

        while (prev.next != tail)
            prev = prev.next;

        prev.next = head;
        tail = prev;
        count--;

        return data;
    }

    public void add(int i, E data) {
        if (data == null) throw new IllegalArgumentException("Data cannot be null");
        if (i < 0 || i > count) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        if (i == 0) {
            addFirst(data);
            return;
        }
        if (i == count) {
            addLast(data);
            return;
        }

        SinglyNode<E> node = new SinglyNode<>(data);
        SinglyNode<E> prev = head;

        for (int index = 0; index < i - 1; index++)
            prev = prev.next;

        node.next = prev.next;
        prev.next = node;
        count++;
    }

    public E remove(int i) {
        if (i < 0 || i >= count) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        if (i == 0) return pollFirst();
        if (i == count - 1) return pollLast();

        SinglyNode<E> prev = head;

        for (int index = 0; index < i - 1; index++)
            prev = prev.next;

        SinglyNode<E> removed = prev.next;
        E data = removed.data;
        prev.next = removed.next;

        count--;
        return data;
    }

    public int indexOf(E data) {
        if (data == null) return -1;

        SinglyNode<E> cur = head;

        for (int index = 0; index < count; index++) {
            if (comparator.compare(cur.data, data) == 0) return index;

            cur = cur.next;
        }

        return -1;
    }

    public boolean contains(E data) {
        return indexOf(data) != -1;
    }

    static void main() {
        CircularLinkedList<String> list = new CircularLinkedList<>(null);
    }
}
