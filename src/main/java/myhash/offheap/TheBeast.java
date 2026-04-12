package myhash.offheap;

import sun.misc.Unsafe;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicInteger;

public class TheBeast {
    static final class Table {
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

        Table(int capacity) {
            this.capacity = capacity;
            this.mask = capacity - 1;
            this.transferIndex = new AtomicInteger(this.capacity);
            this.activeTransferThreads = new AtomicInteger(0);

            // Create bytes buffer for storing data
            this.buffer = ByteBuffer.allocateDirect(bufferSize(this.capacity))
                    .order(ByteOrder.nativeOrder());

            // Get the raw memory address
            this.baseAddress = directBufferAddress(buffer);

            // Safety Check: Ensure we aren't zeroing memory at address 0
            if (baseAddress == 0) {
                throw new IllegalStateException("Buffer address is 0. Is the buffer direct?");
            }

            // 3. Zero Fill: Use 'null' for absolute address and cast to long for safety
            unsafe.setMemory(null, baseAddress, bufferSize(this.capacity), (byte) 0);
        }

        void clear() {
            // Zero out the size in the header
            unsafe.putLongVolatile(null, baseAddress + SIZE_OFFSET, 0L);

            // 2. Zero out all Key/Value slots
            long dataStart = baseAddress + HEADER_SIZE;
            long dataLength = (long) capacity * ENTRY_SIZE;

            // setMemory is MUCH faster for clearing all data in a range
            unsafe.setMemory(dataStart, dataLength, (byte) 0);
        }

        // Manual Memory Management: close out everything
        void destroy() {
            // Manually free the memory
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

        static void moveSlot(Table curTab, Table newTab, int index) {
            long curAddress = slotAddress(index, curTab.baseAddress);

            while (true) {
                long curKey = unsafe.getLongVolatile(null, curAddress);

                // Seal the empty/tombstone slot before other threads grab it
                if (curKey == EMPTY || curKey == REMOVED) {
                    if (unsafe.compareAndSwapLong(null, curAddress, curKey, MOVED)) return;
                    continue; // Retry if CAS failed
                }

                // Slot already moved
                if (curKey == MOVED) return;

                // 1. Grab the current data
                long val = unsafe.getLongVolatile(null, curAddress + VALUE_OFFSET);

                // 2. Move the current data into new table
                insert(newTab, curKey, val);

                // 3. Seal the current slot as MOVED
                if (unsafe.compareAndSwapLong(null, curAddress, curKey, MOVED)) {
                    return; // CAS successful, DONE
                }
                // CAS fail, loop again to retry
            }
        }

        static void insert(Table tab, long key, long value) {
            int index = hash(key) & tab.mask;

            while (true) {
                long keyAddress = slotAddress(index, tab.baseAddress);
                long curKey = unsafe.getLongVolatile(null, keyAddress);

                if (curKey == EMPTY) {
                    if (unsafe.compareAndSwapLong(null, keyAddress, EMPTY, key)) {
                        unsafe.putLongVolatile(null, keyAddress + VALUE_OFFSET, value);
                        return;
                    }
                } else if (curKey == key) {
                    // This should not happen, but we still have it for safety
                    unsafe.putOrderedLong(null, keyAddress + VALUE_OFFSET, value);
                    return;
                }

                index = (index + 1) & tab.mask;
            }
        }

        static int bufferSize(int capacity) {
            return HEADER_SIZE + (capacity * ENTRY_SIZE);
        }

        static long directBufferAddress(ByteBuffer buffer) {
            return unsafe.getLong(buffer, ADDRESS_OFFSET);
        }
    }

    static final Unsafe unsafe; // Enable access of ByteBuffer raw memory address
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

    volatile Table table; // Current buffer and newly created buffer for resize

    static final int HEADER_SIZE = 64; // Store number of entries count (size)
    static final int ENTRY_SIZE = 16; // (K) 8 bytes + (V) 8 bytes
    static final int VALUE_OFFSET = 8;
    static final int SIZE_OFFSET = 0; // Slot where size is stored
    static final long EMPTY = 0L;
    static final long REMOVED = -1L;
    static final long MOVED = Long.MAX_VALUE;
    private static final double LOAD_FACTOR = 0.6;
    private static final int TRANSFER_STRIDE = 64; // Number of slots to transfer. Use 64 to minimize contention on 'transferIndex'
    private static final int MAX_CAPACITY = 1 << 30;

    public TheBeast(int capacity) {
        this.table = new Table(tableSize(capacity));
    }

    // Clean up buffer stored data
    public void clear() {
        this.table.clear();
    }

    // Manual Memory Management: close out everything
    public void destroy() {
        this.table.destroy();
    }

    public long get(long key) {
        if (key == EMPTY || key == REMOVED) return -1L;

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
                return unsafe.getLongVolatile(null, keyAddress + VALUE_OFFSET);
            }

            index = (index + 1) & curTab.mask;
        }
    }

    public void put(long key, long value) {
        if (key == EMPTY || key == REMOVED) throw new IllegalArgumentException("Illegal key: " + key);

        Table curTab = this.table;
        final int hash = hash(key);

        while (true) {
            if (size() >= curTab.capacity * LOAD_FACTOR) {
                resize();
                curTab = this.table;
            }

            int index = hash & curTab.mask;
            long tombstoneAddress = -1L;

            while (true) {
                long keyAddress = slotAddress(index, curTab.baseAddress);
                long curKey = unsafe.getLongVolatile(null, keyAddress);

                // Key is being moved to new buffer
                if (curKey == MOVED) {
                    transfer(curTab); // Help with the resize
                    curTab = this.table; // Switch to new table
                    break; // Stop inner loop to start over on new table
                }

                // Key found, update value
                if (curKey == key) {
                    // Ensures the change is seen immediately
                    unsafe.putLongVolatile(null, keyAddress + VALUE_OFFSET, value);
                    return;
                }

                // Found a tombstone, save the address for reuse
                if (curKey == REMOVED && tombstoneAddress == -1L) {
                    tombstoneAddress = keyAddress;
                }

                // Found an empty slot, insert
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
                        unsafe.putLongVolatile(null, address + VALUE_OFFSET, value);
                        updateSize(1);
                        return;
                    }

                    // If CAS fails, restart on this index to see what is put there.
                    break;
                }

                index = (index + 1) & curTab.mask;
            }
        }
    }

    private void resize() {
        Table curTab = this.table;

        if (curTab.capacity == MAX_CAPACITY) return;

        // If a resize already occurs, move to transfer
        if (curTab.next != null) {
            transfer(curTab);
            return;
        }

        Table newTab = new Table(curTab.capacity * 2);
        // If we can't claim the current table
        if (!unsafe.compareAndSwapObject(curTab, tableNextOffset, null, newTab)) {
            newTab.destroy(); // Destroy the new table and move to transfer
        };

        transfer(curTab);
    }

    private void transfer(Table curTab) {
        Table newTab = curTab.next;
        if (newTab == null) return; // Resize has not started yet

        int oldCap = curTab.capacity;
        int stride = (oldCap > TRANSFER_STRIDE) ? TRANSFER_STRIDE : oldCap;

        // Register as an active helper
        curTab.activeTransferThreads.incrementAndGet();

        try {
            int end;

            // 1. Grab a stride of 64 slots at a time to move
            // Threads count from capacity down to zero
            while ((end = curTab.transferIndex.get()) > 0) {
                int start = Math.max(0, end - stride);

                // 2. Claim the range we will work on
                if (curTab.transferIndex.compareAndSet(end, start)) {
                    // 3. Start moving the slots (right to left)
                    for (int i = end - 1; i >= start; i--) {
                        Table.moveSlot(curTab, newTab, i);
                    }
                }
            }
        } finally {
            // 4. Update pointer and clean up old table memory
            if (curTab.activeTransferThreads.decrementAndGet() == 0) {
                // Capture the size of current table
                long size = unsafe.getLongVolatile(null, curTab.baseAddress + SIZE_OFFSET);
                // Update the new table with the captured size
                unsafe.getAndAddLong(null, newTab.baseAddress + SIZE_OFFSET, size);

                // Set the global pointer to new table
                this.table = newTab;
            }
        }
    }

    // Tombstone
    public long remove(long key) {
        if (key == EMPTY || key == REMOVED) return -1L;

        Table curTab = this.table;
        final int hash = hash(key);

        while (true) {
            int index = hash & curTab.mask;

            while (true) {
                long keyAddress = slotAddress(index, curTab.baseAddress);
                long curKey = unsafe.getLongVolatile(null, keyAddress);

                if (curKey == EMPTY) return -1L;

                if (curKey == key) {
                    long removedValue = unsafe.getLongVolatile(null, keyAddress + VALUE_OFFSET);

                    if (unsafe.compareAndSwapLong(null, keyAddress, key, REMOVED)) {
                        unsafe.putLongVolatile(null, keyAddress + VALUE_OFFSET, EMPTY);
                        updateSize(-1);
                        return removedValue;
                    }
                    // CAS fail, rehash on new table
                    break;
                }

                if (curKey == MOVED) {
                    transfer(curTab);
                    curTab = this.table;
                    break; // Rehash on new table
                }

                index = (index + 1) & curTab.mask;
            }
        }
    }

    public long size() {
        Table curTab = this.table;
        Table nextTab = curTab.next;

        // Get size of current table
        long size = unsafe.getLongVolatile(null, curTab.baseAddress + SIZE_OFFSET);

        // If a resize is happening, combine size of both current and new table
        if (nextTab != null) {
            size += unsafe.getLongVolatile(null, nextTab.baseAddress + SIZE_OFFSET);
        }

        return size;
    }

    private void updateSize(int delta) {
        // Always update to the 'latest' table
        Table curTab = (table.next != null) ? table.next : table;
        unsafe.getAndAddLong(null, curTab.baseAddress + SIZE_OFFSET, (long) delta);
    }

    // Compute the raw memory address of ByteBuffer
    static long slotAddress(int index, long base) {
        return base + HEADER_SIZE + (index * (long) ENTRY_SIZE);
    }

    static int hash(long key) {
        // Mixer for longs ensuring even distribution
        key ^= key >>> 33;
        key *= 0xff51afd7ed558ccdL;
        key ^= key >>> 33;
        return (int) key;
    }

    private static int tableSize(int n) {
        int cap = n - 1;
        cap |= cap >>> 1;
        cap |= cap >>> 2;
        cap |= cap >>> 4;
        cap |= cap >>> 8;
        cap |= cap >>> 16;
        return (cap < 0) ? 1 : (cap >= MAX_CAPACITY) ? MAX_CAPACITY : cap + 1;
    }
}

