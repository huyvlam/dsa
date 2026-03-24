package mylinked.singly;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import mymodel.Person;

class MyLinkedListTest {
    private MyLinkedList<Person> list;

    @BeforeEach
    void setUp() {
        // Initialize with an age-based comparator
        list = new MyLinkedList<>((p1, p2) -> Integer.compare(p1.age, p2.age));
    }

    @Test
    @DisplayName("Should add at both ends and maintain head/tail")
    void testAddFirstLast() {
        list.addFirst(new Person("Bob", 30));   // [30]
        list.addLast(new Person("Charlie", 40)); // [30, 40]
        list.addFirst(new Person("Alice", 20));  // [20, 30, 40]

        assertEquals(3, list.size());
        assertEquals(20, list.peekFirst().age);
        assertEquals(40, list.peekLast().age);
    }

    @Test
    @DisplayName("Should handle pollFirst and pollLast on single element")
    void testPollSingleElement() {
        list.addFirst(new Person("Olly", 10));
        list.addLast(new Person("Atlas", 12));

        assertEquals(10, list.pollFirst().age);
        assertEquals(12, list.pollLast().age);
        assertEquals(0, list.size());
        assertNull(list.peekFirst());
        assertNull(list.peekLast()); // Ensures tail was reset!
    }

    @Test
    @DisplayName("Should reverse the list and swap head/tail pointers")
    void testReverse() {
        list.addLast(new Person("A", 10));
        list.addLast(new Person("B", 20));
        list.addLast(new Person("C", 30));
        list.addLast(new Person("D", 40));

        list.reverse();

        assertEquals(40, list.peekFirst().age); // New Head
        assertEquals(30, list.get(1).age);  // New Tail
        assertEquals(20, list.get(2).age);      // Middle stayed
        assertEquals(10, list.peekLast().age);      // Middle stayed
    }

    @Test
    @DisplayName("Should add/remove data by the given index")
    void testAddRemoveByIndex() {
        list.add(0, new Person("Abby", 20));
        list.add(1, new Person("Bona", 22));
        list.add(1, new Person("Ciro", 24));

        assertEquals(0, list.indexOf(new Person("A", 20)));
        assertEquals(1, list.indexOf(new Person("B", 24)));
        assertEquals("Bona", list.remove(2).name);
        assertEquals(2, list.size());
        assertFalse(list.contains(new Person("Ciro", 22)));
    }

    @Test
    @DisplayName("Should remove by data using the comparator")
    void testRemoveByData() {
        list.addFirst(new Person("Head", 50));
        list.addLast(new Person("Tail", 25));
        list.add(1, new Person("Mid", 15));

        assertTrue(list.remove(new Person("Ghost", 15))); // match by age
        assertFalse(list.remove(new Person("Ghost", 15))); // match by age
        assertTrue(list.remove(new Person("Ghost", 25))); // match by age
        assertTrue(list.remove(new Person("Ghost", 50))); // match by age
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Should replace/get data by the specified index")
    void testSetGetByIndex() {
        list.addFirst(new Person("Alice", 20));

        assertTrue(list.contains(new Person("Alice", 20)));

        list.set(0, new Person("Target", 25));

        assertEquals(25, list.get(0).age);
    }

    @Test
    @DisplayName("Should throw exception for out of bounds access")
    void testBounds() {
        list.addFirst(new Person("Alice", 20));

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, null));
    }

    @Test
    @DisplayName("Should use custom printer in toString")
    void testToStringCustomPrinter() {
        list.addFirst(new Person("Alice", 20));
        String result = list.toString(p -> p.name + " is " + p.age);

        assertTrue(result.contains("Alice is 20"));
        assertTrue(result.contains("-> null"));
    }
}
