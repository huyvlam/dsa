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
        Checker.checkNullArgument(data);

        DoublyNode<E> node = new DoublyNode<>(data);

        node.next = sentinel.next;
        node.prev = sentinel;

        sentinel.next.prev = node;
        sentinel.next = node;

        count++;
    }

    public void addLast(E data) {
        Checker.checkNullArgument(data);

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
        Checker.checkNullArgument(data);

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
        Checker.checkBound(i, count);

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
        if (data == null || comparator == null) throw new IllegalArgumentException("Data, Comparator cannot be null");

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
        Checker.checkBound(i, count);
        Checker.checkNullArgument(data);

        DoublyNode<E> node = getNode(i);
        E replaced = node.data;
        node.data = data;

        return replaced;
    }

    public E get(int i) {
        Checker.checkBound(i, count);
        return getNode(i).data;
    }

    public int indexOf(E data) {
        return indexOf(data, comparator);
    }

    public int indexOf(E data, Comparator<? super E> comp) {
        Checker.checkNullArgument(comp, "Comparator");
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
            private DoublyNode<E> lastReturn = null;

            public boolean hasNext() {
                return next != sentinel;
            }

            public E next() {
                if (next == sentinel) throw new NoSuchElementException("Element not found");
                lastReturn = next;
                next = next.next;
                return lastReturn.data;
            }

            public void remove() {
                if (lastReturn == null) throw new IllegalStateException("This method can only be called once after calling next method");

                lastReturn.prev.next = lastReturn.next;
                lastReturn.next.prev = lastReturn.prev;

                lastReturn.prev = null;
                lastReturn.next = null;

                count--;
                lastReturn = null;
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

    void checkInvariants() {
        // 1. Sentinel must not be null
        assert sentinel != null;

        // 2. Sentinel links must not be null
        assert sentinel.next != null;
        assert sentinel.prev != null;

        // 3. Empty list invariant
        if (count == 0) {
            assert sentinel.next == sentinel;
            assert sentinel.prev == sentinel;
            return;
        }

        //4. Non-empty boundary invariant
        assert sentinel.next.prev == sentinel;
        assert sentinel.prev.next == sentinel;

        int actual = 0;
        DoublyNode<E> cur = sentinel.next;

        while (cur != sentinel) {
            // 5. No null pointers
            assert cur.next != null;
            assert cur.prev != null;

            // 6. Bidirectional consistency
            assert cur.next.prev == cur;
            assert cur.prev.next == cur;

            actual++;
            cur = cur.next;

            // prevent infinite loop if corrupted
            assert actual <= count;
        }

        // 7. Count must match the actual number of nodes
        assert actual == count;
    }
}
