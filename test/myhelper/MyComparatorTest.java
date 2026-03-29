package myhelper;

import mymodel.Person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class MyComparatorTest {
    private Integer[] arrI;
    private Person[] arrP;

    @BeforeEach
    void setup() {
        arrI = new Integer[]{null,3,1,5};
        arrP = new Person[]{new Person("A", 25)};
    }

    @Test
    @DisplayName("Should return equals comparison for generics or natural order for Comparables")
    void testEqualsComparator() {
        Comparator comp = MyComparator.equalsComparator;

        assertTrue(comp.compare(arrI[1], 3) == 0);

        assertTrue(comp.compare(arrP[0], arrP[0]) == 0);
        assertFalse(comp.compare(arrP[0], new Person("A", 25)) == 0);
    }

    @Test
    @DisplayName("Should move null elements to last")
    void testNullsLastComparator() {
        assertThrows(NullPointerException.class, () -> Arrays.sort(arrI));
        assertDoesNotThrow(() -> Arrays.sort(arrP, MyComparator.nullsLastComparator(null)));
    }

    @Test
    @DisplayName("Should return false if array elements are not Comparable")
    void testIsComparable() {
        assertFalse(MyComparator.isComparable(arrP, arrP.length));
        assertTrue(MyComparator.isComparable(arrI, 2));
    }
}