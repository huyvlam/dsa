package myheap;

public class MinBinaryHeap extends BinaryHeap {
    public MinBinaryHeap(int capacity) {
        super(capacity);
    }

    @Override
    protected void bubble(int i){
        BinaryHeapUtil.bubbleMin(root, i);
    }

    @Override
    protected void sink(int i, int n){
        BinaryHeapUtil.sinkMin(root, i, n);
    }

    @Override
    protected void provision(int i, int n){
        if (i > 0 && root[i] > root[(i - 1) >> 1]) {
            bubble(i);
        } else {
            sink(i, n);
        }
    }

    @Override
    protected void buildHeap(int[] arr, int n) {
        BinaryHeapUtil.buildMin(arr, n);
    }

    @Override
    protected void sortHeap() {
        for (int i = size - 1; i > 0; i--) {
            int temp = root[0];
            root[0] = root[i];
            root[i] = temp;

            sink(0, i);
        }
    }
}
