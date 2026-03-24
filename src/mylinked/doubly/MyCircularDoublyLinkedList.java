package mylinked.doubly;

import java.util.Objects;

public class MyCircularDoublyLinkedList<E> {
    private final DoublyNode<E> sentinel;
    private int count;

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
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

        DoublyNode<E> node = new DoublyNode<>(data);

        node.next = sentinel.next;
        node.prev = sentinel;

        sentinel.next.prev = node;
        sentinel.next = node;

        count++;
    }

    public void addLast(E data) {
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

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
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

        if (i == 0) {
            addFirst(data);
            return;
        }
        if (i == count) {
            addLast(data);
            return;
        }

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

        DoublyNode<E> node = new DoublyNode<>(data);
        node.next = cur;
        node.prev = cur.prev;
        cur.prev.next = node;
        cur.prev = node;

        count++;
    }

    public E remove(int i) {
        if (i < 0 || i >= count) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        if (i == 0) return pollFirst();
        if (i == count - 1) return pollLast();

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

        E data = cur.data;
        cur.next.prev = cur.prev;
        cur.prev.next = cur.next;

        cur.prev = null;
        cur.next = null;

        count--;
        return data;
    }

    public boolean remove(E data) {
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

        if (count > 0 && Objects.equals(sentinel.prev.data, data)) {
            pollLast();
            return true;
        }

        DoublyNode<E> cur = sentinel.next;

        while (cur != sentinel.prev) {
            if (Objects.equals(cur.data, data)) {
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
        if (i < 0 || i >= count) throw new IndexOutOfBoundsException("Index cannot be out of bound");
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

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

        E replaced = cur.data;
        cur.data = data;

        return replaced;
    }

    public E get(int i) {
        if (i < 0 || i >= count) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        if (i == 0) return peekFirst();
        if (i == count - 1) return peekLast();

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

        return cur.data;
    }
}
