package myhelper;

import mymodel.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {
    @Test
    @DisplayName("Should throw exception for null argument")
    void testCheckNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> Checker.checkNullArgument(null));
    }

    @Test
    @DisplayName("Should throw exception for out of bound access")
    void testCheckOutOfBoundAccess() {
        assertThrows(IndexOutOfBoundsException.class, () -> Checker.checkBounds(-1, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> Checker.checkBounds(10, 10));
    }

    @Test
    @DisplayName("Should throw exception for change in modification count")
    void testCheckModCount() {
        assertThrows(ConcurrentModificationException.class, () -> Checker.checkModCount(2, 3));
    }

    @Test
    @DisplayName("Should return false if array elements are not Comparable")
    void testComparable() {
        Person[] arrP = new Person[]{new Person("A", 25)};
        assertFalse(Checker.isComparable(arrP, 1));

        Integer[] arrI = new Integer[]{1};
        assertTrue(Checker.isComparable(arrI, 1));
    }
}