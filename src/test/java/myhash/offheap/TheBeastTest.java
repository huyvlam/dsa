package myhash.offheap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TheBeastTest {
    TheBeast beast;
    static int capacity, newCap;

    @BeforeEach
    void setUp() {
        IO.println("Runtime Flags: " + java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments());
    }

    @Test
    void testQuietMoveSlot() {
        capacity = 2;
        beast = new TheBeast(capacity);
        TheBeast.Table newTab = new TheBeast.Table(capacity * 2);

        long[] keys = {32L, 64L};
        long value = 100L;

        beast.put(keys[0], value);

        // Case 1. Test empty slot move
        int emptyIndex = TheBeast.hash(keys[1]) & beast.table.mask;
        long emptySlot = TheBeast.slotAddress(emptyIndex, beast.table.baseAddress);

        // Verify the slot is empty
        assertEquals(TheBeast.EMPTY, TheBeast.unsafe.getLongVolatile(null, emptySlot));

        // Handle the empty slot during move
        TheBeast.Table.moveSlot(beast.table, newTab, emptyIndex);

        // Verify the empty slot is a moved sentinel
        assertEquals(TheBeast.unsafe.getLongVolatile(null, emptySlot), TheBeast.MOVED);

        // Verify the new slot is empty
        int newEmptyIndex = TheBeast.hash(keys[1]) & newTab.mask;
        long newEmptySlot = TheBeast.slotAddress(newEmptyIndex, newTab.baseAddress);
        assertEquals(TheBeast.EMPTY, TheBeast.unsafe.getLongVolatile(null, newEmptySlot));

        // Case 2. Test original data love
        int origIndex = TheBeast.hash(keys[0]) & beast.table.mask;
        long origSlot = TheBeast.slotAddress(origIndex, beast.table.baseAddress);

        // Verify the slot contains data
        assertEquals(beast.get(keys[0]), TheBeast.unsafe.getLongVolatile(null, origSlot + TheBeast.VALUE_OFFSET));

        // Move data from original table to new table
        TheBeast.Table.moveSlot(beast.table, newTab, origIndex);

        // Verify the original slot is a moved sentinel
        assertEquals(TheBeast.unsafe.getLongVolatile(null, origSlot), TheBeast.MOVED);

        // Verify the new slot contains migrated data
        int newIndex = TheBeast.hash(keys[0]) & newTab.mask;
        long newSlot = TheBeast.slotAddress(newIndex, newTab.baseAddress);
        assertEquals(value, TheBeast.unsafe.getLongVolatile(null, newSlot + TheBeast.VALUE_OFFSET));

        beast.clear();
        beast.destroy();
        newTab.clear();
        newTab.destroy();
    }

//    @Test
//    void verifyTransferIndexMath() {
//        // Force a key into the last slot (index 7)
//        long keyThatHashesToSeven = 7L; // Simplified for the test
//        beast.put(keyThatHashesToSeven, 12345L);
//
//        Table oldTab = beast.table;
//        Table newTab = new Table(ByteBuffer.allocateDirect(...), 16);
//
//        // Simulate one stride execution
//        int startIdx = oldTab.transferIndex.get(); // Should be 8
//        beast.moveSlot(startIdx - 1, oldTab, newTab); // Move index 7
//
//        // Assertions
//        long keyAtOld = unsafe.getLongVolatile(null, oldTab.baseAddress + HEADER_SIZE + (7 * ENTRY_SIZE));
//        assertEquals(MOVED_SENTINEL, keyAtOld, "Old slot should be marked MOVED");
//
//        assertEquals(12345L, beast.getFromTable(keyThatHashesToSeven, newTab), "Value should be in new table");
//    }
}