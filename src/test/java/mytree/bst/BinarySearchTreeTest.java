package mytree.bst;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BinarySearchTreeTest {
    BinarySearchTree bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree();
    }

    @Test
    @DisplayName("Should insert and delete data")
    void testInsertDelete() {
        bst.recursiveInsert(23);
        bst.recursiveInsert(9);
        bst.iterativeInsert(46);
        bst.iterativeInsert(20);
        bst.iterativeInsert(17);

        assertTrue(bst.root.value > bst.root.left.value);
        assertTrue(bst.root.value < bst.root.right.value);
        assertEquals(20, bst.root.left.right.value);
        assertEquals(5, bst.size());

        bst.recursiveDelete(9);
        bst.iterativeDelete(23);

        assertEquals(17, bst.root.left.value);
        assertEquals(20, bst.root.value);
        assertEquals(3, bst.size());
    }

    @Test
    @DisplayName("Should compute and return height and depth of a node")
    void testGetHeightDepth() {
        bst.recursiveInsert(26);
        bst.recursiveInsert(40);
        bst.iterativeInsert(18);
        bst.iterativeInsert(9);
        bst.iterativeInsert(3);

        assertEquals(3, bst.getDepth(3));
        assertEquals(2, BinarySearchTree.getDepth(bst.root.left, 3));
        assertEquals(1, bst.getDepth(40));

        assertEquals(3, bst.getHeight());
        assertEquals(1, BinarySearchTree.getHeight(bst.root.left.left));
        assertEquals(0, BinarySearchTree.getHeight(bst.root.right));
    }
}
