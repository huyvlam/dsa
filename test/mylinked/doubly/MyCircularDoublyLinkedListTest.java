package mylinked.doubly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

class MyCircularDoublyLinkedListTest {
    private MyCircularDoublyLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new MyCircularDoublyLinkedList<>(Comparator.naturalOrder());
    }

    @Test
    @DisplayName("Should add at both ends and update head/tail pointer")
    void testAddFirstLast() {
        list.addLast(23);
        list.addFirst(54);
        list.addLast(89);

        assertEquals(54, list.peekFirst());
        assertEquals(89, list.peekLast());
        assertEquals(3, list.size());

        list.clear();

        assertNull(list.peekFirst());
        assertNull(list.peekLast());
    }
}