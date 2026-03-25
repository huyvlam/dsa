package mylinked.doubly;

import myhelper.Checker;
import myinterface.Printer;

import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

public class MyCircularDoublyLinkedList<E> {
    private final DoublyNode<E> sentinel;
    private int count;
    private final Comparator<? super E> comparator = (a, b) -> Objects.equals(a, b) ? 0 : -1;

    public MyCircularDoublyLinkedList() {
        sentinel = new DoublyNode<>(null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        count = 0;
    }

    public void clear() {
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        count = 0;
    }

    public int size() {
        return count;
    }

    public E peekFirst() {
        return (count == 0) ? null : sentinel.next.data;
    }

    public E peekLast() {
        return (count == 0) ? null : sentinel.prev.data;
    }

    public void addFirst(E data) {
        Checker.checkNotNull(data);

        DoublyNode<E> node = new DoublyNode<>(data);

        node.next = sentinel.next;
        node.prev = sentinel;

        sentinel.next.prev = node;
        sentinel.next = node;

        count++;
    }

    public void addLast(E data) {
        Checker.checkNotNull(data);

        DoublyNode<E> node = new DoublyNode<>(data);

        node.next = sentinel;
        node.prev = sentinel.prev;

        sentinel.prev.next = node;
        sentinel.prev = node;

        count++;
    }

    public E pollFirst() {
        if (count == 0) return null;

        DoublyNode<E> first = sentinel.next;
        E data = first.data;

        sentinel.next = first.next;
        first.next.prev = sentinel;

        first.prev = null;
        first.next = null;

        count--;
        return data;
    }

    public E pollLast() {
        if (count == 0) return null;

        DoublyNode<E> last = sentinel.prev;
        E data = last.data;

        sentinel.prev = last.prev;
        last.prev.next = sentinel;

        last.prev = null;
        last.next = null;

        count--;
        return data;
    }

    public void add(int i, E data) {
        if (i < 0 || i > count) throw new IndexOutOfBoundsException("Index cannot be out of bound");
        Checker.checkNotNull(data);

        if (i == count) {
            addLast(data);
            return;
        }

        DoublyNode<E> curNode = getNode(i);
        DoublyNode<E> newNode = new DoublyNode<>(data);

        newNode.next = curNode;
        newNode.prev = curNode.prev;
        curNode.prev.next = newNode;
        curNode.prev = newNode;

        count++;
    }

    public E remove(int i) {
        Checker.checkIndex(i, count);

        DoublyNode<E> node = getNode(i);
        E data = node.data;

        node.next.prev = node.prev;
        node.prev.next = node.next;

        node.prev = null;
        node.next = null;

        count--;
        return data;
    }

    public boolean remove(E data) {
        return remove(data, comparator);
    }

    public boolean remove(E data, Comparator<? super E> comp) {
        if (data == null || comp == null) throw new IllegalArgumentException("Data, Comparator cannot be null");

        if (count > 0 && comp.compare(sentinel.prev.data, data) == 0) {
            pollLast();
            return true;
        }

        DoublyNode<E> cur = sentinel.next;

        while (cur != sentinel.prev) {
            if (comp.compare(cur.data, data) == 0) {
                cur.next.prev = cur.prev;
                cur.prev.next = cur.next;

                cur.prev = null;
                cur.next = null;

                count--;
                return true;
            }
            cur = cur.next;
        }

        return false;
    }

    public E set(int i, E data) {
        Checker.checkIndex(i, count);
        Checker.checkNotNull(data);

        DoublyNode<E> node = getNode(i);
        E replaced = node.data;
        node.data = data;

        return replaced;
    }

    public E get(int i) {
        Checker.checkIndex(i, count);
        return getNode(i).data;
    }

    public int indexOf(E data) {
        return indexOf(data, comparator);
    }

    public int indexOf(E data, Comparator<? super E> comp) {
        if (data == null) return -1;

        DoublyNode<E> cur = sentinel.next;

        for (int index = 0; index < count; index++) {
            if (comp.compare(cur.data, data) == 0) return index;
            cur = cur.next;
        }

        return -1;
    }

    public Iterator<E> iterator() {
        return new Iterator<>() {
            private DoublyNode<E> next = sentinel.next;

            public boolean hasNext() {
                return next != sentinel;
            }

            public E next() {
                if (next == sentinel) throw new NoSuchElementException("Element not found");
                E data = next.data;
                next = next.next;
                return data;
            }

            public void remove() {
                throw new UnsupportedOperationException("The operation is not suppoerted");
            }
        };
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(Printer<E> printer) {
        if (sentinel.next == sentinel) return "CircularDoublyLinkedList: [ empty ]";

        Printer<E> safePrinter = (printer == null) ? (Object::toString) : printer;

        StringBuilder sb = new StringBuilder();
        sb.append("CircularDoublyLinkedList (size").append(count).append("): ");

        DoublyNode<E> cur = sentinel.next;
        while (cur != sentinel) {
            sb.append("[").append(safePrinter.print(cur.data)).append("]");

            if(cur.next == sentinel) sb.append(" -> (sentinel)");
            else sb.append(" -> ");

            cur = cur.next;
        }

        return sb.toString();
    }

    private DoublyNode<E> getNode(int i) {
        DoublyNode<E> cur;

        if (i < count / 2) {
            cur = sentinel.next;
            for (int index = 0; index < i; index++)
                cur = cur.next;
        } else {
            cur = sentinel.prev;
            for (int index = count - 1; index > i; index--)
                cur = cur.prev;
        }

        return cur;
    }
}
