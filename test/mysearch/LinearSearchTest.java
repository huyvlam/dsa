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
    private Comparator<Integer> naturalComp;

    @BeforeEach
    void setUp() {
        naturalComp = Comparator.naturalOrder();
        customComp = Comparator.comparingInt((Person p) -> p.age);

        arrP = new Person[]{
                new Person("Herra",23),
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
        Pair<Integer> natural = LinearSearch.findMinMax(arrI, naturalComp);

        assertNotNull(natural);
        assertEquals(-5, natural.min);
        assertEquals(200, natural.max);

        Pair<Person> custom = LinearSearch.findMinMax(arrP, customComp);

        assertNotNull(custom);
        assertEquals(19, custom.min.age);
        assertEquals(54, custom.max.age);
    }

    @Test
    @DisplayName("Should find the index of given element in the array")
    void testFindIndex() {
        int i = 0;
        assertEquals(i, LinearSearch.findIndex(arrI, arrI[i], naturalComp));

        arrI = new Integer[]{1};
        assertEquals(-1, LinearSearch.findIndex(arrI, 2, naturalComp));

        assertEquals(2, LinearSearch.findIndex(arrP, new Person("X", 40), customComp));
    }
}