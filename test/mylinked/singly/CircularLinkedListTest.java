package mylinked.singly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircularLinkedListTest {
    private CircularLinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = new CircularLinkedList<>(null);
    }

    @Test
    @DisplayName("Should add at both ends and maintain head/tail")
    void testAddFirstLast() {
        list.addLast("Kiwi");
        list.addFirst("Mangosteen");
        list.addLast("Passionfruit");

        assertEquals("Mangosteen", list.peekFirst());
        assertEquals("Passionfruit", list.peekLast());
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("Should handle pollFirst, pollLast on single element")
    void testPollSingleElement() {
        list.addFirst("Apple");
        list.addLast("Cherry");

        assertEquals("Apple", list.pollFirst());
        assertEquals("Cherry", list.pollLast());
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Should add/remove element by the given index")
    void testAddRemoveByIndex() {
        list.addFirst("Peach");
        list.addLast("Apricot");
        list.add(1, "Nectarine");

        assertEquals(1, list.indexOf("Nectarine"));
        assertEquals("Apricot", list.remove(2));
        assertEquals("Peach", list.remove(0));
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Should remove by the given data")
    void testRemoveByData() {
        list.addFirst("Lychee");
        list.addLast("Longan");
        list.add(1, "Rambutan");

        assertFalse(list.remove("Apple"));
        assertTrue(list.remove("Rambutan"));
        assertTrue(list.remove("Lychee"));
        assertTrue(list.remove("Longan"));
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Should get/replace data by the specified index")
    void testSetGetByIndex() {
        list.addFirst("Strawberry");
        list.addLast("Pluot");
        list.add(1, "Plum");

        assertEquals("Strawberry", list.get(0));
        assertEquals("Plum", list.get(1));
        assertEquals("Pluot", list.get(2));

        list.set(2, "Pear");
        assertEquals("Pear", list.get(2));
    }

    @Test
    @DisplayName("Should throw exception for out of bounds access")
    void testBounds() {
        list.addFirst("Dragon fruit");

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, null));
    }

    @Test
    @DisplayName("Should reverse the list and swap head/tail pointers")
    void testReverse() {
        list.addFirst("Tangerine");
        list.addFirst("Orange");
        list.addFirst("Kiwi");
        list.addFirst("Mango");

        list.reverse();

        assertEquals("Tangerine", list.peekFirst());
        assertEquals("Orange", list.get(1));
        assertEquals("Kiwi", list.get(2));
        assertEquals("Mango", list.peekLast());
    }

    @Test
    @DisplayName("Should use custom/default printer in toString")
    void testToString() {
        list.addFirst("Soursop");
        String result = list.toString(null);
        String customResult = list.toString(p -> ("I like " + p));

        assertTrue(result.contains("[Soursop]"));
        assertTrue(result.contains("-> (head)"));
        assertTrue(customResult.contains("I like Soursop"));
    }
}