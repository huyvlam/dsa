package mylinked.singly;

import mymodel.Person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyCircularLinkedListTest {
    private MyCircularLinkedList<String> listS;
    private MyCircularLinkedList<Person> listP;

    @BeforeEach
    void setUp() {
        listS = new MyCircularLinkedList<>();
        listP = new MyCircularLinkedList<>();
    }

    @Test
    @DisplayName("Should add at both ends and maintain head/tail")
    void testAddFirstLast() {
        listS.addLast("Kiwi");
        listS.addFirst("Mangosteen");
        listS.addLast("Passionfruit");
        listS.addFirst("Lime");

        assertEquals("Lime", listS.peekFirst());
        assertEquals("Passionfruit", listS.peekLast());
        assertEquals(4, listS.size());
    }

    @Test
    @DisplayName("Should handle pollFirst, pollLast on single element")
    void testPollSingleElement() {
        listS.addLast("Cherry");
        listS.addFirst("Apple");
        listS.addLast("Berry");

        assertEquals("Apple", listS.pollFirst());
        assertEquals("Berry", listS.pollLast());
        assertEquals("Cherry", listS.pollLast());
        assertEquals(0, listS.size());
    }

    @Test
    @DisplayName("Should add/remove element by the given index")
    void testAddRemoveByIndex() {
        listS.add(0, "Nectarine");
        listS.add(1, "Peach");
        listS.add(1, "Apricot");
        listS.add(1, "Plum");

        assertEquals("Nectarine", listS.peekFirst());
        assertEquals("Plum", listS.get(1));
        assertEquals("Apricot", listS.get(2));
        assertEquals("Peach", listS.peekLast());

        assertEquals("Apricot", listS.remove(2));
        assertEquals("Plum", listS.remove(1));
        assertEquals("Peach", listS.remove(1));
        assertEquals("Nectarine", listS.remove(0));
        assertEquals(0, listS.size());
    }

    @Test
    @DisplayName("Should remove by the given data")
    void testRemoveByData() {
        listS.addFirst("Lychee");
        listS.addLast("Longan");
        listS.add(1, "Rambutan");

        assertFalse(listS.remove("Apple"));
        assertTrue(listS.remove("Rambutan"));
        assertTrue(listS.remove("Lychee"));
        assertEquals(1, listS.size());
    }

    @Test
    @DisplayName("Should get/replace data by the specified index")
    void testSetGetByIndex() {
        listS.addFirst("Strawberry");
        listS.addLast("Pluot");
        listS.add(1, "Plum");

        assertEquals("Strawberry", listS.get(0));
        assertEquals("Plum", listS.get(1));
        assertEquals("Pluot", listS.get(2));

        assertEquals("Pluot", listS.set(2, "Pear"));
        assertEquals("Pear", listS.get(2));
    }

    @Test
    @DisplayName("Should throw exception for out of bounds access and invalid argument")
    void testExceptions() {
        listS.addFirst("Dragon fruit");

        assertThrows(IndexOutOfBoundsException.class, () -> listS.get(5));
        assertThrows(IndexOutOfBoundsException.class, () -> listS.set(-1, null));
        assertThrows(IllegalArgumentException.class, () -> listS.indexOf("Lime", null));
    }

    @Test
    @DisplayName("Should reverse the list and swap head/tail pointers")
    void testReverse() {
        listS.addFirst("Tangerine");
        listS.addFirst("Orange");
        listS.addFirst("Kiwi");
        listS.addFirst("Mango");

        listS.reverse();

        assertEquals("Tangerine", listS.peekFirst());
        assertEquals("Orange", listS.get(1));
        assertEquals("Kiwi", listS.get(2));
        assertEquals("Mango", listS.peekLast());
    }

//    @Test
//    @DisplayName("Should use custom/default printer in toString")
//    void testToString() {
//        listS.addFirst("Soursop");
//        String standard = listS.toString();
//        String custom = listS.toString(p -> ("I like " + p));
//
//        assertTrue(standard.contains("[Soursop] -> (head)"));
//        assertTrue(custom.contains("I like Soursop"));
//    }
}