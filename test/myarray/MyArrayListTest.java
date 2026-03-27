package myarray;

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

        assertNotEquals(INITIAL_CAPACITY, arrList.size());
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
            IO.println("Field does not exist");
        } catch (IllegalAccessException e) {
            IO.println("Field cannot be accessed");
        }
    }

    @Test
    @DisplayName("Should throw exception if constructor capacity is negative")
    void testConstructorCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new MyArrayList<Integer>(-1));
    }
}