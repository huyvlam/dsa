package mylinked.doubly;

public class DoublyNode<E> {
    public E data;
    public DoublyNode<E> next, prev;
    public DoublyNode(E data) {
        this.data = data;
        next = null;
        prev = null;
    }
}

