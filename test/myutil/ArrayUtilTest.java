package myhelper;

import myinterface.Printer;
import mymodel.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayHelperTest {
    private final Person[] persons = {new Person("Herra", 23), new Person("Rambo", 54)};
    private final Integer[] nums = new Integer[5];
    private Printer<Person> printer;

    @BeforeEach
    void setUp() {
        printer = (p) -> p.name + " is " + p.age;
    }

    @Test
    @DisplayName("Should fill the array with random numbers")
    void testFillRandomNumbers() {
        assertNull(nums[0]);

        ArrayHelper.fillRandomNumbers(nums, 20);

        assertNotNull(nums[0]);
    }

    @Test
    @DisplayName("Should return string value from the given array using custom printer")
    void testToString() {
        String res = ArrayHelper.toString(persons, printer);

        assertTrue(res.contains("Herra is 23"));
    }
}