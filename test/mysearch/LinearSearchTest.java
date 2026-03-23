package mysearch;

import mymodel.Person;
import myarray.ArrayUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;


class LinearSearchTest {
    private Integer[] listI;
    private Person[] listP;
    private Comparator<Person> comparator;

    @BeforeEach
    void setUp() {
        comparator = Comparator.comparingInt((Person p) -> p.age);
        listI = new Integer[20];
        ArrayUtil.fillRandomNumbers(listI, 100);
        listI[5] = -5;
        listI[15] = 200;
        listP = new Person[4];
        listP[0] = new Person("Herra", 23);
        listP[1] = new Person("Rumi", 54);
        listP[2] = new Person("Oro", 40);
        listP[3] = new Person("Niko", 19);
    }

    @Test
    @DisplayName("Should find min/max elements in the given array using default comparator")
    void testMinMaxDefaultComparator() {
        Object[] res = LinearSearch.findMinMax(listI);

        assertEquals(-5, res[0]);
        assertEquals(200, res[1]);
    }

    @Test
    @DisplayName("Should find min/max elements in the given array using custom comparator")
    void testMinMaxCustomComparator() {
        Object[] res = LinearSearch.findMinMax(listP, comparator);

        assertNotNull(res);
        assertEquals(19, ((Person) res[0]).age);
        assertEquals(54, ((Person) res[1]).age);
    }
}