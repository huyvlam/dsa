package myheap;

public abstract class BinaryHeap {
    int[] root;
    int capacity;
    int size;
    int modCount;
    boolean sorted;

    public BinaryHeap(int capacity) {
        this.capacity = capacity;
        root = new int[this.capacity];
        size = 0;
        modCount = 0;
        sorted = false;
    }

    protected abstract void bubble(int i);
    protected abstract void sink(int i, int n);
    protected abstract void provision(int i, int n);
    protected abstract void buildHeap(int[] arr, int n);
    protected abstract void sortHeap();

    public void clear() {
        root = new int[this.capacity];
        size = 0;
        sorted = false;
        modCount++;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    public boolean isFull() {
        return size == capacity;
    }
    public int capacity() {
        return capacity;
    }
    public int size() {
        return size;
    }

    public int peek() {
        if (size == 0) return Integer.MIN_VALUE;
        return root[0];
    }

    public int pop() {
        checkSorted();
        if (size == 0) return Integer.MIN_VALUE;

        int top = root[0];
        int i = --size;
        root[0] = root[i];
        root[i] = 0;
        modCount++;

        if (size > 1) sink(0, size);

        return top;
    }

    public boolean insert(int value) {
        checkSorted();
        int i = size++;
        root[i] = value;
        modCount++;

        bubble(i);
        return true;
    }

    public int replace(int value) {
        checkSorted();
        if (size == 0) return Integer.MIN_VALUE;

        int top = root[0];
        root[0] = value;
        modCount++;

        if (size > 1) sink(0, size);
        return top;
    }

    public int deleteKey(int i) {
        checkSorted();
        if (size == 0) return Integer.MIN_VALUE;

        int deleted = root[i];
        root[i] = root[size - 1];
        root[size - 1] = 0;
        size--;
        modCount++;

        if (i != size || size > 1) provision(i, size);

        return deleted;
    }

    public int changeKey(int i, int value) {
        checkSorted();
        if (size == 0) return Integer.MIN_VALUE;

        int changed = root[i];
        root[i] = value;
        modCount++;

        if (size > 1) provision(i, size);

        return changed;
    }

    /**
     * Method is called only to restore the heap structure after it has been sorted
     */
    public void rebuild() {
        if (size <= 1 || !sorted) return;
        build(root, size);
    }

    public void build(int[] arr, int n) {
        if (n <= 1 || arr.length <= 1) return;
        buildHeap(arr, n);
        root = arr;
        size = n;
        sorted = false;
        modCount++;
    }

    public void sort() {
        if (sorted || size <= 1) return;
        sortHeap();
        sorted = true;
        modCount++;
    }

    /**
     * Merge an array into the current heap
     * @param arr   the array to merge
     * @param start merge start at this index
     * @param end   merge end at this index inclusively
     */
    public void merge(int[] arr, int start, int end) {
        checkSorted();
        if (start < 0 || end >= arr.length || start > end) {
            throw new IllegalArgumentException("Indices out of range");
        }

        int m = (end - start) + 1;
        int requiredCap = size + m;

        if (requiredCap > capacity) {
            int newCap = requiredCap + (requiredCap >> 1);
            int[] newHeap = new int[newCap];

            System.arraycopy(root, 0, newHeap, 0, size);
            root = newHeap;
            capacity = newCap;
        }

        System.arraycopy(arr, start, root, size, m);
        size += m;
        build(root, size);
        modCount++;
    }

    void checkSorted() {
        if (sorted) throw new IllegalStateException("Heap is sorted. Call rebuild method to enable this task.");
    }
}