package myqueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class QueueTest {
    private Queue<Integer> queue;

    @BeforeEach
    void setUp() {
        int capacity = 3;
        queue = new Queue<>(capacity);
    }

    @Test
    @DisplayName("Should add/offer data in the rear")
    void testAddOfferData() {
        assertTrue(queue.add(5));
        assertTrue(queue.offer(8));
        assertTrue(queue.add(9));

        assertFalse(queue.offer(2));

        assertEquals(5, queue.poll());
        assertEquals(8, queue.remove());
        assertEquals(9, queue.element());

        assertEquals(9, queue.remove());
        assertNull(queue.peek());
        assertNull(queue.poll());
    }

    @Test
    @DisplayName("Should throw exception when empty or capacity is reached")
    void testThrowExceptions() {
        assertTrue(queue.offer(7));
        assertTrue(queue.add(9));
        assertTrue(queue.offer(4));

        assertThrows(IllegalStateException.class, () -> queue.add(5));

        assertEquals(7, queue.poll());
        assertEquals(9, queue.remove());
        assertEquals(4, queue.poll());

        assertThrows(NoSuchElementException.class, () -> queue.remove());
        assertThrows(NoSuchElementException.class, () -> queue.element());
    }
}