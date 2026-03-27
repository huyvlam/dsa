package myarray;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

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

        assertNotEquals(INITIAL_CAPACITY, arrList.capacity());
        assertEquals(3, arrList.size());
    }

    @Test
    @DisplayName("Should remove data and move remaining elements to the left")
    void testRemoveByIndex() {
        arrList.add("Soccer");
        arrList.add("Archery");
        arrList.add("Swimming");

        arrList.remove(0);

        assertEquals("Archery", arrList.get(0));
    }
}