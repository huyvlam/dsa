package mystack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStackTest {
    private MyStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new MyStack<>();
    }

    @Test
    @DisplayName("Should add element to the top")
    void testPush() {
        assertEquals(4, stack.push(4));
        assertEquals(6, stack.push(6));
        assertEquals(6, stack.peek());
    }

    @Test
    @DisplayName("Should remove top element")
    void testPop() {
        stack.push(3);
        stack.push(5);

        assertEquals(5, stack.pop());
        assertEquals(3, stack.pop());
        assertTrue(stack.empty());
    }

    @Test
    @DisplayName("Should return the distance of element to the top")
    void testDistanceSearch() {
        stack.push(1);
        stack.push(3);
        stack.push(5);

        assertEquals(1, stack.search(stack.peek()));
        assertEquals(2, stack.search(3));
        assertEquals(3, stack.search(1));
    }
}