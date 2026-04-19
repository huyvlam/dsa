package mytree;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryTreeTest {
    private ArrayBinaryTree abt;
    private LinkedBinaryTree lbt;
    private Random rand;

    @BeforeEach
    void setup() {
        abt = new ArrayBinaryTree(8);
        lbt = new LinkedBinaryTree();
        rand = new Random();
    }

    @Test
    @DisplayName("Should add/delete data")
    void testAddDelete() {
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
        int[] sizes = {3, 7, 8};
        for (int s : sizes) {
            for (int i = 0; i < s; i++) {
                int num = rand.nextInt(13);
                abt.insert(num);
                lbt.insert(num);
            }

            int abtHeight = 31 - Integer.numberOfLeadingZeros(abt.size());
            int lbtHeight = 31 - Integer.numberOfLeadingZeros(lbt.size());

            assertEquals(abt.height(), abtHeight);
            assertEquals(lbt.height(), lbtHeight);

            abt.clear();
            lbt.clear();
        }
    }

    @Test
    @DisplayName("Should compute the max path sum")
    void testMaxPathSum() {
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

    @AfterEach
    void tearDown() {
        abt.clear();
        lbt.clear();
    }
}
