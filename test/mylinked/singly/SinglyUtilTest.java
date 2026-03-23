package mylinked.singly;

import myinterface.Printer;
import mymodel.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SinglyUtilTest {
    private SinglyNode<Person> p;
    private SinglyNode<String> s;
    private Printer<Person> printer;

    @BeforeEach
    void setUp() {
        s = new SinglyNode<>("Ari");
        p = new SinglyNode<>(new Person("Ari", 3));
        printer = (Person p) -> p.name + " is my " + p.age + " years old pup.";
    }

    @Test
    @DisplayName("Should use default printer in toString")
    void testToStringDefaultPrinter() {
        String result = SinglyUtil.toString(s, 1, null);
        assertTrue(result.contains("Ari"));
        assertTrue(result.contains("-> null"));
    }

    @Test
    @DisplayName("Should use custom printer in toString")
    void testToStringCustomPrinter() {
        String result = SinglyUtil.toString(p, 1, printer);
        assertTrue(result.contains("Ari is my 3 years old pup."));
        assertTrue(result.contains("-> null"));
    }
}