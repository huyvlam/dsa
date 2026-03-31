package mylinked.singly;

import mymodel.Person;

import java.util.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SinglyCircularLinkedListTest {
    private SinglyCircularLinkedList<String> listS;
    private SinglyCircularLinkedList<Person> listP;

    @BeforeEach
    void setUp() {
        listS = new SinglyCircularLinkedList<>();
        listP = new SinglyCircularLinkedList<>();
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
    @DisplayName("Should remove Comparable element by data using natural order")
    void testRemoveComparableByData() {
        listS.addFirst("Lychee");
        listS.addLast("Longan");
        listS.add(1, "Rambutan");

        assertFalse(listS.remove("Apple"));
        assertTrue(listS.remove("Rambutan"));
        assertTrue(listS.remove("Lychee"));
        assertEquals(1, listS.size());
    }

    @Test
    @DisplayName("Should remove generic element by data using equals comparison or custom comparator")
    void testRemoveGenericByData() {
        Person mai = new Person("Mai", 42);
        listP.add(0, mai);
        assertTrue(listP.remove(mai));

        listP.add(0, new Person("Chi", 19));
        assertFalse(listP.remove(new Person("Chi", 19)));

        assertTrue(listP.remove(new Person("Chi", 19),
                (p1, p2) -> (Objects.equals(p1.age, p2.age) && Objects.equals(p1.name, p2.name) ? 0 : -1)));
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
    @DisplayName("Should throw exception for out of bounds access")
    void testExceptions() {
        listS.addFirst("Dragon fruit");

        assertThrows(IndexOutOfBoundsException.class, () -> listS.get(5));
        assertThrows(IndexOutOfBoundsException.class, () -> listS.set(-1, null));
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

    @Test
    @DisplayName("Should iterate through all elements in the list")
    void testIterator() {
        listS.addFirst("Tangerine");
        listS.addFirst("Orange");
        listS.addFirst("Kiwi");

        Iterator<String> it = listS.iterator();

        assertTrue(it.hasNext());
        assertThrows(IllegalStateException.class, it::remove);
        assertEquals("Kiwi", it.next());

        it.remove();
        assertEquals(2, listS.size());
        assertEquals("Orange", listS.peekFirst());

        it.next();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);

        listS.pollLast();
        assertThrows(ConcurrentModificationException.class, it::remove);
    }

    @Test
    @DisplayName("Should print using custom/standard printer in toString")
    void testToString() {
        listS.addFirst("Soursop");
        String standard = listS.toString();
        String custom = listS.toString(p -> ("I like " + p));

        assertTrue(standard.contains("[Soursop] -> (head)"));
        assertTrue(custom.contains("I like Soursop"));
    }
}