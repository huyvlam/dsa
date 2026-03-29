package mylinked.singly;

import myhelper.MyComparator;
import myinterface.Printer;
import myhelper.Checker;

import java.util.*;

public class MyCircularLinkedList<E> {
    private SinglyNode<E> tail;

    private int modCount; // number of modifications
    private int size; // number of nodes

    public MyCircularLinkedList() {
        tail = null;
        size = 0;
        modCount = 0;
    }

    public void clear() {
        tail = null;
        size = 0;
        modCount++;
    }

    public int size() {
        return size;
    }

    public E peekFirst() {
        return (size == 0) ? null : tail.next.data;
    }

    public E peekLast() {
        return (size == 0) ? null : tail.data;
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
        size++;
        modCount++;
    }

    public void addLast(E data) {
        Checker.checkNullArgument(data);

        SinglyNode<E> node = new SinglyNode<>(data);
        updateTail(node);
        tail = node;
        size++;
        modCount++;
    }

    public E pollFirst() {
        if (size == 0) return null;

        SinglyNode<E> first = tail.next;

        if (first == tail) tail = null;
        else tail.next = first.next;

        size--;
        modCount++;
        return first.data;
    }

    public E pollLast() {
        if (size == 0) return null;

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

        size--;
        modCount++;

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

        SinglyNode<E> prev = tail.next;
        for (int index = 0; index < i - 1; index++)
            prev = prev.next;

        SinglyNode<E> node = new SinglyNode<>(data);
        node.next = prev.next;
        prev.next = node;

        size++;
        modCount++;
    }

    public E remove(int i) {
        Checker.checkBounds(i, size);

        if (i == 0) return pollFirst();
        if (i == size - 1) return pollLast();

        SinglyNode<E> prev = tail.next;

        for (int index = 0; index < i - 1; index++)
            prev = prev.next;

        SinglyNode<E> target = prev.next;
        E data = target.data;
        prev.next = target.next;

        size--;
        modCount++;

        return data;
    }

    public boolean remove(E data) {
        return remove(data, MyComparator.equalsComparator);
    }

    public boolean remove(E data, Comparator<? super E> comp) {
        if (data == null || size == 0) return false;

        Comparator<? super E> safeComp = MyComparator.nullsLastComparator(comp);

        if (safeComp.compare(tail.next.data, data) == 0) {
            pollFirst();
            return true;
        }
        if (safeComp.compare(tail.data, data) == 0) {
            pollLast();
            return true;
        }

        SinglyNode<E> prev = tail.next;

        while (prev.next != tail) {
            SinglyNode<E> cur = prev.next;
            if (safeComp.compare(cur.data, data) == 0) {
                prev.next = cur.next;

                size--;
                modCount++;

                return true;
            }
            prev = prev.next;
        }
        return false;
    }

    public E set(int i, E data) {
        Checker.checkBounds(i, size);
        Checker.checkNullArgument(data);

        SinglyNode<E> cur = tail.next;

        for (int index = 0; index < i; index++)
            cur = cur.next;

        E target = cur.data;
        cur.data = data;

        modCount++;
        return target;
    }

    public E get(int i) {
        Checker.checkBounds(i, size);

        if (i == size - 1) return peekLast();

        SinglyNode<E> cur = tail.next;

        for (int index = 0; index < i; index++)
            cur = cur.next;

        return cur.data;
    }

    public int indexOf(E data) {
        return indexOf(data, MyComparator.equalsComparator);
    }

    public int indexOf(E data, Comparator<? super E> comp) {
        if (data == null) return -1;

        Comparator<? super E> safeComp = MyComparator.nullsLastComparator(comp);
        SinglyNode<E> cur = tail.next;

        for (int index = 0; index < size; index++) {
            if (safeComp.compare(cur.data, data) == 0) return index;
            cur = cur.next;
        }

        return -1;
    }

    public void reverse() {
        if (size <= 1) return;

        SinglyNode<E> prev = tail;
        SinglyNode<E> cur = tail.next;
        SinglyNode<E> next = null;

        for (int i = 0; i < size; i++) {
            next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }

        tail = next;
        tail.next = prev;

        modCount++;
    }

    public Iterator<E> iterator() {
        return new Iterator<>() {
            private SinglyNode<E> cur = (tail == null) ? null : tail.next;
            private SinglyNode<E> prev = tail;

            private SinglyNode<E> returned = null;
            private SinglyNode<E> returnedPrev = null;

            private int expectedModCount = modCount;
            private int iterated = 0;

            public boolean hasNext() {
                return iterated < size;
            }

            public E next() {
                Checker.checkModCount(expectedModCount, modCount);
                if (!hasNext()) throw new NoSuchElementException("No more elements");

                returnedPrev = prev;
                returned = cur;

                prev = cur;
                cur = cur.next;

                iterated++;
                return returned.data;
            }

            public void remove() {
                Checker.checkModCount(expectedModCount, modCount);
                if (returned == null) throw new IllegalStateException("The method can only be called once after calling next method");

                // if list has one element, reset tail/cur
                if (size == 1) {
                    tail = null;
                    cur = null;
                } else {
                    returnedPrev.next = returned.next;

                    // if returned is tail, reset tail
                    if (returned == tail) tail = returnedPrev;
                }
                returned.next = null;

                // reset all pointers to the element that is removed
                prev = returnedPrev;
                returned = null;

                size--;
                iterated--;

                modCount++;
                expectedModCount++;
            }
        };
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(Printer<E> printer) {
        if (size == 0) return "CircularLinkedList: [ empty ]";

        Printer<E> safePrinter = (printer == null) ? (Object::toString) : printer;

        StringBuilder sb = new StringBuilder();
        sb.append("CircularLinkedList (size ").append(size).append("): ");

        SinglyNode<E> cur = tail.next;
        do {
            sb.append("[").append(safePrinter.print(cur.data)).append("]");

            if (cur.next == tail.next) sb.append(" -> (head)");
            else sb.append(" -> ");

            cur = cur.next;
        } while (cur != tail.next);

        return sb.toString();
    }
}
