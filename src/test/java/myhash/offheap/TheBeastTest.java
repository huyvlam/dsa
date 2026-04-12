package myhash.offheap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Should move slot from old table to new table")
    void testMoveSlot() {
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

        // Case 2. Test slot filled with original data
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

        beast.destroy();
        newTab.destroy();
    }

    @Test
    @DisplayName("Should return the starting position for the range of transfer slots")
    void verifyTransferIndexMath() {
        int cap = 16;
        TheBeast.Table oldTab = new TheBeast.Table(cap);
        TheBeast.Table newTab = new TheBeast.Table(cap * 2);

        long[] keys = {
                10L,  // Bottom boundary
                7L,  // Middle
                15L  // Top boundary
        };
        for (long k : keys) {
            TheBeast.Table.insert(oldTab, k, k + 100);
        }

        // Mocking the transfer loop
        int stride = 4;
        while (true) {
            int currentIdx = oldTab.transferIndex.get();
            if (currentIdx <= 0) break;

            int nextIdx = Math.max(0, currentIdx - stride);
            if (oldTab.transferIndex.compareAndSet(currentIdx, nextIdx)) {
                for (int i = currentIdx - 1; i >= nextIdx; i--) {
                    TheBeast.Table.moveSlot(oldTab, newTab, i);
                }
            }
        }

        for (long k : keys) {
            // Verify old table is sealed
            int idx = TheBeast.hash(k) & oldTab.mask;
            assertEquals(TheBeast.MOVED, TheBeast.unsafe.getLongVolatile(null, TheBeast.slotAddress(idx, oldTab.baseAddress)));

            // Verify data is in new table
            long foundVal = -1;
            int probeIdx = TheBeast.hash(k) & newTab.mask;
            while (true) {
                long probeAddress = TheBeast.slotAddress(probeIdx, newTab.baseAddress);
                long probeKey = TheBeast.unsafe.getLongVolatile(null, probeAddress);

                if (probeKey == k) {
                    foundVal = TheBeast.unsafe.getLongVolatile(null, probeAddress + TheBeast.VALUE_OFFSET);
                    break;
                }

                if (probeKey == TheBeast.EMPTY) break;

                probeIdx = (probeIdx + 1) & newTab.mask;
            }
            assertEquals(k + 100, foundVal, "Value not found for key: " + k);
        }

        assertEquals(0, oldTab.transferIndex.get(), "Transfer index should be exactly 0");
    }
}