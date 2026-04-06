package myhash.offheap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LongLinearMap {
    private ByteBuffer buffer;
    private int capacity; // Must be power of 2
    private int mask; // Cache to avoid repeated computation
    private int size;

    private static final int ENTRY_SIZE = 16; // (K) 8 bytes + (V) 8 bytes
    private static final long EMPTY_SLOT = 0L;
    private static final double LOAD_FACTOR = 0.7;

    public LongLinearMap(int capacity) {
        this.capacity = tableSize(capacity);
        mask = this.capacity - 1;
        size = 0;
        buffer = ByteBuffer.allocateDirect(this.capacity * ENTRY_SIZE)
                .order(ByteOrder.nativeOrder()); // Use CPU's native byte order
    }

    public void resize() {
        int newCapacity = capacity * 2;
        int newMask = newCapacity - 1;
        ByteBuffer newBuffer = ByteBuffer.allocateDirect(newCapacity * ENTRY_SIZE)
                .order(ByteOrder.nativeOrder());

        for (int i = 0; i < capacity; i++) {
            int offset = i * ENTRY_SIZE;
            long key = buffer.getLong(offset);

            if (key != EMPTY_SLOT) {
                int newIndex = hash(key) & newMask;
                int newOffset = newIndex * ENTRY_SIZE;

                while (newBuffer.getLong(newOffset) != EMPTY_SLOT) {
                    newIndex = (newIndex + 1) & newMask;
                    newOffset = newIndex * ENTRY_SIZE;
                }

                newBuffer.putLong(newOffset, key);
                newBuffer.putLong(newOffset + 8, buffer.getLong(offset + 8));
            }
        }

        buffer = newBuffer;
        capacity = newCapacity;
        mask = newMask;
    }

    public void put(long key, long value) {
        if (key == EMPTY_SLOT) throw new IllegalArgumentException("Key cannot be 0");

        if ((size << 2) > (capacity * 3)) {
            resize();
        }

        int index = hash(key) & mask;
        int offset = index * ENTRY_SIZE;

        while (true) {
            long curKey = buffer.getLong(offset);

            if (curKey == key) {
                buffer.putLong(offset + 8, value);
                return;
            }

            if (curKey == EMPTY_SLOT) {
                buffer.putLong(offset, key);
                buffer.putLong(offset + 8, value);
                size++;
                return;
            }

            index = (index + 1) & mask;
            offset = index * ENTRY_SIZE;
        }
    }

    public long get(long key) {
        int index = hash(key) & mask;
        int offset = index * ENTRY_SIZE;

        while (true) {
            long curKey = buffer.getLong(offset);

            if (curKey == EMPTY_SLOT) return -1L;
            if (curKey == key) return buffer.getLong(offset + 8);

            index = (index + 1) & mask;
            offset = index * ENTRY_SIZE;
        }
    }

    public long remove(long key) {
        int index = hash(key) & mask;
        int offset = index * ENTRY_SIZE;
        long curKey;

        while (true) {
            curKey = buffer.getLong(offset);

            if (curKey == EMPTY_SLOT) return -1L;
            if (curKey == key) break;

            index = (index + 1) & mask;
            offset = index * ENTRY_SIZE;
        }

        // Backshifting
        long removedValue = buffer.getLong(offset + 8);
        int vacant = index;

        while (true) {
            // Check the next slot to see if it can move forward
            index = (index + 1) & mask;
            offset = index * ENTRY_SIZE;
            curKey = buffer.getLong(offset);

            // Next slot if empty, stop searching
            if (curKey == EMPTY_SLOT) break;

            // This index is where the current key likes to be
            int ideal = hash(curKey) & mask;

            // If the vacant slot is NOT between current index and ideal, move the current key into vacant slot
            if (!isBetween(vacant, index, ideal)) {
                int vacantOffset = vacant * ENTRY_SIZE;
                buffer.putLong(vacantOffset, curKey);
                buffer.putLong(vacantOffset + 8, buffer.getLong(offset + 8));
                vacant = index;
            }
        }

        // Finally set vacant slot to empty
        offset = vacant * ENTRY_SIZE;
        buffer.putLong(offset, EMPTY_SLOT);
        buffer.putLong(offset + 8, EMPTY_SLOT);
        size--;

        return removedValue;
    }

    // Cyclical check if ideal lies between vacant and current spot
    private boolean isBetween(int vacant, int cur, int ideal) {
        if (vacant <= cur) {
            return vacant < ideal && ideal <= cur;
        } else {
            return vacant < ideal || ideal <= cur;
        }
    }

    private int hash(long key) {
        // Mixer for longs ensuring even distribution
        key ^= key >>> 33;
        key *= 0xff51afd7ed558ccdL;
        key ^= key >>> 33;
        return (int) key;
    }

    private int tableSize(int n) {
        int cap = 1;
        while (cap < n) cap <<= 1;
        return cap;
    }
}