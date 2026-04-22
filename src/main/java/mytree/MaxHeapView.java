package mytree;

import myheap.HeapUtil;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class MaxHeapView implements AutoCloseable {
    private ArrayBinaryTree abt;
    private int expectedModCount;

    MaxHeapView(ArrayBinaryTree abt) {
        this.abt = abt;
        expectedModCount = abt.modCount;
    }

    int peek() {
        checkModCount();
        return abt.tree[0];
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
            HeapUtil.maxSink(abt.tree, 0, abt.size);
        } else {
            abt.activeMaxView = null;
        }

        expectedModCount = ++abt.modCount;
        return top;
    }

    void insert(int value) {
        checkModCount();
        abt.insert(value);
        expectedModCount = abt.modCount;
    }

    private void checkModCount() {
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
