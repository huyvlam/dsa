package mytree;

import java.util.Random;

public class ArrayBinaryTree {
    private int[] tree;
    private int initCapacity;
    private int size;
    private int maxPathSum;
    private boolean computed;
    public ArrayBinaryTree(int capacity) {
        if (capacity < 4) throw new IllegalArgumentException("Capacity must be 4 or greater");

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
        return size == tree.length;
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
        for (int i = 0; i < size; i++) {
            if (tree[i] == value) {
                tree[i] = tree[size - 1];
                tree[size - 1] = 0;
                size--;
                computed = false;
                return;
            }
        }
    }

    public int getMaxPathSum() {
        if (computed) {
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
        for (int i = 0; i < size; i++) {
            if (tree[i] == value) return i;
        }
        return -1;
    }

    static void main() {
        ArrayBinaryTree tree = new ArrayBinaryTree(8);
        Random rand = new Random();
        int n = 7;
        int[] vals = new int[n];
        for (int i = 0; i < n; i++) {
            vals[i] = rand.nextInt(16);
            tree.insert(vals[i]);
            IO.println("Size: " + tree.size() + " Height: " + tree.height());
        }
        IO.println("Root Max Path Sum: " + tree.getMaxPathSum());
        IO.println("Subtree Max Path Sum: " + tree.getMaxPathSum(3));
        tree.delete(vals[0]);
    }
}
