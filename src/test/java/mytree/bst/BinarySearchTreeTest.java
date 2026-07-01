package mytree.bst;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BinarySearchTreeTest {
    @Test
    @DisplayName("Should insert and delete data recursively")
    void testRecursiveInsertDelete() {
        BinarySearchTree bst = new BinarySearchTree();

        bst.recursiveInsert(23);
        bst.recursiveInsert(9);
        bst.recursiveInsert(46);
        bst.recursiveInsert(15);

        assertEquals(4, bst.size());
        assertTrue(bst.root.value > bst.root.left.value);
        assertTrue(bst.root.value < bst.root.right.value);
        assertEquals(15, bst.root.left.right.value);

        bst.recursiveDelete(9);
        bst.recursiveDelete(23);

        assertEquals(15, bst.root.left.value);
        assertEquals(46, bst.root.value);
        assertEquals(2, bst.size());
    }

    @Test
    @DisplayName("Should insert and delete data iteratively")
    void testIterativeInsertDelete() {
        BinarySearchTree bst = new BinarySearchTree();

        bst.iterativeInsert(25);
        bst.iterativeInsert(48);
        bst.iterativeInsert(19);
        bst.iterativeInsert(36);

        assertEquals(4, bst.size());
        assertTrue(bst.root.value > bst.root.left.value);
        assertTrue(bst.root.value < bst.root.right.value);
        assertEquals(36, bst.root.right.left.value);

        bst.iterativeDelete(25);
        assertEquals(19, bst.root.value);

        bst.iterativeDelete(19);
        assertEquals(48, bst.root.value);
    }
}
