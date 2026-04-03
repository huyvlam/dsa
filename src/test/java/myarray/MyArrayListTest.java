package myarray;

import mymodel.Person;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

class MyArrayListTest {
    private MyArrayList<String> arrList;
    private final int INITIAL_CAPACITY = 1;

    @BeforeEach
    void setUp() {
        arrList = new MyArrayList<>(INITIAL_CAPACITY);
    }

    @Test
    @DisplayName("Should grow in capacity when exceeding initial capacity")
    void testGrowCapacity() {
        arrList.add("Sepak Takraw");
        arrList.add("Yachting");
        arrList.add("Boxing");

        assertTrue(INITIAL_CAPACITY < arrList.size());
        assertEquals("Boxing", arrList.get(2));
    }

    @Test
    @DisplayName("Should remove data and shift remaining elements to the left")
    void testRemoveByIndex() {
        arrList.add("Soccer");
        arrList.add("Archery");
        arrList.add("Swimming");

        assertEquals("Soccer", arrList.remove(0));
        assertEquals("Archery", arrList.get(0));
        assertEquals(2, arrList.size());
    }

    @Test
    @DisplayName("Should trim the capacity to the size of the list")
    void testTrimToSize() throws NoSuchFieldException, IllegalAccessException {
        arrList.add("Hockey");
        arrList.add("Badminton");
        arrList.add("Judo");

        assertEquals("Badminton", arrList.remove(1));

        arrList.trimToSize();

        assertEquals(2, arrList.size());

        try {
            Field field = MyArrayList.class.getDeclaredField("dataList");
            field.setAccessible(true);
            Object[] dataList = (Object[]) field.get(arrList);

            assertEquals(2, dataList.length);
        } catch (NoSuchFieldException e) {
            assertEquals(NoSuchFieldException.class, e);
        } catch (IllegalAccessException e) {
            assertEquals(IllegalAccessException.class, e);
        }
    }

    @Test
    @DisplayName("Should find the index of given data")
    void testIndexOf() {
        arrList.add("Judo");
        arrList.add("Rowing");
        arrList.add("Baseball");

        int i = 0;
        assertEquals(i, arrList.indexOf(arrList.get(i)));
        assertEquals(-1, arrList.indexOf("Tennis"));
    }

    @Test
    @DisplayName("Should sort elements using custom comparator or natural order")
    void testSort() {
        arrList.add("soccer");
        arrList.add("baseball");
        arrList.add("recurve");
        arrList.add("tennis");
        arrList.add("badminton");
        arrList.add("marathon");

        arrList.sort(null);

        assertEquals("badminton", arrList.get(0));
        assertEquals("baseball", arrList.get(1));
        assertEquals("tennis", arrList.get(arrList.size() - 1));
    }

    @Test
    @DisplayName("Should throw exception if constructor capacity is negative")
    void testConstructorException() {
        assertThrows(IllegalArgumentException.class, () -> new MyArrayList<Integer>(-1));
    }

    @Test
    @DisplayName("Should throw exception if no comparator is provided for sorting non-Comparable element")
    void testSortException() {
        MyArrayList<Person> listP = new MyArrayList<>(2);
        listP.add(new Person("X", 9));
        listP.add(new Person("Z", 6));

        assertThrows(IllegalArgumentException.class, () -> listP.sort(null));
    }
}