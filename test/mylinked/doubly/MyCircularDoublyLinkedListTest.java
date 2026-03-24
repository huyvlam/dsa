package mylinked.doubly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyCircularDoublyLinkedListTest {
    private MyCircularDoublyLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new MyCircularDoublyLinkedList<>();
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

    @Test
    @DisplayName("Should poll single element at either end")
    void testPollFirstLast() {
        list.addFirst(34);
        list.addFirst(65);
        list.addLast(17);
        list.addLast(9);

        assertEquals(65, list.pollFirst());
        assertEquals(9, list.pollLast());
        assertEquals(34, list.pollFirst());
        assertEquals(17, list.pollLast());
        assertNull(list.peekFirst());
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Should add/remove data by the given index")
    void testAddRemoveByIndex() {
        list.add(0, 13);
        list.add(1, 45);
        list.add(2, 87);
        list.add(1, 69);

        assertEquals(13, list.peekFirst());
        assertEquals(87, list.peekLast());

        assertEquals(45, list.remove(2));
        assertEquals(69, list.remove(1));

        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Should remove by data and return true for success")
    void testRemoveByData() {
        list.addLast(75);
        list.addFirst(21);
        list.addFirst(64);

        assertTrue(list.remove((Integer) 21));
        assertFalse(list.remove((Integer) 22));
    }

    @Test
    @DisplayName("Should replace/get data by the given index")
    void testGetSetByIndex() {
        list.addLast(68);
        list.addLast(49);
        list.addLast(52);

        assertEquals(49, list.set(1, 37));
        assertEquals(37, list.get(1));
        assertEquals(52, list.set(2, 13));
        assertEquals(13, list.get(2));
    }
}