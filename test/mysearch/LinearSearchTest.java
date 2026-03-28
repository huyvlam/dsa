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
    private Comparator<Person> customComp;

    @BeforeEach
    void setUp() {
        customComp = Comparator.comparing((Person p) -> p.name);

        arrP = new Person[]{
                new Person("Hera",23),
                new Person("Rumi",54),
                new Person("Oro",40),
                new Person("Niko",19)};

        arrI = new Integer[15];
        ArrayUtil.fillRandomNumbers(arrI, 100);
        arrI[5] = -5;
        arrI[13] = 200;
    }

    @Test
    @DisplayName("Should find min/max elements in the given array")
    void testFindMinMax() {
        Pair<Integer> natural = LinearSearch.findMinMax(arrI, null);

        assertNotNull(natural);
        assertEquals(-5, natural.min);
        assertEquals(200, natural.max);

        Pair<Person> custom = LinearSearch.findMinMax(arrP, customComp);

        assertNotNull(custom);
        assertEquals("Hera", custom.min.name);
        assertEquals("Rumi", custom.max.name);
    }

    @Test
    @DisplayName("Should find the index of given element in the array")
    void testFindIndex() {
        int i = 0;
        assertEquals(i, LinearSearch.findIndex(arrI, arrI[i], null));

        arrI = new Integer[]{1};
        assertEquals(-1, LinearSearch.findIndex(arrI, 2, null));

        assertEquals(2, LinearSearch.findIndex(arrP, new Person("Oro", 35), customComp));
    }
}