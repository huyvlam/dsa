package myarray;

import mymodel.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayUtilTest {
    private Person[] persons;
    private Integer[] numbers;

    @BeforeEach
    void setUp() {
        numbers = new Integer[5];
        persons = new Person[]{new Person("Herra",23),new Person("Rambo",54)};
    }

    @Test
    @DisplayName("Should fill the array with random numbers")
    void testFillRandomNumbers() {
        assertNull(numbers[0]);

        ArrayUtil.fillRandomNumbers(numbers, 20);

        assertNotNull(numbers[0]);
    }

    @Test
    @DisplayName("Should toString array elements using custom printer")
    void testToStringCustomPrint() {
        String custom = ArrayUtil.toString(persons, (p) -> p.name + " is " + p.age);

        assertTrue(custom.contains("Herra is 23"));
    }
}