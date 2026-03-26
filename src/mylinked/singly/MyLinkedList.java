package mylinked.singly;

import java.util.Comparator;
import java.util.Objects;

import myinterface.Printer;

public class MyLinkedList<E> {
    private SinglyNode<E> head, tail;
    private final Comparator<? super E> comparator = (a, b) -> Objects.equals(a, b) ? 0 : -1;

    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
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
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

        SinglyNode<E> node = new SinglyNode<>(data);

        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head = node;
        }
        size++;
    }

    public void addLast(E data) {
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

        if (head == null) {
            addFirst(data);
            return;
        }

        SinglyNode<E> node = new SinglyNode<>(data);

        tail.next = node;
        tail = node;
        size++;
    }

    public E pollFirst() {
        if (head == null) return null;

        E data = head.data;
        head = head.next;

        // * When list becomes empty, tail MUST be set to null (i.e ghost node)
        if (head == null) tail = null;

        size--;
        return data;
    }

    public E pollLast() {
        if (tail == null) return null;

        if (head == tail) return pollFirst();

        E data = tail.data;
        SinglyNode<E> prev = head;

        while (prev.next != tail)
            prev = prev.next;

        prev.next = null;
        tail = prev;
        size--;

        return data;
    }

    public void add(int i, E data) {
        if (data == null) throw new IllegalArgumentException("Data cannot be null");
        if (i < 0 || i > size) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        if (i == 0) {
            addFirst(data);
            return;
        }

        if (i == size) {
            addLast(data);
            return;
        }

        SinglyNode<E> node = new SinglyNode<>(data);
        SinglyNode<E> prev = head;

        for (int index = 0; index < i - 1; index++)
            prev = prev.next;

        node.next = prev.next;
        prev.next = node;
        size++;
    }

    public E remove(int i) {
        if (i < 0 || i >= size) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        if (i == 0) return pollFirst();
        if (i == size - 1) return pollLast();

        SinglyNode<E> prev = head;

        for (int index = 0; index < i - 1; index++)
            prev = prev.next;

        SinglyNode<E> removed = prev.next;
        E data = removed.data;
        prev.next = removed.next;

        size--;
        return data;
    }

    public boolean remove(E data) {
        return remove(data, comparator);
    }

    public boolean remove(E data, Comparator<? super E> comp) {
        if (data == null || size == 0) return false;

        if (comp.compare(head.data, data) == 0) {
            pollFirst();
            return true;
        }
        if (comp.compare(tail.data, data) == 0) {
            pollLast();
            return true;
        }

        SinglyNode<E> prev = head;

        while (prev.next != tail) {
            SinglyNode<E> cur = prev.next;
            if (comp.compare(cur.data, data) == 0) {
                prev.next = cur.next;
                size--;

                return true;
            }
            prev = prev.next;
        }

        return false;
    }

    public E set(int i, E data) {
        if (i < 0 || i >= size) throw new IndexOutOfBoundsException("Index cannot be out of bound");
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

        SinglyNode<E> cur = head;

        for (int index = 0; index < i; index++)
            cur = cur.next;

        E replaced = cur.data;
        cur.data = data;

        return replaced;
    }

    public E get(int i) {
        if (i < 0 || i >= size) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        // use tail pointer for O(1) search
        if (i == size - 1) return peekLast();

        SinglyNode<E> cur = head;

        for (int count = 0; count < i; count++)
            cur = cur.next;

        return cur.data;
    }

    public int indexOf(E data) {
        return indexOf(data, comparator);
    }

    public int indexOf(E data, Comparator<? super E> comp) {
        if (data == null) return -1;

        SinglyNode<E> cur = head;

        for (int index = 0; index < size; index++) {
            if (comp.compare(cur.data, data) == 0) return index;
            cur = cur.next;
        }

        return -1;
    }

    public void reverse() {
        if (head == null || head.next == null) return;

        SinglyNode<E> prev = null;
        SinglyNode<E> cur = head;
        SinglyNode<E> next;

        while (cur != null) {
            next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }

        SinglyNode<E> temp = head;
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
        sb.append("LinkedList (size ").append(size).append("): ");

        SinglyNode<E> cur = head;
        while (cur != null) {
            sb.append("[").append(safePrinter.print(cur.data)).append("]");

            if (cur.next != null) sb.append(" -> ");
            else sb.append(" -> null");

            cur = cur.next;
        }

        return sb.toString();
    }
}
