package mytree.bst;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @DisplayName("Should compute and return the sum of all leaf nodes")
    void testLeafSum() {
        bst.recursiveInsert(26);
        bst.recursiveInsert(40);
        bst.iterativeInsert(18);
        bst.iterativeInsert(9);
        bst.iterativeInsert(3);

        assertEquals(43, bst.getLeafSum());
        assertEquals(3, BinarySearchTree.getLeafSum(bst.root.left));
    }

    @Test
    @DisplayName("Should compute and return the max path sum of a node")
    void testMaxPathSum() {
        bst.recursiveInsert(26);
        bst.recursiveInsert(40);
        bst.iterativeInsert(18);
        bst.iterativeInsert(-9);
        bst.iterativeInsert(3);

        assertEquals(84, bst.getMaxPathSum());
        assertEquals(18, BinarySearchTree.getMaxPathSum(bst.root.left));
    }

    @Test
    @DisplayName("Should find and return the sibling of a node")
    void testFindSibling() {
        bst.recursiveInsert(26);
        bst.iterativeInsert(18);
        bst.recursiveInsert(40);
        bst.iterativeInsert(9);
        bst.recursiveInsert(57);
        bst.iterativeInsert(3);

        assertNull(bst.recursiveFindSibling(9));

        BSTNode sibling = bst.iterativeFindSibling(18);
        assertEquals(40, sibling.value);
    }

    @Test
    @DisplayName("Should find and return a node with matched value")
    void testSearch() {
        bst.recursiveInsert(26);
        bst.iterativeInsert(18);
        bst.recursiveInsert(40);
        bst.iterativeInsert(9);
        bst.recursiveInsert(57);
        bst.iterativeInsert(3);

        assertEquals(bst.root.right, bst.search(40));
        assertNull(bst.search(90));
        assertNull(BinarySearchTree.search(bst.root.left, 26));
    }
}
