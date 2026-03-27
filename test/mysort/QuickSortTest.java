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
    private Comparator<Person> customComp;

    @BeforeEach
    void setUp() {
        customComp = Comparator.comparing((p) -> p.age);

        arrS = new String[]{"soccer","archery",null,"rowing","baseball","volleyball","judo","sepak takraw"};
        arrP = new Person[]{
                null,
                new Person("Yuri", 34),
                new Person("June", 21),
                new Person("Ha", 56),
                new Person("Domo", 17),
                new Person("Timu", 48)
        };
    }

    @Test
    @DisplayName("Should sort elements using natural order")
    void testSortNaturalOrder() {
        QuickSort.sort(arrS, 0, arrS.length - 1, null);
        assertEquals("archery", arrS[0]);
        assertEquals("volleyball", arrS[arrS.length - 2]);
        assertNull(arrS[arrS.length - 1]);
    }

    @Test
    @DisplayName("Should sort elements using custom comparator")
    void testSortCustom() {
        QuickSort.sort(arrP, 0, arrP.length - 1, customComp);
        assertEquals("Domo", arrP[0].name);
        assertEquals("Ha", arrP[arrP.length - 2].name);
        assertNull(arrP[arrP.length - 1]);
    }
}