package mylinked.doubly;

import myinterface.Printer;
import myhelper.Checker;

import java.util.Comparator;

public class MyDoublyLinkedList<E> {
    private DoublyNode<E> head, tail;
    private int size;
    private final Comparator<? super E> comparator;

    public MyDoublyLinkedList(Comparator<? super E> comparator) {
        head = null;
        tail = null;
        size = 0;
        this.comparator = comparator;
    }

    public MyDoublyLinkedList() {
        this((a, b) -> {
            if (a instanceof Comparable && b instanceof Comparable)
                return ((Comparable) a).compareTo(b);

            return a.equals(b) ? 0 : -1;
        });
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public E peekFirst() {
        return head == null ? null : head.data;
    }

    public E peekLast() {
        return tail == null ? null : tail.data;
    }

    public void addFirst(E data) {
        Checker.checkNullArgument(data);

        DoublyNode<E> node = new DoublyNode<>(data);

        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    public void addLast(E data) {
        Checker.checkNullArgument(data);

        DoublyNode<E> node = new DoublyNode<>(data);

        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public E pollFirst() {
        if (head == null) return null;

        E data = head.data;
        head = head.next;

        if (head != null) head.prev = null;
        else tail = null;

        size--;
        return data;
    }

    public E pollLast() {
        if (tail == null) return null;

        E data = tail.data;
        tail = tail.prev;

        if (tail != null) tail.next = null;
        else head = null;

        size--;
        return data;
    }

    public void add(int i, E data) {
        Checker.checkNullArgument(data);
        if (i < 0 || i > size) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        if (i == 0) {
            addFirst(data);
            return;
        }
        if (i == size) {
            addLast(data);
            return;
        }

        DoublyNode<E> node = new DoublyNode<>(data);

        if (i < size / 2) {
            DoublyNode<E> prev = head;

            for (int index = 0; index < i - 1; index++)
                prev = prev.next;

            node.next = prev.next;
            node.prev = prev;
            prev.next = node;
            prev.next.prev = node;
        } else {
            DoublyNode<E> cur = tail;

            for (int index = size - 1; index > i; index--)
                cur = cur.prev;

            node.prev = cur.prev;
            node.next = cur;
            cur.prev.next = node;
            cur.prev = node;
        }
        size++;
    }

    public E remove(int i) {
        Checker.checkBounds(i, size);

        if (i == 0) return pollFirst();
        if (i == size - 1) return pollLast();

        DoublyNode<E> cur;

        if (i < size / 2) {
            cur = head;
            for (int index = 0; index < i; index++)
                cur = cur.next;
        } else {
            cur = tail;
            for (int index = size - 1; index > i; index--)
                cur = cur.prev;
        }

        E data = cur.data;

        cur.prev.next = cur.next;
        cur.next.prev = cur.prev;
        cur.next = null;
        cur.prev = null;

        size--;
        return data;
    }

    public boolean remove(E data) {
        if (data == null || size == 0) return false;

        if (comparator.compare(head.data, data) == 0)
            return pollFirst() != null;

        if (comparator.compare(tail.data, data) == 0)
            return pollLast() != null;

        DoublyNode<E> cur = head.next;

        while (cur != null) {
            if (comparator.compare(cur.data, data) == 0) {
                cur.prev.next = cur.next;
                cur.next.prev = cur.prev;
                cur.next = null;
                cur.prev = null;

                size--;
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    public E set(int i, E data) {
        Checker.checkNullArgument(data);
        Checker.checkBounds(i, size);

        DoublyNode<E> cur;

        if (i < size / 2) {
            cur = head;
            for (int index = 0; index < i; index++)
                cur = cur.next;
        } else {
            cur = tail;
            for (int index = size - 1; index > i; index--)
                cur = cur.prev;
        }

        E replaced = cur.data;
        cur.data = data;

        return replaced;
    }

    public E get(int i) {
        Checker.checkBounds(i, size);

        if (i == 0) return peekFirst();

        if (i == size - 1) return peekLast();

        DoublyNode<E> cur = head;
        for (int count = 0; count < i; count++)
            cur = cur.next;

        return cur.data;
    }

    public int indexOf(E data) {
        if (data == null) return -1;

        DoublyNode<E> cur = head;

        for (int index = 0; index < size; index++) {
            if (comparator.compare(cur.data, data) == 0) return index;
            cur = cur.next;
        }

        return -1;
    }

    public boolean contains(E data) {
        return indexOf(data) != -1;
    }

    public void reverse() {
        if (head == null || head.next == null) return;

        DoublyNode<E> cur = head;
        DoublyNode<E> temp;

        while (cur != null) {
            temp = cur.prev;
            cur.prev = cur.next;
            cur.next = temp;

            cur = cur.prev;
        }

        temp = head;
        head = tail;
        tail = temp;
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(Printer<E> printer) {
        if (head == null) return "List: [ empty ]";

        Printer<E> safePrinter = (printer == null) ? (Object::toString) : printer;

        StringBuilder sb = new StringBuilder();
        sb.append("List (size ").append(size).append("): ");

        DoublyNode<E> cur = head;
        while (cur != null) {
            sb.append("[").append(safePrinter.print(cur.data)).append("]");

            if (cur.next != null) {
                sb.append(" -> ");
            } else {
                sb.append(" -> null");
            }
            cur = cur.next;
        }

        return sb.toString();
    }
}
