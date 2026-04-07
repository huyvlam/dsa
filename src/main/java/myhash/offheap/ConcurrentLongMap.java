package myhash.offheap;

import sun.misc.Unsafe;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ConcurrentLongMap {
    private ByteBuffer buffer;
    private static long baseAddress; // Absolute raw memory address (slot 0)
    private int capacity;
    private int mask;

    private static final int HEADER_SIZE = 64; // Bytes reserved for number of entries (size)
    private static final int ENTRY_SIZE = 16; // (K) 8 bytes + (V) 8 bytes
    private static final int SIZE_INDEX = 0; // We store size at this position
    private static final long EMPTY_SLOT = 0L;
    private static final long REMOVED_SLOT = -1L;
    private static final double LOAD_FACTOR = 0.6;

    private static final Unsafe unsafe; // Enable access of ByteBuffer raw memory address
    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ConcurrentLongMap(int capacity) {
        this.capacity = tableSize(capacity);
        mask = this.capacity - 1;
        buffer = ByteBuffer.allocateDirect(HEADER_SIZE + (this.capacity * ENTRY_SIZE))
                .order(ByteOrder.nativeOrder());
        updateBaseAddress(this.buffer);
    }

    // Call this after allocating DirectByteBuffer
    private void updateBaseAddress(ByteBuffer buffer) {
        try {
            // We use reflection to get the 'address' field inside the Buffer class
            Field addressField = Buffer.class.getDeclaredField("address");
            addressField.setAccessible(true);
            baseAddress = addressField.getLong(buffer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void put(long key, long value) {
        int index = hash(key) & mask;

        while (true) {
            // Calculate the raw memory address of the key
            long keyAddress = slotAddress(index);
            long tombstoneAddress = -1L;

            // Read the key currently at this address
            long curKey = unsafe.getLongVolatile(null, keyAddress);

            // Found a tombstone, save the key address for reuse
            if (curKey == REMOVED_SLOT && tombstoneAddress == -1L) {
                tombstoneAddress = keyAddress;
            }

            // Key found, update value
            if (curKey == key) {
                // `putOrderedLong` ensures other threads see the change immediately
                unsafe.putOrderedLong(null, keyAddress + 8, value);
                return;
            }

            // Found an empty slot
            if (curKey == EMPTY_SLOT) {
                long address = (tombstoneAddress != -1L) ? tombstoneAddress : keyAddress;
                long slot = (tombstoneAddress != -1L) ? REMOVED_SLOT : EMPTY_SLOT;                // Tombstone: Claim the removed slot by swapping w/ our key

                // We claim the slot by swapping w/ the key
                if (unsafe.compareAndSwapLong(null, address, slot, key)) {
                    unsafe.putOrderedLong(null, address + 8, value);
                    updateSize(1);
                    return;
                }
                // If CAS failed, another thread grabbed it. Loop again to check new key
            }

            index = linearProbe(index);
        }
    }

    // Tombstone
    public long remove(long key) {
        int index = hash(key) & mask;

        while (true) {
            long keyAddress = slotAddress(index);
            long curKey = unsafe.getLongVolatile(null, keyAddress);

            if (curKey == EMPTY_SLOT) return -1L;

            if (curKey == key) {
                if (unsafe.compareAndSwapLong(null, keyAddress, key, REMOVED_SLOT)) {
                    long removedValue = unsafe.getLongVolatile(null, keyAddress + 8);
                    unsafe.putOrderedLong(null, keyAddress + 8, REMOVED_SLOT);
                    updateSize(-1);
                    return removedValue;
                }
            }

            index = linearProbe(index);
        }
    }

    private void updateSize(int delta) {
        unsafe.getAndAddLong(null, slotAddress(SIZE_INDEX), delta);
    }

    public long size() {
        return unsafe.getLongVolatile(null, slotAddress(SIZE_INDEX));
    }

    private int linearProbe(int index) {
        return (index + 1) & mask;
    }

    private static long slotAddress(int index) {
        return baseAddress + HEADER_SIZE + (index * ENTRY_SIZE);
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
