package myarray;

import myinterface.Printer;
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
        persons = new Person[2];
        persons[0] = new Person("Herra", 23);
        persons[1] = new Person("Rambo", 54);
    }

    @Test
    @DisplayName("Should fill the array with random numbers")
    void testFillRandomNumbers() {
        assertNull(numbers[0]);

        ArrayUtil.fillRandomNumbers(numbers, 20);

        assertNotNull(numbers[0]);
    }

    @Test
    @DisplayName("Should return string value from the given array")
    void testToString() {
        ArrayUtil.fillRandomNumbers(numbers, 20);
        String standard = ArrayUtil.toString(numbers);
        String custom = ArrayUtil.toString(persons, (p) -> p.name + " is " + p.age);

        assertTrue(standard.contains(numbers[2].toString()));
        assertTrue(custom.contains("Herra is 23"));
    }
}