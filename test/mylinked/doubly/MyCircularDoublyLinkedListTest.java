package mylinked.doubly;

import mymodel.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MyCircularDoublyLinkedListTest {
    private MyCircularDoublyLinkedList<Integer> listI;
    private MyCircularDoublyLinkedList<Person> listP;

    @BeforeEach
    void setUp() {
        listI = new MyCircularDoublyLinkedList<>();
        listP = new MyCircularDoublyLinkedList<>();
    }

    @Test
    @DisplayName("Should add at both ends and update head/tail pointer")
    void testAddFirstLast() {
        listI.addLast(23);
        listI.addFirst(54);
        listI.addLast(89);

        assertEquals(54, listI.peekFirst());
        assertEquals(89, listI.peekLast());
        assertEquals(3, listI.size());

        listI.clear();

        assertNull(listI.peekFirst());
        assertNull(listI.peekLast());
    }

    @Test
    @DisplayName("Should poll single element at either end")
    void testPollFirstLast() {
        listI.addFirst(34);
        listI.addFirst(65);
        listI.addLast(17);
        listI.addLast(9);

        assertEquals(65, listI.pollFirst());
        assertEquals(9, listI.pollLast());
        assertEquals(34, listI.pollFirst());
        assertEquals(17, listI.pollLast());
        assertNull(listI.peekFirst());
        assertEquals(0, listI.size());
    }

    @Test
    @DisplayName("Should add/remove data by the given index")
    void testAddRemoveByIndex() {
        listI.add(0, 13);
        listI.add(1, 45);
        listI.add(2, 87);
        listI.add(1, 69);

        int i1 = listI.indexOf(45);
        int i2 = listI.indexOf(69);

        assertEquals(13, listI.get(0));
        assertEquals(87, listI.peekLast());
        assertEquals(45, listI.remove(i1));
        assertEquals(69, listI.remove(i2));
        assertEquals(2, listI.size());
    }

    @Test
    @DisplayName("Should remove by data and return true for success")
    void testRemoveByData() {
        listI.addLast(75);
        listI.addFirst(21);
        listI.addFirst(64);

        assertTrue(listI.remove((Integer) 21));
        assertFalse(listI.remove((Integer) 22));
    }

    @Test
    @DisplayName("Should remove by data using custom comparator")
    void testRemoveByDataCustomComparator() {
        listP.addLast(new Person("Jana", 34));
        listP.addLast(new Person("Yoga", 51));

        assertTrue(listP.remove(new Person("Java", 34), Comparator.comparingInt(p -> p.age)));
        assertTrue(listP.remove(new Person("Yoga", 26), Comparator.comparing(p -> p.name)));
    }

    @Test
    @DisplayName("Should replace/get data by the given index")
    void testGetSetByIndex() {
        listI.addLast(68);
        listI.addLast(49);
        listI.addLast(52);

        assertEquals(49, listI.set(1, 37));
        assertEquals(37, listI.get(1));
        assertEquals(52, listI.set(2, 13));
        assertEquals(13, listI.get(2));
    }

    @Test
    @DisplayName("Should throw exception for out of bound access")
    void testOutOfBounds() {
        listI.addLast(68);

        assertThrows(IndexOutOfBoundsException.class, () -> listI.set(-1, 37));
        assertThrows(IndexOutOfBoundsException.class, () -> listI.get(2));
    }

    @Test
    @DisplayName("Should iterate through every element in the list")
    void testIterator() {
        listI.addFirst(89);
        listI.addFirst(79);

        Iterator<Integer> it = listI.iterator();

        assertTrue(it.hasNext());
        assertEquals(79, it.next());
        assertTrue(it.hasNext());
        assertEquals(89, it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @DisplayName("Should print all elements in the list")
    void testToString() {
        listI.addLast(90);
        listI.addLast(34);
        listI.addLast(16);
        String res = listI.toString();

        assertTrue(res.contains("[90] -> [34] -> [16]"));
        assertTrue(res.contains(" -> (sentinel)"));
    }
}