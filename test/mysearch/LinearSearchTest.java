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

    @BeforeEach
    void setUp() {
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
    @DisplayName("Should find min/max elements in the given array using natural order")
    void testMinMaxNaturalOrder() {
        Pair<Integer> res = LinearSearch.findMinMax(arrI, Comparator.naturalOrder());

        assertNotNull(res);
        assertEquals(-5, res.min);
        assertEquals(200, res.max);
    }

    @Test
    @DisplayName("Should find min/max elements in the given array using custom comparator")
    void testMinMaxCustomComparator() {
        Pair<Person> res = LinearSearch.findMinMax(arrP, Comparator.comparingInt((Person p) -> p.age));

        assertNotNull(res);
        assertEquals(19, res.min.age);
        assertEquals(54, res.max.age);
    }
}