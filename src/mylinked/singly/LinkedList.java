package mylinked.singly;

import java.util.Comparator;

import mymodel.Person;
import myinterface.Printer;

public class LinkedList<E> {
    public SinglyNode<E> head, tail;
    private int count;
    private final Comparator<? super E> comparator;

    public LinkedList(Comparator<? super E> comparator) {
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

        node.next = head;
        head = node;
        count++;

        if (tail != null) return;
        tail = head;
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
        count++;
    }

    public E pollFirst() {
        if (head == null) return null;

        E data = head.data;
        head = head.next;

        // Important: when list becomes empty, tail MUST be null to avoid "ghost node"
        if (head == null) tail = null;

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

        prev.next = null;
        tail = prev;
        count--;

        return data;
    }

    public void add(int i, E data) {
        if (data == null) throw new IllegalArgumentException("Data cannot be null");
        if (i < 0 || i > count) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        if (i == 0 || count == 0) {
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
        SinglyNode<E> cur = head.next;

        for (int index = 0; index < i - 1; index++) {
            prev = cur;
            cur = cur.next;
        }

        E data = cur.data;
        prev.next = cur.next;

        count--;
        return data;
    }

    public boolean remove(E data) {
        if (data == null || count == 0) return false;

        if (comparator.compare(head.data, data) == 0)
            return pollFirst() != null;

        if (comparator.compare(tail.data, data) == 0)
            return pollLast() != null;

        SinglyNode<E> prev = head;
        SinglyNode<E> cur = head.next;

        while (cur != null) {
            if (comparator.compare(cur.data, data) == 0) {
                prev.next = cur.next;
                if (cur == tail) tail = prev;
                count--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }

        return false;
    }

    public E set(int i, E data) {
        if (i < 0 || i >= count) throw new IndexOutOfBoundsException("Index cannot be out of bound");
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

        SinglyNode<E> cur = head;

        for (int index = 0; index < i; index++)
            cur = cur.next;

        E replaced = cur.data;
        cur.data = data;

        return replaced;
    }

    public E get(int i) {
        if (i < 0 || i >= count) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        if (i == 0) return peekFirst();

        if (i == count - 1) return peekLast();

        SinglyNode<E> cur = head;
        for (int count = 0; count < i; count++)
            cur = cur.next;

        return cur.data;
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

    public void reverse() {
        if (head == null || head.next == null) return;

        SinglyNode<E> prev = null;
        SinglyNode<E> cur = head;
        SinglyNode<E> next = null;

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
        return toString(Object::toString);
    }

    public String toString(Printer<E> printer) {
        if (head == null) return "List: [ empty ]";

        Printer<E> safePrinter = (printer == null) ? (Object::toString) : printer;

        StringBuilder sb = new StringBuilder();
        sb.append("List (size ").append(count).append("): ");

        SinglyNode<E> cur = head;
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
