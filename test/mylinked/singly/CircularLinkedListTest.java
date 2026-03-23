package mylinked.singly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircularLinkedListTest {
    private CircularLinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = new CircularLinkedList<>(null);
    }

    @Test
    @DisplayName("Should add at both ends and maintain head/tail")
    void testAddFirstLast() {
        list.addLast("Kiwi");
        list.addFirst("Mangosteen");
        list.addLast("Passionfruit");

        assertEquals("Mangosteen", list.peekFirst());
        assertEquals("Passionfruit", list.peekLast());
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("Should handle pollFirst, pollLast on single element")
    void testPollSingleElement() {
        list.addFirst("Apple");
        list.addLast("Cherry");

        assertEquals("Apple", list.pollFirst());
        assertEquals("Cherry", list.pollLast());
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Should add/remove element by the given index")
    void testAddRemoveByIndex() {
        list.addFirst("Peach");
        list.addLast("Apricot");
        list.add(1, "Nectarine");

        assertEquals(1, list.indexOf("Nectarine"));
        assertEquals("Apricot", list.remove(2));
        assertEquals("Peach", list.remove(0));
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Should remove by the given data")
    void testRemoveByData() {
        list.addFirst("Lychee");
        list.addLast("Longan");
        list.add(1, "Rambutan");

        assertFalse(list.remove("Apple"));
        assertTrue(list.remove("Rambutan"));
        assertTrue(list.remove("Lychee"));
        assertTrue(list.remove("Longan"));
        assertEquals(0, list.size());
    }
}