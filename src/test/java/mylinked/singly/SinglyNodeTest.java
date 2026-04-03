package mylinked.singly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SinglyNodeTest {
    private SinglyNode<Integer> node;

    @BeforeEach
    void setUp() {
        node = new SinglyNode<>(1);
    }

    @Test
    @DisplayName("Should contain fields data/next")
    void testContainFields() {
        node = new SinglyNode<>(2);

        assertEquals(2, node.data);
        assertNull(node.next);
    }
}
