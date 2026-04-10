package myhash.offheap;

import sun.misc.Unsafe;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicInteger;

public class TheBeast {
    private static class Table {
        private static final long ADDRESS_OFFSET;
        static {
            try {
                // Requires --add-opens=java.base/java.nio=ALL-UNNAMED
                Field addressField = java.nio.Buffer.class.getDeclaredField("address");
                addressField.setAccessible(true);
                ADDRESS_OFFSET = unsafe.objectFieldOffset(addressField);
            } catch (Exception e) {
                throw new Error("The Beast cannot see into java.nio. Check your --add-opens flags.", e);
            }
        }

        final ByteBuffer buffer;
        final int capacity;
        final int mask;
        final long baseAddress;
        volatile Table next;

        final AtomicInteger transferIndex;
        final AtomicInteger activeTransferThreads;

        Table(ByteBuffer buffer, int capacity) {
            this.buffer = buffer;
            this.capacity = capacity;
            this.mask = capacity - 1;
            this.transferIndex = new AtomicInteger(capacity);
            this.activeTransferThreads = new AtomicInteger(0);

            // 1. Get the raw memory address
            this.baseAddress = unsafe.getLong(buffer, ADDRESS_OFFSET);

            // 2. Safety Check: Ensure we aren't zeroing memory at address 0
            if (baseAddress == 0) {
                throw new IllegalStateException("Buffer address is 0. Is the buffer direct?");
            }

            // 3. ZERO FILL: Use 'null' for absolute address and cast to long for safety
            long totalSize = HEADER_SIZE + ((long) capacity * ENTRY_SIZE);
            unsafe.setMemory(null, baseAddress, totalSize, (byte) 0);
        }

        static void moveSlot(Table curTab, Table newTab, int index) {
            long curAddress = slotAddress(index, curTab.baseAddress);

            while (true) {
                long key = unsafe.getLongVolatile(null, curAddress);

                // Already moved by another thread
                if (key == MOVED) return;

                // Seal the empty slot before other threads grab it
                if (key == EMPTY) {
                    if (unsafe.compareAndSwapLong(null, curAddress, EMPTY, MOVED)) return;
                }

                // Seal the tombstone before others grab it
                if (key == REMOVED) {
                    if (unsafe.compareAndSwapLong(null, curAddress, REMOVED, MOVED)) return;
                }

                // Mark the current slot as MOVED before others grab it
                if (unsafe.compareAndSwapLong(null, curAddress, key, MOVED)) {
                    // Grab the actual current data
                    long val = unsafe.getLongVolatile(null, curAddress + 8);
                    // Move the current data into new table
                    insert(newTab, key, val);
                    return;
                }
            }
        }

        static void insert(Table tab, long key, long value) {
            int index = hash(key) & tab.mask;

            while (true) {
                long keyAddress = slotAddress(index, tab.baseAddress);
                long curKey = unsafe.getLongVolatile(null, keyAddress);

                if (curKey == EMPTY) {
                    if (unsafe.compareAndSwapLong(null, keyAddress, EMPTY, key)) {
                        unsafe.putOrderedLong(null, keyAddress + 8, value);
                        return;
                    }
                } else if (curKey == key) {
                    // Though this should not happen, we still have it for safety
                    unsafe.putOrderedLong(null, keyAddress + 8, value);
                    return;
                }

                index = (index + 1) & tab.mask;
            }
        }
    }

    private static final Unsafe unsafe; // Enable access of ByteBuffer raw memory address
    private static final long tableNextOffset;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            tableNextOffset = unsafe.objectFieldOffset(Table.class.getDeclaredField("next"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private volatile Table table; // Holds current buffer and newly created buffer during resize

    private static final int HEADER_SIZE = 64; // Storage reserved for counting number of entries (size)
    private static final int ENTRY_SIZE = 16; // (K) 8 bytes + (V) 8 bytes
    private static final int SIZE_INDEX = 0; // The position where size is stored
    private static final long EMPTY = 0L;
    private static final long REMOVED = -1L;
    private static final long MOVED = Long.MAX_VALUE;
    private static final double LOAD_FACTOR = 0.6;

    public TheBeast(int capacity) {
        int tabSize = tableSize(capacity);
        ByteBuffer buffer = ByteBuffer.allocateDirect(HEADER_SIZE + (tabSize * ENTRY_SIZE))
                .order(ByteOrder.nativeOrder());
        this.table = new Table(buffer, tabSize);
    }

    // Clean up buffer stored data
    public void clear() {
        Table curTab = this.table;

        // Zero out the size in the header
        unsafe.putLongVolatile(null, curTab.baseAddress + SIZE_INDEX, 0L);

        // 2. Zero out all Key/Value slots
        long dataStart = curTab.baseAddress + HEADER_SIZE;
        long dataLength = (long) curTab.capacity * ENTRY_SIZE;

        // setMemory is MUCH faster by clearing all data within a range
        unsafe.setMemory(dataStart, dataLength, (byte) 0);
    }

    // Manual Memory Management
    public void destroy() {
        Table tab = this.table;

        // Manually free the memory
        freeBuffer(tab.buffer);
    }

    private static void freeBuffer(ByteBuffer buffer) {
        if (buffer instanceof sun.nio.ch.DirectBuffer) {
            try {
                // JDK 25 safe approach
                ((sun.nio.ch.DirectBuffer) buffer).cleaner().clean();
            } catch (Exception e) {
                // If the above fails due to module restrictions, use this fallback:
                try {
                    java.lang.reflect.Method cleanerMethod = buffer.getClass().getMethod("cleaner");
                    cleanerMethod.setAccessible(true);
                    Object cleaner = cleanerMethod.invoke(buffer);
                    java.lang.reflect.Method cleanMethod = cleaner.getClass().getMethod("clean");
                    cleanMethod.invoke(cleaner);
                } catch (Exception ex) {
                    throw new Error(ex);
                }
            }
        }
    }

    public long get(long key) {
        if (key == EMPTY || key == REMOVED) throw new IllegalArgumentException("Illegal key: " + key);

        Table curTab = this.table;
        final int hash = hash(key);
        int index = hash & curTab.mask;

        while (true) {
            long keyAddress = slotAddress(index, curTab.baseAddress);
            long curKey = unsafe.getLongVolatile(null, keyAddress);

            if (curKey == EMPTY) return -1L;

            // Data is moving
            if (curKey == MOVED) {
                Table nextTab = curTab.next;

                if (nextTab != null) {
                    // Double check to ensure the new table exists before using it
                    curTab = nextTab;
                } else {
                    // The resize might finish already, get the updated table
                    curTab = this.table;
                }

                // Start the loop over w/ the new/updated table
                index = hash & curTab.mask;
                continue;
            }

            if (curKey == key) {
                return unsafe.getLongVolatile(null, keyAddress + 8);
            }

            index = (index + 1) & curTab.mask;
        }
    }

    public void put(long key, long value) {
        if (key == EMPTY || key == REMOVED) throw new IllegalArgumentException("Illegal key: " + key);

        Table curTab = this.table;
        final int hash = hash(key);

        if (size() >= curTab.capacity * LOAD_FACTOR) {
            resize();
            curTab = this.table;
        }

        int index = hash & curTab.mask;
        long tombstoneAddress = -1L;

        while (true) {
            // Buffer raw memory address of this key
            long keyAddress = slotAddress(index, curTab.baseAddress);
            // Read the key currently at this address
            long curKey = unsafe.getLongVolatile(null, keyAddress);

            // Key is being moved to new buffer
            if (curKey == MOVED) {
                helpTransfer(curTab); // Help with the resize
                curTab = this.table; // Switch to new table
                index = hash & curTab.mask; // Rehash using new table mask
                tombstoneAddress = -1L; // Reset the tombstone since it is now the new table
                continue; // Start the loop over
            }

            // Key found, update value
            if (curKey == key) {
                // `putOrderedLong` ensures other threads see the change immediately
                unsafe.putOrderedLong(null, keyAddress + 8, value);
                return;
            }

            // Found a tombstone, save the address for reuse
            if (curKey == REMOVED && tombstoneAddress == -1L) {
                tombstoneAddress = keyAddress;
            }

            // Found an empty slot
            if (curKey == EMPTY) {
                long address, slot;

                if (tombstoneAddress != -1L) {
                    // We have a tombstone, use it
                    address = tombstoneAddress;
                    slot = REMOVED;
                } else {
                    address = keyAddress;
                    slot = EMPTY;
                }

                // We claim the slot by swapping w/ the key
                if (unsafe.compareAndSwapLong(null, address, slot, key)) {
                    // `putOrderedLong` handles CAS failure by starting a new probe
                    unsafe.putOrderedLong(null, address + 8, value);
                    updateSize(1);
                    return;
                }

                // If CAS fails, other threads took the slot.
                // We must stay on this index to see what is put there.
                continue;
            }

            index = (index + 1) & curTab.mask;
        }
    }

    private void resize() {
        Table curTab = this.table;

        // Ensure only ONE thread creates the new table
        if (curTab.next == null) {
            int newCapacity = curTab.capacity * 2;
            ByteBuffer newBuffer = ByteBuffer.allocateDirect(HEADER_SIZE + (newCapacity * ENTRY_SIZE))
                    .order(ByteOrder.nativeOrder());
            Table newTab = new Table(newBuffer, newCapacity);

            // Claim the current table for resizing
            // If other threads took it already then move to help transfer
            unsafe.compareAndSwapObject(curTab, tableNextOffset, null, newTab);
        }

        // Every threads help move data to new table
        helpTransfer(curTab);
    }

    private void helpTransfer(Table curTab) {
        Table newTab = curTab.next;

        // Resize has not started yet
        if (newTab == null) return;

        // Register as an active helper
        curTab.activeTransferThreads.incrementAndGet();

        try {
            // Move 64 slots at a time to reduce contention on the counter
            int stride = 64;

            while (true) {
                // 1. Grab a chunk of 64 slots to work on
                // Threads count from capacity down to zero
                int end = curTab.transferIndex.getAndAdd(-stride);

                // No more slot to move
                if (end <= 0) break;

                int start = Math.max(0, end - stride);

                // 2. Start moving the slots we grabbed
                // Iterate right to left
                for (int i = end - 1; i >= start; i--) {
                    Table.moveSlot(curTab, newTab, i);
                }
            }
        } finally {
            // 3. Last one out turns off the lights
            if (curTab.activeTransferThreads.decrementAndGet() == 0) {
                // Capture the size of current table
                long curSize = unsafe.getLongVolatile(null, curTab.baseAddress + SIZE_INDEX);

                // Update the new table with current table size
                unsafe.getAndAddLong(null, newTab.baseAddress + SIZE_INDEX, curSize);

                // Swap the new table w/ current
                this.table = newTab;

                // Zero out the size in old table to prevent double counting
                unsafe.putOrderedLong(null, curTab.baseAddress + SIZE_INDEX, 0L);

                // Unlink the the old table from new one
                curTab.next = null;
            }
        }
    }

    // Tombstone
    public long remove(long key) {
        if (key == EMPTY || key == REMOVED) throw new IllegalArgumentException("Illegal key: " + key);

        Table curTab = this.table;
        int index = hash(key) & curTab.mask;

        while (true) {
            long keyAddress = slotAddress(index, curTab.baseAddress);
            long curKey = unsafe.getLongVolatile(null, keyAddress);

            if (curKey == EMPTY) return -1L;

            if (curKey == key) {
                if (unsafe.compareAndSwapLong(null, keyAddress, key, REMOVED)) {
                    long removedValue = unsafe.getLongVolatile(null, keyAddress + 8);
                    unsafe.putOrderedLong(null, keyAddress + 8, REMOVED);
                    updateSize(-1);
                    return removedValue;
                }
            }

            index = (index + 1) & curTab.mask;
        }
    }

    public long size() {
        Table curTab = this.table;
        Table nextTab = curTab.next;

        // Get size of current table
        long size = unsafe.getLongVolatile(null, curTab.baseAddress + SIZE_INDEX);

        // If a resize is happening, combine size of both current and new table
        if (nextTab != null) {
            size += unsafe.getLongVolatile(null, nextTab.baseAddress + SIZE_INDEX);
        }

        return size;
    }

    private void updateSize(int delta) {
        // Always update to the 'latest' table
        Table curTab = (table.next != null) ? table.next : table;
        unsafe.getAndAddLong(null, curTab.baseAddress + SIZE_INDEX, (long) delta);
    }

    // Compute the raw memory address of ByteBuffer
    private static long slotAddress(int index, long base) {
        return base + HEADER_SIZE + (index * (long) ENTRY_SIZE);
    }

    private static int hash(long key) {
        // Mixer for longs ensuring even distribution
        key ^= key >>> 33;
        key *= 0xff51afd7ed558ccdL;
        key ^= key >>> 33;
        return (int) key;
    }

    private static int tableSize(int n) {
        int cap = 1;
        while (cap < n) cap <<= 1;
        return cap;
    }
}

