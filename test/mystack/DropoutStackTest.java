package mystack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

class DropoutStackTest {
    private DropoutStack<String> stack;

    @BeforeEach
    void setUp() {
        int MAX_CAPACITY = 3;
        stack = new DropoutStack<>(MAX_CAPACITY);
    }

    @Test
    @DisplayName("Should drop oldest element when exceeding max capacity")
    void testDropOldest() {
        stack.push("compound");
        stack.push("horsebow");
        stack.push("barebow");

        assertEquals(3, stack.search("compound"));

        stack.push("recurve");

        assertEquals(3, stack.search("horsebow"));
    }

    @Test
    @DisplayName("Should add element to the top")
    void testPush() {
        assertEquals("recurve", stack.push("recurve"));
        assertEquals("horsebow", stack.push("horsebow"));
        assertEquals("barebow", stack.push("barebow"));
        assertEquals("barebow", stack.peek());
    }

    @Test
    @DisplayName("Should remove top element")
    void testPop() {
        stack.push("compound");
        assertEquals("compound", stack.pop());
        assertTrue(stack.empty());
    }

    @Test
    @DisplayName("Should return the distance of element to the top")
    void testDistanceSearch() {
        stack.push("recurve");
        stack.push("horsebow");
        stack.push("barebow");

        assertEquals(1, stack.search("barebow"));
        assertEquals(2, stack.search("horsebow"));
        assertEquals(3, stack.search("recurve"));
    }

    @Test
    @DisplayName("Should throw exception if stack is empty")
    void testEmptyException() {
        assertThrows(EmptyStackException.class, () -> stack.peek());
        assertThrows(EmptyStackException.class, () -> stack.pop());
    }
}