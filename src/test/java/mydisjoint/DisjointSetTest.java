package mydisjoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DisjointSetTest {
    private DisjointSet ds;
    private int n;

    @BeforeEach
    void setUp() {
        n = 5;
        ds = new DisjointSet(n);
    }

    @Test
    @DisplayName("Each element should be the root of its own set initially")
    void testInitialState() {
        assertEquals(5, ds.getCount());

        for (int i = 0; i < n; i++) {
            assertEquals(i, ds.find(i));
        }

        assertNotEquals(ds.find(0), ds.find(1));
    }

    @Test
    @DisplayName("Should merge two distinct sets and decrement counts")
    void testUnionAndConnected() {
        assertEquals(5, ds.getCount());

        assertTrue(ds.union(0, 1));
        assertEquals(ds.find(0), ds.find(1), "Should have the same root");
        assertEquals(4, ds.getCount(), "Should decrement count");

        assertTrue(ds.union(1, 2));
        assertTrue(ds.connected(0, 2), "0 and 2 should be in the same set");
        assertEquals(3, ds.getCount());

        assertFalse(ds.union(0, 2), "Should return false since the two are already connected");
        assertEquals(3, ds.getCount(), "Count should remain the same");
    }

    @Test
    @DisplayName("Should merge distinct sets by size to maintain balance")
    void testUnionBySize() {
        // Set A {0, 1, 2}
        ds.union(0, 1);
        ds.union(0, 2);

        // Set B {3, 4}
        ds.union(3, 4);

        assertEquals(2, ds.getCount());

        int rootA = ds.find(0);
        int rootB = ds.find(3);
        ds.union(rootA, rootB);

        assertEquals(rootA, ds.find(rootB));
        assertEquals(1, ds.getCount());
    }

    @Test
    @DisplayName("Should throw exceptions for invalid bounds")
    void testInvalidBounds() {
        assertThrows(IllegalArgumentException.class, () -> ds.find(-1));
        assertThrows(IllegalArgumentException.class, () -> ds.find(ds.getCount()));
    }
}
