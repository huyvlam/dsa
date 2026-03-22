package myheap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import mymodel.Person;

import java.util.Comparator;

class VirtualBinaryHeapTest {
    private final Person[] personsA = { new Person("Zizi", 23), new Person("Yan", 31) };
    private final Person[] personsB = { new Person("Herra", 14), new Person("Tin", 54) };
    private final String[] strA = {"Xuan", "Ha"};
    private final String[] strB = {"Thu", "Dong"};
    private Comparator<Person> comparator;

    @BeforeEach
    void setUp() {
        comparator = (p1, p2) -> Integer.compare(p1.age, p2.age);
    }

    @Test
    @DisplayName("Should rearrange the two arrays into min-heap")
    void testMergeMinHeap() {
        VirtualBinaryHeap.merge(personsA, personsB, VirtualBinaryHeap.HeapOrder.MIN, comparator);

        assertEquals(14, personsA[0].age);
    }

    @Test
    @DisplayName("Should rearrange the two arrays into max-heap")
    void testMergeMaxHeap() {
        VirtualBinaryHeap.merge(personsA, personsB, VirtualBinaryHeap.HeapOrder.MAX, comparator);

        assertEquals(54, personsA[0].age);
    }

    @Test
    @DisplayName("Should sort the two arrays using natural order of elements")
    void testSortStandard() {
        VirtualBinaryHeap.sort(strA, strB);

        assertEquals("Dong", strA[0]);
        assertEquals("Ha", strA[1]);
        assertEquals("Thu", strB[0]);
        assertEquals("Xuan", strB[1]);
    }

    @Test
    @DisplayName("Should sort the two arrays in ascending order using custom comparator")
    void testSortCustomAscending() {
        VirtualBinaryHeap.sort(personsA, personsB, VirtualBinaryHeap.SortOrder.ASC, comparator);

        assertEquals(14, personsA[0].age);
        assertEquals(23, personsA[1].age);
        assertEquals(31, personsB[0].age);
        assertEquals(54, personsB[1].age);
    }

    @Test
    @DisplayName("Should sort the two arrays in descending order using custom comparator")
    void testSortCustomDescending() {
        VirtualBinaryHeap.sort(personsA, personsB, VirtualBinaryHeap.SortOrder.DESC, comparator);

        assertEquals(54, personsA[0].age);
        assertEquals(31, personsA[1].age);
        assertEquals(23, personsB[0].age);
        assertEquals(14, personsB[1].age);
    }
}