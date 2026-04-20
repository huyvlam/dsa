package mytree;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryTreeTest {
    @Test
    @DisplayName("Should add/delete data")
    void testAddDelete() {
        ArrayBinaryTree abt = new ArrayBinaryTree(8);
        LinkedBinaryTree lbt = new LinkedBinaryTree();

        abt.insert(7);
        lbt.insert(7);

        assertEquals(1, abt.size());
        assertEquals(1, lbt.size());

        abt.delete(7);
        lbt.delete(7);

        assertTrue(abt.isEmpty());
        assertTrue(lbt.isEmpty());
    }

    @Test
    @DisplayName("The height of a complete tree should equate to log2 of its size")
    void testTreeHeight() {
        ArrayBinaryTree abt = new ArrayBinaryTree(8);
        LinkedBinaryTree lbt = new LinkedBinaryTree();

        Random rand = new Random();
        int[] sizes = {3, 7, 8};
        for (int s : sizes) {
            for (int i = 0; i < s; i++) {
                int num = rand.nextInt(13);
                abt.insert(num);
                lbt.insert(num);
            }

            int abtHeight = 31 - Integer.numberOfLeadingZeros(abt.size());

            assertEquals(abt.height(), abtHeight);
            assertEquals(abt.height(), lbt.height());
        }
    }

    @Test
    @DisplayName("Should compute the max path sum")
    void testMaxPathSum() {
        ArrayBinaryTree abt = new ArrayBinaryTree(8);
        LinkedBinaryTree lbt = new LinkedBinaryTree();

        abt.insert(-10);
        abt.insert(5);
        abt.insert(24);
        abt.insert(13);
        assertEquals(32, abt.getMaxPathSum());

        lbt.insert(-10);
        lbt.insert(5);
        lbt.insert(24);
        lbt.insert(13);
        assertEquals(32, lbt.getMaxPathSum());

        abt.clear();
        lbt.clear();

        abt.insert(-10);
        abt.insert(5);
        abt.insert(2);
        abt.insert(13);
        assertEquals(18, abt.getMaxPathSum());

        lbt.insert(-10);
        lbt.insert(5);
        lbt.insert(2);
        lbt.insert(13);
        assertEquals(18, lbt.getMaxPathSum());
    }

    @Test
    @DisplayName("Should find a tree element by its value")
    void testFindByValue() {
        int n = 4;
        ArrayBinaryTree abt = new ArrayBinaryTree(n);
        LinkedBinaryTree lbt = new LinkedBinaryTree();
        int[] data = new int[n];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            data[i] = rand.nextInt();
            abt.insert(data[i]);
            lbt.insert(data[i]);
        }

        for (int i = 0; i < n; i++) {
            assertEquals(i, abt.findIndex(data[i]));
            assertEquals(data[i], lbt.find(data[i]).value);
        }
    }

    @Test
    @DisplayName("Should compute the depth and height of a given value")
    void testGetDepthHeight() {
        int n = 4;
        ArrayBinaryTree abt = new ArrayBinaryTree(n);
        LinkedBinaryTree lbt = new LinkedBinaryTree();
        int[] data = new int[n];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            data[i] = rand.nextInt();
            abt.insert(data[i]);
            lbt.insert(data[i]);
        }

        for (int i = 0; i < n; i++) {
            assertEquals(abt.getDepth(data[i]), lbt.getDepth(data[i]));

            if (i == 0) {
                assertEquals(0, lbt.getDepth(data[i]));
            } else if (i < 3) {
                assertEquals(1, lbt.getDepth(data[i]));
            } else {
                assertEquals(2, lbt.getDepth(data[i]));
            }

            assertEquals(abt.getHeight(data[i]), lbt.getHeight(data[i]));

            if (i == 0) {
                assertEquals(2, abt.getHeight(data[i]));
            } else if (i < 3) {
                assertEquals(1, abt.getHeight(data[i]));
            } else {
                assertEquals(0, abt.getHeight(data[i]));
            }
        }
    }}
