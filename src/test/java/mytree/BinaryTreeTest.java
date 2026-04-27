package mytree;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
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
        assertEquals(32, abt.maxPathSum());

        lbt.insert(-10);
        lbt.insert(5);
        lbt.insert(24);
        lbt.insert(13);
        assertEquals(32, lbt.maxPathSum());

        abt.clear();
        lbt.clear();

        abt.insert(-10);
        abt.insert(5);
        abt.insert(2);
        abt.insert(13);
        assertEquals(18, abt.maxPathSum());

        lbt.insert(-10);
        lbt.insert(5);
        lbt.insert(2);
        lbt.insert(13);
        assertEquals(18, lbt.maxPathSum());
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
    }

    @Test
    @DisplayName("Should compute the minimum depth of a tree")
    void testMinDepth() {
        int n = 13;
        ArrayBinaryTree abt = new ArrayBinaryTree(n);
        LinkedBinaryTree lbt = new LinkedBinaryTree();
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int val = rand.nextInt();
            abt.insert(val);
            lbt.insert(val);
        }
        assertEquals(abt.minDepth(), lbt.minDepth());
    }

    @Test
    @DisplayName("Should return the sibling of a given node")
    void testGetSibling() {
        ArrayBinaryTree abt = new ArrayBinaryTree(4);
        LinkedBinaryTree lbt = new LinkedBinaryTree();

        abt.insert(-10);
        abt.insert(5);
        abt.insert(24);

        assertEquals(Integer.MIN_VALUE, abt.getSibling(-10));
        assertEquals(24, abt.getSibling(5));
        assertEquals(5, abt.getSibling(24));

        lbt.insert(5);
        lbt.insert(36);
        lbt.insert(7);

        assertNull(lbt.getSibling(5));
        assertEquals(7, lbt.getSibling(36).value);
        assertEquals(36, lbt.getSibling(7).value);
    }

    @Test
    @DisplayName("Should convert array binary tree into a Max Heap")
    void testMaxHeapView() {
        int n = 16;
        ArrayBinaryTree abt = new ArrayBinaryTree(n);
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            abt.insert(rand.nextInt());
        }

        try (MaxHeapView maxHeap = abt.createMaxHeap().get()) {
            int root = maxHeap.pop();

            while (abt.size() > n / 2) {
                int cur = maxHeap.pop();
                assertTrue(cur < root);
                root = cur;
            }

            maxHeap.insert(root + 3);
            assertTrue(maxHeap.peek() > root);

            maxHeap.replace(root);
            assertEquals(root, maxHeap.peek());

            abt.insert(46);
            assertThrows(ConcurrentModificationException.class, maxHeap::sort);

            maxHeap.sync();
            String str = maxHeap.toString();
            assertTrue(str.contains("Heap View (size"));

            maxHeap.sort();
            assertTrue(abt.tree[0] < abt.tree[5]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should convert array binary tree into a Min Heap")
    void testMinHeapView() {
        int n = 16;
        ArrayBinaryTree abt = new ArrayBinaryTree(n);
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            abt.insert(rand.nextInt());
        }

        try (MinHeapView minHeap = abt.createMinHeap().get()) {
            int root = minHeap.pop();

            while (abt.size() > n / 2) {
                int cur = minHeap.pop();
                assertTrue(cur > root);
                root = cur;
            }

            minHeap.insert(root - 3);
            assertTrue(minHeap.peek() < root);

            minHeap.replace(root);
            assertEquals(root, minHeap.peek());

            abt.insert(25);
            assertThrows(ConcurrentModificationException.class, minHeap::sort);

            minHeap.sync();
            String str = minHeap.toString();
            assertTrue(str.contains("Heap View (size"));

            minHeap.sort();
            assertTrue(abt.tree[0] > abt.tree[5]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }}
