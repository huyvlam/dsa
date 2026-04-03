package mylinked.doubly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DoublyNodeTest {
    private DoublyNode<String> node;

    @BeforeEach
    void setUp() {
        node = new DoublyNode<>("Bach");
    }

    @Test
    @DisplayName("Should contain fields data/next/prev")
    void testContainsField() {
        node = new DoublyNode<>("Bach");
        node.prev = node;

        assertEquals("Bach", node.data);
        assertEquals(node, node.prev);
        assertNull(node.next);
    }
}