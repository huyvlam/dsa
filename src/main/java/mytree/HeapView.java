package mytree;

import myheap.HeapUtil;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public abstract class HeapView implements AutoCloseable {
    ArrayBinaryTree abt;
    int expectedModCount;

    HeapView(ArrayBinaryTree abt) {
        this.abt = abt;
        expectedModCount = abt.modCount;
    }

    int peek() {
        checkModCount();
        return abt.tree[0];
    }

    abstract int pop();

    void insert(int value) {
        checkModCount();
        abt.insert(value);
        expectedModCount = abt.modCount;
    }

    void checkModCount() {
        if (abt.modCount != expectedModCount)
            throw new ConcurrentModificationException("Tree mutated outside Heap view");
    }

    @Override
    public void close() {
        if (abt != null) {
            abt.clearView();
            this.abt = null;
        }
    }
}

class MaxHeapView extends HeapView {
    MaxHeapView(ArrayBinaryTree abt) {
        super(abt);
    }

    int pop() {
        checkModCount();
        if (abt.size == 0) {
            abt.activeMaxView = null;
            throw new NoSuchElementException();
        }

        int top = abt.tree[0];

        abt.tree[0] = abt.tree[abt.size - 1];
        abt.tree[abt.size - 1] = 0;
        abt.size--;

        if (abt.size > 0) {
            HeapUtil.sinkMax(abt.tree, 0, abt.size);
        } else {
            abt.activeMaxView = null;
        }

        expectedModCount = ++abt.modCount;
        return top;
    }
}

class MinHeapView extends HeapView {
    MinHeapView(ArrayBinaryTree abt) {
        super(abt);
    }

    int pop() {
        checkModCount();
        if (abt.size == 0) {
            abt.activeMinView = null;
            throw new NoSuchElementException();
        }

        int top = abt.tree[0];

        abt.tree[0] = abt.tree[abt.size - 1];
        abt.tree[abt.size - 1] = 0;
        abt.size--;

        if (abt.size > 0) {
            HeapUtil.sinkMin(abt.tree, 0, abt.size);
        } else {
            abt.activeMinView = null;
        }

        expectedModCount = ++abt.modCount;
        return top;
    }
}