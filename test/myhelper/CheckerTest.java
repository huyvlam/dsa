package myhelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {
    private String nullS;
    private Integer nullI;
    private int size;

    @BeforeEach
    void setUp() {
        nullS = null;
        nullI = null;
        size = 10;
    }

    @Test
    @DisplayName("Should throw exception for null argument")
    void testCheckNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> Checker.checkNullArgument(nullS));
        assertThrows(IllegalArgumentException.class, () -> Checker.checkNullArgument(nullI));
    }

    @Test
    @DisplayName("Should throw exception for out of bound access")
    void testCheckOutOfBoundAccess() {
        assertThrows(IndexOutOfBoundsException.class, () -> Checker.checkBound(-1, size));
        assertThrows(IndexOutOfBoundsException.class, () -> Checker.checkBound(size, size));
    }
}