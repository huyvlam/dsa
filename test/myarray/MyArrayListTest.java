package myarray;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MyArrayListTest {
    private MyArrayList<String> al;
    private final int INITIAL_CAPACITY = 2;

    @BeforeEach
    void setUp() {
        al = new MyArrayList<>(INITIAL_CAPACITY);
    }

    @Test
    @DisplayName("Should grow in capacity when exceeding initial capacity")
    void testGrowCapacity() {
        al.add("Sepak Takraw");
        al.add("Yachting");
        al.add("Boxing");

        assertNotEquals(INITIAL_CAPACITY, al.capacity());
        assertEquals(3, al.size());
    }
}