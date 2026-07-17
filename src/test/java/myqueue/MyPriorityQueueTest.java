package myqueue;

import mymodel.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyPriorityQueueTest {
    private MyPriorityQueue<Person> pq;

    @BeforeEach
    public void setUp() {
        pq = new MyPriorityQueue<>(8, (p1, p2) -> Integer.compare(p1.age, p2.age));
    }

    @Test
    @DisplayName("Should add elements in sorted order by comparator")
    void testEnqueue() {
        assertNull(pq.peek());

        pq.enqueue(new Person("Lamine", 18));
        assertEquals("Lamine", pq.peek().name);

        pq.enqueue(new Person("Nakamura", 24));
        assertEquals("Nakamura", pq.peek().name, "Nakamura is at the top since he is older");
    }

    @Test
    @DisplayName("Should pop top element from the queue")
    void testDequeue() {
        pq.enqueue(new Person("Lamine", 18));
        pq.enqueue(new Person("Nakamura", 24));
        pq.enqueue(new Person("Mbappe", 26));

        assertEquals("Mbappe", pq.peek().name);
        pq.dequeue();

        assertEquals("Nakamura", pq.peek().name);
        pq.dequeue();

        assertEquals("Lamine", pq.peek().name);
        pq.dequeue();

        assertNull(pq.peek());
    }

    @Test
    @DisplayName("Should recycle slots from removed elements")
    void testFreeSlotsRecycle() {
        pq.enqueue(new Person("Lamine", 18));
        pq.enqueue(new Person("Mbappe", 26));
        pq.enqueue(new Person("Nakamura", 24));

        pq.dequeue();

        assertDoesNotThrow(() -> pq.enqueue(new Person("Vozinha", 40)));

        pq.enqueue(new Person("Dembele", 27));
        pq.enqueue(new Person("Merino", 22));
        pq.enqueue(new Person("Sane", 30));
        pq.enqueue(new Person("Messi", 40));
        pq.enqueue(new Person("Son", 35));


        assertThrows(IllegalStateException.class, () -> pq.enqueue(new Person("Salah", 36)));
    }

    @Test
    @DisplayName("Should update an element and change its order based on new value")
    void testUpdate() {
        pq.enqueue(new Person("Dembele", 27));
        pq.enqueue(new Person("Merino", 22));
        pq.enqueue(new Person("Sane", 30));
        pq.enqueue(new Person("Messi", 40));
        pq.enqueue(new Person("Son", 35));

        assertEquals("Messi", pq.peek().name);

        pq.update(0, new Person("Neuer", 42));
        assertEquals("Neuer", pq.peek().name);

        pq.update(0, new Person("Neuer", 38));
        assertEquals("Messi", pq.peek().name);
    }

    @Test
    @DisplayName("Should remove an element by a given index")
    void testRemoveByIndex() {
        pq.enqueue(new Person("Dembele", 27));
        pq.enqueue(new Person("Sane", 30));
        pq.enqueue(new Person("Messi", 40));
        pq.enqueue(new Person("Son", 35));

        assertEquals("Messi", pq.peek().name);

        pq.remove(2);
        assertEquals("Son", pq.peek().name);

        pq.remove(3);
        assertEquals("Sane", pq.peek().name);
    }

    @Test
    @DisplayName("Should throw exceptions for invalid index and exceeding capacity")
    void testBoundaryExceptionGuards() {
        assertThrows(IllegalStateException.class, () -> pq.dequeue(), "Dequeue empty queue should fail");

        assertThrows(IllegalArgumentException.class, () -> pq.update(0, new Person("Xaka", 25)), "Update inactive index should fail");
        assertThrows(IndexOutOfBoundsException.class, () -> pq.remove(15), "Remove out of bound index should fail");

        pq.enqueue(new Person("Dembele", 27));
        pq.enqueue(new Person("Xaka", 25));
        pq.remove(1);

        assertThrows(IllegalArgumentException.class, () -> pq.update(1, new Person("Son", 36)), "Update a removed index should fail");
    }
}
