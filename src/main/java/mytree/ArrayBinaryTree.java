package mytree;

import java.util.Random;

public class ArrayBinaryTree {
    private int[] tree;
    private int initCapacity;
    private int size;
    private int maxPathSum;
    private boolean computed;
    public ArrayBinaryTree(int capacity) {
        if (capacity < 2) throw new IllegalArgumentException("Capacity must be 2 or greater");

        initCapacity = capacity;
        tree = new int[this.initCapacity];
        size = 0;
        computed = false;
    }

    public void clear() {
        tree = new int[this.initCapacity];
        size = 0;
        computed = false;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        if (size == 0) return true;
        // If next element start a new level, then tree is full
        return (size & (size + 1)) == 0;
    }

    public int size() {
        return size;
    }

    public int height() {
        return (size == 0) ? -1 : 31 - Integer.numberOfLeadingZeros(size);
    }

    public int capacity() {
        return tree.length;
    }

    public void ensureCapacity(int capacity) {
        grow(capacity);
    }

    private void grow() {
        grow(tree.length << 1);
    }

    private void grow(int capacity) {
        if (capacity <= tree.length) return;

        int[] newTree = new int[capacity];
        System.arraycopy(tree, 0, newTree, 0, size);
        tree = newTree;
    }

    public void trimToSize() {
        if (size < tree.length) {
            int newCap = size > initCapacity ? size : initCapacity;
            int[] newTree = new int[newCap];

            System.arraycopy(tree, 0, newTree, 0, size);
            tree = newTree;
        }
    }

    public void insert(int value) {
        if (size == tree.length) grow();
        tree[size++] = value;
        computed = false;
    }

    public void delete(int value) {
        int i = findIndex(value);
        if (i == -1) return;

        if (tree[i] == value) {
            tree[i] = tree[size - 1];
            tree[size - 1] = 0;
            size--;
            computed = false;
        }
    }

    public int getMaxPathSum() {
        if (!computed) {
            maxPathSum = getMaxPathSum(0);
            computed = true;
        }
        return maxPathSum;
    }

    public int getMaxPathSum(int i) {
        int[] res = {Integer.MIN_VALUE};
        computeMaxPathSum(i, res);
        return res[0];
    }

    private int computeMaxPathSum(int i, int[] res) {
        if (i >= size) return 0;

        int leftVal = computeMaxPathSum((i << 1) + 1, res);
        int rightVal = computeMaxPathSum((i << 1) + 2, res);

        leftVal = (leftVal > 0) ? leftVal : 0;
        rightVal = (rightVal > 0) ? rightVal : 0;

        int curSum = leftVal + rightVal + tree[i];
        if (curSum > res[0]) res[0] = curSum;

        return tree[i] + (leftVal > rightVal ? leftVal : rightVal);
    }

    public int findIndex(int value) {
        if (size == 0) return -1;

        int i = 0;
        if (size % 2 == 1) {
            if (tree[i] == value) return i;
            i = 1;
        }

        while (i < size - 1) {
            if (tree[i] == value) return i;
            if (tree[i + 1] == value) return i + 1;
            i += 2;
        }

        return -1;
    }

//    public int getDepth(int value) {
//        if (size == 0) return -1;
//
//        int match = findIndex(value);
//        int nodeDepth = 0;
//
//        while (match > 0) {
//            match = (match - 1) >> 1;
//            nodeDepth++;
//        }
//
//        return nodeDepth;
//    }

    // Optimize by calculating depth = log2(i + 1)
    public int getDepth(int value) {
        int index = findIndex(value);
        if (index == -1) return -1;

        // 31 - number of leading zeros is the floor(log2)
        return 31 - Integer.numberOfLeadingZeros(index + 1);
    }

//    public int getHeight(int value) {
//        if (size == 0) return -1;
//
//        int match = findIndex(value);
//        int nodeHeight = 0;
//        int bottom = size - 1;
//
//        while (bottom > match) {
//            bottom = (bottom - 1) >> 1;
//            nodeHeight++;
//        }
//
//        return nodeHeight;
//    }

    // Optimize by calculating height(i) = log2(n) - log2(i + 1)
    public int getHeight(int value) {
        if (size == 0) return -1;

        int match = -1;
        for (int i = 0; i < size; i++) {
            if (tree[i] == value) {
                match = i;
                break;
            }
        }

        if (match == -1) return -1;

        // Calculate the level of the matching element using log2(match + 1)
        int matchLevel = 31 - Integer.numberOfLeadingZeros(match + 1);
        // Subtract the match level from total tree height
        return height() - matchLevel;
    }
}
