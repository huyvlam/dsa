package mylinked.singly;

import myinterface.Printer;

import java.util.Comparator;

public class CircularLinkedList<E> {
    private SinglyNode<E> head, tail;
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

    public boolean remove(E data) {
        if (data == null || count == 0) return false;

        if (comparator.compare(head.data, data) == 0) {
            pollFirst();
            return true;
        }
        if (comparator.compare(tail.data, data) == 0) {
            pollLast();
            return true;
        }

        SinglyNode<E> prev = head;

        while (prev.next != tail) {
            SinglyNode<E> cur = prev.next;
            if (comparator.compare(cur.data, data) == 0) {
                prev.next = cur.next;
                count--;

                return true;
            }
            prev = prev.next;
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

        if (i == count - 1) return peekLast();

        SinglyNode<E> cur = head;

        for (int index = 0; index < i; index++)
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

        SinglyNode<E> prev = tail;
        SinglyNode<E> cur = head;
        SinglyNode<E> next;

        for (int i = 0; i < count; i++) {
            next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }

        tail = head;
        head = prev;
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(Printer<E> printer) {
        if (head == null) return "List: [ empty ]";

        Printer<E> safePrinter = (printer == null) ? (Object::toString) : printer;

        StringBuilder sb = new StringBuilder();
        sb.append("CircularLinkedList (size ").append(count).append("): ");

        SinglyNode<E> cur = head;
        do {
            sb.append("[").append(safePrinter.print(cur.data)).append("]");

            if (cur.next != head) sb.append(" -> ");
            else sb.append(" -> (head)");

            cur = cur.next;
        } while (cur != head);

        return sb.toString();
    }
}
