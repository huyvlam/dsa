package mylinked.doubly;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class MyDoublyLinkedListTest {
    private MyDoublyLinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = new MyDoublyLinkedList<>();
    }

    @Test
    @DisplayName("Should add at both end and maintain head/tail")
    void testPreviousPointer() {
        list.addLast("Thu");
        list.addFirst("Ha");
        list.addFirst("Xuan");

        assertEquals(3, list.size());
        assertEquals("Xuan", list.peekFirst());
        assertEquals("Thu", list.peekLast());
    }

    @Test
    @DisplayName("Should handle pollFirst and pollLast on single element")
    void testPollSingleElement() {
        list.addFirst("Judo");
        list.addLast("Kundo");

        assertEquals("Judo", list.pollFirst());
        assertEquals("Kundo", list.pollLast());
        assertEquals(0, list.size());
        assertNull(list.peekFirst());
        assertNull(list.peekLast()); // Ensures tail was reset!
    }

    @Test
    @DisplayName("Should reverse the list and swap head/tail pointers")
    void testReverse() {
        list.addFirst("Xuan");
        list.addFirst("Ha");
        list.addFirst("Thu");
        list.addFirst("Dong");

        assertEquals("Thu", list.get(1));      // Middle stayed
        assertEquals("Ha", list.get(2));      // Middle stayed

        list.reverse();

        assertEquals("Xuan", list.peekFirst()); // New Head
        assertEquals("Dong", list.peekLast());  // New Tail
        assertEquals("Thu", list.get(2));      // Middle stayed
    }

    @Test
    @DisplayName("Should add/remove data by the given index")
    void testAddRemoveByIndex() {
        list.add(0, "Abby");
        list.add(1, "Donna");
        list.add(1, "Ciro");
        list.remove(2);

        assertEquals(0, list.indexOf("Abby"));
        assertEquals(1, list.indexOf("Ciro"));
        assertEquals(2, list.size());
        assertFalse(list.contains("Donna"));
    }

    @Test
    @DisplayName("Should remove by data using the comparator")
    void testRemoveByData() {
        list.addLast("Judo");
        list.addLast("Kundun");

        // Match only by age per comparator
        boolean removed = list.remove("Kundun");

        assertTrue(removed);
        assertEquals(1, list.size());
        assertEquals("Judo", list.peekFirst());
    }

    @Test
    @DisplayName("Should replace data at the given index")
    void testSetByIndex() {
        list.addLast("Abby");
        list.set(0, "Ciro");

        assertEquals("Ciro", list.get(0));
        assertFalse(list.contains("Abby"));
    }

    @Test
    @DisplayName("Should throw exception for out of bound access")
    void testBounds() {
        list.addFirst("Ha");

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, "Dong"));

    }

    @Test
    @DisplayName("Should use custom printer in toString")
    void testToStringCustomPrinter() {
        list.addFirst("Neferiti");
        String result = list.toString(p -> "Queen " + p);

        assertTrue(result.contains("Queen"));
        assertTrue(result.contains("-> null"));
    }
}