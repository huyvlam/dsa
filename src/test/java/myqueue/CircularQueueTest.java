package myqueue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CircularQueueTest {
    private CircularArrayQueue<Integer> arrayQ;
    private CircularLinkedQueue<Integer> linkedQ;

    @BeforeEach
    void setUp() {
        int capacity = 3;
        arrayQ = new CircularArrayQueue<>(capacity);
        linkedQ = new CircularLinkedQueue<>(capacity);
    }

    @Test
    @DisplayName("Should add/offer data in the back")
    void testAddOfferData() {
        assertTrue(arrayQ.add(5));
        assertTrue(linkedQ.add(5));

        assertTrue(arrayQ.offer(8));
        assertTrue(linkedQ.offer(8));

        assertTrue(arrayQ.add(3));
        assertTrue(linkedQ.add(3));

        assertEquals(3, arrayQ.size());
        assertEquals(3, linkedQ.size());

        assertEquals(5, arrayQ.element());
        assertEquals(5, linkedQ.element());

        arrayQ.clear();
        linkedQ.clear();

        assertTrue(arrayQ.isEmpty());
        assertTrue(linkedQ.isEmpty());
    }

    @Test
    @DisplayName("Should poll/remove data in the front")
    void testPollRemoveData() {
        assertTrue(arrayQ.add(5));
        assertTrue(linkedQ.add(5));

        assertTrue(arrayQ.offer(8));
        assertTrue(linkedQ.offer(8));

        assertEquals(5, arrayQ.poll());
        assertEquals(5, linkedQ.poll());

        assertEquals(8, arrayQ.peek());
        assertEquals(8, linkedQ.peek());

        assertEquals(8, arrayQ.remove());
        assertEquals(8, linkedQ.remove());

        assertNull(arrayQ.peek());
        assertNull(linkedQ.peek());
    }

    @Test
    @DisplayName("Should return true if the queue contains given element")
    void testContains() {
        assertTrue(arrayQ.add(5));
        assertTrue(linkedQ.add(5));

        assertTrue(arrayQ.contains(5));
        assertTrue(linkedQ.contains(5));
        assertFalse(arrayQ.contains(6));
        assertFalse(linkedQ.contains(6));
    }

    @Test
    @DisplayName("Should throw exception when empty or capacity is reached")
    void testThrowExceptions() {
        assertThrows(NoSuchElementException.class, () -> arrayQ.element());
        assertThrows(NoSuchElementException.class, () -> linkedQ.element());

        assertThrows(NoSuchElementException.class, () -> arrayQ.remove());
        assertThrows(NoSuchElementException.class, () -> linkedQ.remove());

        arrayQ.add(5);
        arrayQ.add(6);
        arrayQ.add(7);
        assertThrows(IllegalStateException.class, () -> arrayQ.add(8));

        linkedQ.add(5);
        linkedQ.add(6);
        linkedQ.add(7);
        assertThrows(IllegalStateException.class, () -> linkedQ.add(8));
    }

    @AfterEach
    void tearDown() {
        arrayQ.clear();
        linkedQ.clear();
    }
}