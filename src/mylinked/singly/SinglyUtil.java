package mylinked.singly;

import myinterface.Printer;

public class SinglyUtil {
    public static <E> String toString(SinglyNode<E> head, int size, Printer<E> printer) {
        if (head == null) return "List: [ empty ]";

        Printer<E> safePrinter = (printer == null) ? (Object::toString) : printer;

        StringBuilder sb = new StringBuilder();
        sb.append("List (size ").append(size).append("): ");

        SinglyNode<E> cur = head;
        while (cur != null) {
            sb.append("[").append(safePrinter.print(cur.data)).append("]");

            if (cur.next != null) sb.append(" -> ");
            else sb.append(" -> null");

            cur = cur.next;
        }

        return sb.toString();
    }
}
