package mysort;

import mymodel.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class QuickSortTest {
    private String[] arrS;
    private Person[] arrP;
    private Comparator<Person> comp;

    @BeforeEach
    void setUp() {
        comp = Comparator.comparing((p) -> p.age);

        arrS = new String[]{"soccer","archery",null,"rowing","baseball","volleyball","judo","sepak takraw"};
        arrP = new Person[]{
                new Person("Yuri", 34),
                null,
                new Person("June", 21),
                new Person("Ha", 56),
                new Person("Domo", 17),
                new Person("Timu", 48)
        };
    }

    @Test
    @DisplayName("Should sort Comparable elements using natural order")
    void testSortStandard() {
        int n = arrS.length;
        QuickSort.sort(arrS, 0, n - 1, null);
        assertEquals("archery", arrS[0]);
        assertEquals("volleyball", arrS[n - 2]);
    }

    @Test
    @DisplayName("Should move null to the end of array")
    void testNullsLast() {
        int n = arrS.length;
        QuickSort.sort(arrS, 0, n - 1, null);
        assertNull(arrS[n - 1]);

        int m = arrP.length;
        QuickSort.sort(arrP, 0, m - 1, comp);
        assertNull(arrP[m - 1]);
    }

    @Test
    @DisplayName("Should sort generic elements using custom comparator")
    void testSortCustom() {
        int n = arrP.length;
        QuickSort.sort(arrP, 0, n - 1, comp);
        assertEquals("Domo", arrP[0].name);
        assertEquals("Ha", arrP[n - 2].name);
        assertNull(arrP[arrP.length - 1]);
    }
}