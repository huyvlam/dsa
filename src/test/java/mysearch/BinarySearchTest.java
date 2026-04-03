package mysearch;

import mymodel.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Comparator;

class BinarySearchTest {
    private String[] arrS;
    private Person[] arrP;

    @BeforeEach
    void setUp() {
        arrS = new String[]{"Xuan","Ha","Thu","Dong","Tet"};
        arrP = new Person[]{new Person("Xuan",15), new Person("Ha",13), new Person("Thu",23), new Person("Dong",18)};
    }

    @Test
    @DisplayName("Should find the index of given element within specified range of the sorted array")
    void testSearchNaturalOrder() {
        Arrays.sort(arrS);

        int i = 3;
        assertEquals(-1, BinarySearch.findIndex(arrS, arrS[i], 0, i - 1, null));
        assertEquals(i, BinarySearch.findIndex(arrS, arrS[i], 0, 4, null));
    }

    @Test
    @DisplayName("Should find index of given element in the sorted array using custom comparator")
    void testSearchCustomComparator() {
        Comparator<Person> comp = Comparator.comparing((Person p) -> p.age);
        Arrays.sort(arrP, comp);

        int i = 1;
        assertEquals(-1, BinarySearch.findIndex(arrP, arrP[i], 0, i - 1, comp));
        assertEquals(i, BinarySearch.findIndex(arrP, arrP[i], 0, 3, comp));
    }
}