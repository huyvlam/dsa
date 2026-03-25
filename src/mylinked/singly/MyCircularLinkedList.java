package mylinked.singly;

import myinterface.Printer;
import myhelper.Checker;

import java.util.Comparator;
import java.util.Objects;

public class MyCircularLinkedList<E> {
    private SinglyNode<E> tail;
    private int count;
    private final Comparator<? super E> comparator = (a, b) -> Objects.equals(a, b) ? 0 : -1;

    public MyCircularLinkedList() {
        tail = null;
        count = 0;
    }

    public void clear() {
        tail = null;
        count = 0;
    }

    public int size() {
        return count;
    }

    public E peekFirst() {
        return (count == 0) ? null : tail.next.data;
    }

    public E peekLast() {
        return (count == 0) ? null : tail.data;
    }

    private void updateTail(SinglyNode<E> node) {
        if (tail == null) {
            tail = node;
        } else {
            node.next = tail.next;
        }
        tail.next = node;
    }

    public void addFirst(E data) {
        Checker.checkNullArgument(data);

        SinglyNode<E> node = new SinglyNode<>(data);
        updateTail(node);
        count++;
    }

    public void addLast(E data) {
        Checker.checkNullArgument(data);

        SinglyNode<E> node = new SinglyNode<>(data);
        updateTail(node);
        tail = node;
        count++;
    }

    public E pollFirst() {
        if (count == 0) return null;

        SinglyNode<E> first = tail.next;

        if (first == tail) tail = null;
        else tail.next = first.next;

        count--;
        return first.data;
    }

    public E pollLast() {
        if (count == 0) return null;

        E data = tail.data;
        SinglyNode<E> prev = tail.next;

        while (prev.next != tail)
            prev = prev.next;

        if (prev == tail) {
            tail = null;
        } else {
            prev.next = tail.next;
            tail = prev;
        }

        count--;
        return data;
    }

    public void add(int i, E data) {
        Checker.checkNullArgument(data);
        if (i < 0 || i > count) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        if (i == 0) {
            addFirst(data);
            return;
        }
        if (i == count) {
            addLast(data);
            return;
        }

        SinglyNode<E> prev = tail.next;
        for (int index = 0; index < i - 1; index++)
            prev = prev.next;

        SinglyNode<E> node = new SinglyNode<>(data);
        node.next = prev.next;
        prev.next = node;
        count++;
    }

    public E remove(int i) {
        Checker.checkBounds(i, count);

        if (i == 0) return pollFirst();
        if (i == count - 1) return pollLast();

        SinglyNode<E> prev = tail.next;

        for (int index = 0; index < i - 1; index++)
            prev = prev.next;

        SinglyNode<E> target = prev.next;
        E data = target.data;
        prev.next = target.next;

        count--;
        return data;
    }

    public boolean remove(E data) {
        return remove(data, comparator);
    }

    public boolean remove(E data, Comparator<? super E> comp) {
        Checker.checkNullArgument(comp, "Comparator");
        if (data == null || count == 0) return false;

        if (comp.compare(tail.next.data, data) == 0) {
            pollFirst();
            return true;
        }
        if (comp.compare(tail.data, data) == 0) {
            pollLast();
            return true;
        }

        SinglyNode<E> prev = tail.next;

        while (prev.next != tail) {
            SinglyNode<E> cur = prev.next;
            if (comp.compare(cur.data, data) == 0) {
                prev.next = cur.next;
                count--;

                return true;
            }
            prev = prev.next;
        }
        return false;
    }

    public E set(int i, E data) {
        Checker.checkBounds(i, count);
        Checker.checkNullArgument(data);

        SinglyNode<E> cur = tail.next;

        for (int index = 0; index < i; index++)
            cur = cur.next;

        E target = cur.data;
        cur.data = data;

        return target;
    }

    public E get(int i) {
        Checker.checkBounds(i, count);

        if (i == count - 1) return peekLast();

        SinglyNode<E> cur = tail.next;

        for (int index = 0; index < i; index++)
            cur = cur.next;

        return cur.data;
    }

    public int indexOf(E data) {
        return indexOf(data, comparator);
    }

    public int indexOf(E data, Comparator<? super E> comp) {
        Checker.checkNullArgument(comp, "Comparator");
        if (data == null) return -1;

        SinglyNode<E> cur = tail.next;

        for (int index = 0; index < count; index++) {
            if (comp.compare(cur.data, data) == 0) return index;
            cur = cur.next;
        }

        return -1;
    }

//    public void reverse() {
//        if (head == null || head.next == null) return;
//
//        SinglyNode<E> prev = tail;
//        SinglyNode<E> cur = head;
//        SinglyNode<E> next;
//
//        for (int i = 0; i < count; i++) {
//            next = cur.next;
//            cur.next = prev;
//            prev = cur;
//            cur = next;
//        }
//
//        tail = head;
//        head = prev;
//    }
//
//    @Override
//    public String toString() {
//        return toString(null);
//    }
//
//    public String toString(Printer<E> printer) {
//        if (head == null) return "CircularLinkedList: [ empty ]";
//
//        Printer<E> safePrinter = (printer == null) ? (Object::toString) : printer;
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("CircularLinkedList (size ").append(count).append("): ");
//
//        SinglyNode<E> cur = head;
//        do {
//            sb.append("[").append(safePrinter.print(cur.data)).append("]");
//
//            if (cur.next != head) sb.append(" -> ");
//            else sb.append(" -> (head)");
//
//            cur = cur.next;
//        } while (cur != head);
//
//        return sb.toString();
//    }
}
