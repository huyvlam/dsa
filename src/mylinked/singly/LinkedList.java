package mylinked.singly;

import java.util.Comparator;

import mymodel.Person;
import myinterface.Printer;

public class LinkedList<E> {
    public SinglyNode<E> head, tail;
    private int count;
    private final Comparator<? super E> comparator;

    public LinkedList(Comparator<? super E> comparator) {
        this.comparator = comparator;
        head = null;
        tail = null;
        count = 0;
    }

    public LinkedList() {
        this((a, b) -> {
            // type check data to avoid ClassCastException
            if (a instanceof Comparable && b instanceof Comparable)
                return ((Comparable) a).compareTo(b);
            return a.equals(b) ? 0 : -1; // fallback for comparables
        });
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
        if (i < 0 || i > size()) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        if (i == 0 || size() == 0) {
            addFirst(data);
            return;
        }

        if (i == size()) {
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

    public boolean remove(E data) {
        if (data == null || size() == 0) return false;

        if (comparator.compare(head.data, data) == 0)
            return pollFirst() != null;

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
        if (i < 0 || i >= size()) throw new IndexOutOfBoundsException("Index cannot be out of bound");
        if (data == null) throw new IllegalArgumentException("Data cannot be null");

        SinglyNode<E> cur = head;
        for (int index = 0; index < i; index++)
            cur = cur.next;

        E former = cur.data;
        cur.data = data;

        return former;
    }

    public E get(int i) {
        if (i < 0 || i >= size()) throw new IndexOutOfBoundsException("Index cannot be out of bound");

        if (i == 0) return peekFirst();

        if (i == size() - 1) return peekLast();

        SinglyNode<E> cur = head;
        for (int count = 0; count < i; count++)
            cur = cur.next;

        return cur.data;
    }

    public int indexOf(E data) {
        if (head == null || data == null) return -1;

        int index = 0;
        SinglyNode<E> cur = head;

        while (cur != null) {
            if (comparator.compare(cur.data, data) == 0) return index;
            cur = cur.next;
            index++;
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
        return toString((E e) -> e.toString());
    }

    public String toString(Printer<E> printer) {
        if (head == null) return "List: [ empty ]";

        Printer<E> safePrinter = (printer == null) ? (e -> e.toString()) : printer;

        StringBuilder sb = new StringBuilder();
        sb.append("List (size ").append(count).append("): ");

        SinglyNode<E> cur = head;
        while (cur != null) {
            sb.append("[").append(printer.print(cur.data)).append("]");

            if (cur.next != null) {
                sb.append(" -> ");
            } else {
                sb.append(" -> null");
            }
            cur = cur.next;
        }

        return sb.toString();
    }

    static void main() {
        Comparator<Person> comp = (p1, p2) -> Integer.compare(p1.age, p2.age);
        LinkedList<Person> list = new LinkedList<>(comp);

        list.add(0, new Person("ha", 18));
        list.add(1, new Person("thu", 16));
        list.addLast(new Person("dong", 17));
        list.addFirst(new Person("xuan", 15));

        IO.println(list.toString());
        IO.println(list.toString((Person p) -> "Name: " + p.name + " Age: " + p.age));
    }
}
