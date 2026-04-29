package mytree;

import myheap.BinaryHeapUtil;

import java.util.ConcurrentModificationException;

public abstract class HeapView implements AutoCloseable {
    ArrayBinaryTree abt;
    protected int expectedModCount;

    HeapView(ArrayBinaryTree abt) {
        this.abt = abt;
        expectedModCount = abt.modCount;
    }

    abstract void sink(int n);
    abstract void build(int n);

    void insert(int value) {
        checkModCount();

        abt.insert(value);
        expectedModCount = abt.modCount;
    }

    int peek() {
        checkModCount();
        return abt.tree[0];
    }

    int pop() {
        checkModCount();

        int top = abt.tree[0];
        abt.tree[0] = abt.tree[abt.size - 1];
        abt.tree[abt.size - 1] = 0;
        abt.size--;
        expectedModCount = ++abt.modCount;

        if (abt.size > 1) {
            sink(abt.size);
        } else if (abt.size == 0) {
            close();
        }

        return top;
    }

    int replace(int value) {
        checkModCount();

        int top = abt.tree[0];
        abt.tree[0] = value;

        if (abt.size > 1) {
            sink(abt.size);
        }

        expectedModCount = ++abt.modCount;
        return top;
    }

    void sort() {
        checkModCount();

        for (int i = abt.size - 1; i > 0; i--) {
            int temp = abt.tree[0];
            abt.tree[0] = abt.tree[i];
            abt.tree[i] = temp;

            sink(i);
        }
        expectedModCount = ++abt.modCount;

        // The heap now becomes a sorted list
        // We close the view to prevent further operations
        close();
    }

    public String toString() {
        int level = 0;
        int nodes = 1; // number of nodes in current level
        int prints = 0; // number of nodes printed in current level
        StringBuilder sb = new StringBuilder();
        sb.append("Heap View (size ").append(abt.size).append("):\n");

        for (int i = 0; i < abt.size; i++) {
            if (prints == 0) {
                sb.append("Level ").append(level).append(" [");
            }

            sb.append(String.format("%4d", abt.tree[i]));
            prints++;

            if (prints == nodes) {
                sb.append("]\n");
                prints = 0;
                nodes <<= 1;
                level++;
            }
        }

        // In case the last level was not filled up with max number of nodes
        if (prints > 0 && prints < nodes) sb.append("]\n");

        return sb.toString().trim();
    }

    void checkModCount() {
        if (abt == null) {
            throw new IllegalStateException("View is released and no longer valid.");
        }
        if (abt.modCount != expectedModCount) {
            throw new ConcurrentModificationException("Tree mutated outside Heap view");
        }
    }

    void sync() {
        if (abt == null) {
            throw new IllegalStateException("View is released and no longer valid.");
        }

        build(abt.size);
        expectedModCount = ++abt.modCount;
    }

    @Override
    public void close() {
        if (abt == null) return;

        abt.clearView();
        this.abt = null;
    }
}

class MaxHeapView extends HeapView {
    MaxHeapView(ArrayBinaryTree abt) {
        super(abt);
    }

    void sink(int n) {
        BinaryHeapUtil.sinkMax(abt.tree, 0, n);
    }

    void build(int n) {
        BinaryHeapUtil.buildMax(abt.tree, n);
    }
}

class MinHeapView extends HeapView {
    MinHeapView(ArrayBinaryTree abt) {
        super(abt);
    }

    void sink(int n) {
        BinaryHeapUtil.sinkMin(abt.tree, 0, n);
    }

    void build(int n) {
        BinaryHeapUtil.buildMin(abt.tree, n);
    }
}
