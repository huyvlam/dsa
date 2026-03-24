package mysearch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {
    private Pair<String> pair;

    @BeforeEach
    void setUp() {
        pair = new Pair<>();
    }

    @Test
    @DisplayName("Should contain field min/max")
    void testContainsField() {
        assertNull(pair.min);
        assertNull(pair.max);

        pair.min = "Least";
        pair.max = "Most";

        assertEquals("Least", pair.min);
        assertEquals("Most", pair.max);
    }
}