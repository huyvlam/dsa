package mysearch;

import mymodel.Person;
import myarray.ArrayUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;


class LinearSearchTest {
    private Integer[] arrI;
    private Person[] arrP;
    private Comparator<Person> comparator;

    @BeforeEach
    void setUp() {
        comparator = (Person p1, Person p2) -> Integer.compare(p1.age, p2.age);
        arrI = new Integer[20];
        ArrayUtil.fillRandomNumbers(arrI, 100);
        arrI[5] = -5;
        arrI[15] = 200;
        arrP = new Person[4];
        arrP[0] = new Person("Herra", 23);
        arrP[1] = new Person("Rumi", 54);
        arrP[2] = new Person("Oro", 40);
        arrP[3] = new Person("Niko", 19);
    }

    @Test
    @DisplayName("Should find the smallest/largest element in the given array")
    void testFindMinMax() {
        Object[] res = LinearSearch.findMinMax(arrI);

        assertEquals(-5, res[0]);
        assertEquals(200, res[1]);
    }

    @Test
    @DisplayName("Should find the smallest/largest element in the given array using custom comparator")
    void testFindMinMaxWithComparator() {
        Object[] res = LinearSearch.findMinMax(arrP, comparator);

        assertNotNull(res);
        assertEquals(19, ((Person) res[0]).age);
        assertEquals(54, ((Person) res[1]).age);
    }
}