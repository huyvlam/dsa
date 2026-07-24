package mydisjoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDisjointSetTest {
    private GenericDisjointSet<String> network;
    private int n;

    @BeforeEach
    public void setUp() {
//        List<String> users = Arrays.asList("Aimi", "Blanca", "Chai", "Dong", "Eve");
        String[] names = {"Aimi", "Blanca", "Chai", "Dong", "Eve"};
        network = new GenericDisjointSet<>(Arrays.asList(names));
    }

    @Test
    @DisplayName("Should connect and find users across sets")
    void testGenericConnectAndFind() {
        network.union("Aimi", "Blanca");
        network.union("Blanca", "Chai");

        assertTrue(network.connected("Aimi", "Blanca"));
        assertFalse(network.connected("Aimi", "Dong"));
        assertEquals(3, network.getCount());
    }

    @Test
    @DisplayName("Should return the same root for elements in the same set")
    void testFindRoot() {
        network.union("Dong", "Eve");

        assertEquals(network.findRoot("Dong"),  network.findRoot("Eve"), "Connected elements share the same root");
        assertNotEquals(network.findRoot("Eve"), network.findRoot("Chai"), "These elements are in different set");
    }

    @Test
    @DisplayName("Should return all disjoint sets along with the elements in each set")
    void testGetClusters() {
        // Cluster 1 {Aimi, Blanca, Chai}
        network.union("Aimi", "Blanca");
        network.union("Blanca", "Chai");

        // Cluster 2 {Dong, Eve}
        network.union("Dong", "Eve");

        Map<String, List<String>> clusters = network.getClusters();

        assertEquals(2, clusters.size(), "Should have 2 cluster groups");

        int totalElements = clusters.values().stream().mapToInt(List::size).sum();
        assertEquals(5, totalElements, "All 5 users should be counted in clusters");
    }

    @Test
    @DisplayName("Should handle non-existing elements gracefully")
    void testNonExistingElements() {
        assertFalse(network.union("Chai", "Unknown"));
        assertFalse(network.connected("Aimi", "Unknown"));
        assertNull(network.findRoot("Unknown"));
    }
}
