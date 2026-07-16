/*
 * Priority Queue
 * Use binary heap as the engine for maintaining order
 * Use registry list as actual storage for entries
 * Use inverse map to bridge between heap and registry
 * Use free slots to keep track of released spaces
 */

package myqueue;

import myheap.IndexComparator;
import myheap.MaxBinaryHeap;
import myheap.PositionTracker;

import java.util.Arrays;
import java.util.Comparator;

public class MyPriorityQueue<E extends Comparable<? super E>> {
    private final MaxBinaryHeap heap;
    private final E[] registry;
    private final int[] inverseMap;
    private final int[] freeSlots;
    private final Comparator<? super E> comparator;

    private final IndexComparator indexComparator;
    private final PositionTracker positionTracker;

    private int size;
    private int freeSlotIndex = -1;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int capacity, Comparator<? super E> comparator) {
        heap = new MaxBinaryHeap(capacity);
        registry = (E[]) new Object[capacity];
        inverseMap = new int[capacity];
        freeSlots = new int[capacity];
        size = 0;
        this.comparator = comparator;

        indexComparator = (indexA, indexB) -> this.comparator.compare(registry[indexA], registry[indexB]);
        positionTracker = ((elementIndex, heapPosition) -> inverseMap[elementIndex] = heapPosition);

        Arrays.fill(inverseMap, -1);
    }

    public MyPriorityQueue(int capacity) {
        this(capacity, Comparator.naturalOrder());
    }

    public void enqueue(E element) {
        if (size >= registry.length) {
            throw new IllegalStateException("Queue is full");
        }

        int newIndex = (freeSlotIndex >= 0) ? freeSlots[freeSlotIndex--] : size++;
        registry[newIndex] = element;

        heap.insert(newIndex, indexComparator, positionTracker);
    }

    public void dequeue() {
        if (size <= 0) {
            throw new IllegalStateException("Queue is empty");
        }

        int removedIndex = heap.pop(indexComparator, positionTracker);

        registry[removedIndex] = null;
        inverseMap[removedIndex] = -1;

        freeSlots[++freeSlotIndex] = removedIndex;
        size--;
    }

    public void update(int index, E newElement) {
        if (index < 0 || index >= registry.length || inverseMap[index] == -1) {
            throw new IndexOutOfBoundsException("Element not found at index: " + index);
        }

        registry[index] = newElement;

        heap.changeKey(inverseMap[index], indexComparator, positionTracker);
    }

    public void remove(int index) {
        if (index < 0 || index >= registry.length || inverseMap[index] == -1) {
            throw new IndexOutOfBoundsException("Element not found at index: " + index);
        }

        heap.deleteKey(inverseMap[index], indexComparator, positionTracker);

        registry[index] = null;
        inverseMap[index] = -1;
        freeSlots[++freeSlotIndex] = index;
        size--;
    }

    public E peek() {
        int topIndex = heap.peek();

        if (topIndex >= 0) {
            return (E) registry[topIndex];
        }

        return null;
    }
}
