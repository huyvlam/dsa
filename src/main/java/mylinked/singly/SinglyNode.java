package mylinked.singly;

public class SinglyNode<E> {
    public E data;
    public SinglyNode<E> next;
    public SinglyNode(E data) {
        this.data = data;
        next = null;
    }
}
