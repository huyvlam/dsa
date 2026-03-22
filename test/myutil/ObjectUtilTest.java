package myutil;

import mymodel.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectUtilTest {
    private Person p1, p2;

    @BeforeEach
    void setUp() {
        p1 = new Person("Zizi", 35);
        p2 = new Person("Han", 48);
    }

    @Test
    @DisplayName("Should compare the given object w/ target element")
    void testCompatible() {
        Object obj = new Object();

        assertTrue(ObjectUtil.isCompatible(p1, p2));
        assertFalse(ObjectUtil.isCompatible(obj, p2));
    }
}