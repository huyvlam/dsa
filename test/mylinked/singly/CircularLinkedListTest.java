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
}