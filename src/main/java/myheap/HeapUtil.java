package myheap;

public class HeapUtil {
    public static void buildMaxHeap(int[] arr, int size) {
        if (size <= 1) return;

        for (int i = (size >> 1) - 1; i >= 0; i--) {
            maxSink(arr, i, size);
        }
    }

    public static void maxSink(int[] arr, int index, int size) {
        int target = arr[index];
        int cur = index;

        while (true) {
            int left = (index << 1) + 1;
            int right = (index << 1) + 2;
            if (left >= size) break;

            int max = left;
            if (right < size && arr[right] > arr[left]) {
                max = right;
            }

            if (target >= arr[max]) break;

            arr[cur] = arr[max];
            cur = max;
        }
        arr[cur] = target;
    }

    public static void maxBubble(int[] arr, int index) {
        int target = arr[index];
        int cur = index;

        while (cur > 0) {
            int parent = (cur - 1) >> 1;
            if (arr[parent] >= target) break;

            arr[cur] = arr[parent];
            cur = parent;
        }
        arr[cur] = target;
    }

    public static void buildMinHeap(int[] arr, int size) {
        if (size <= 1) return;

        for (int i = (size >> 1) - 1; i >= 0; i--) {
            minSink(arr, i, size);
        }
    }

    public static void minSink(int[] arr, int index, int size) {
        int target = arr[index];
        int cur = index;

        while (true) {
            int left = (index << 1) + 1;
            int right = (index << 1) + 2;
            if (left >= size) break;

            int min = left;
            if (right < size && arr[right] < arr[left]) {
                min = right;
            }

            if (target <= arr[min]) break;

            arr[cur] = arr[min];
            cur = min;
        }
        arr[cur] = target;
    }

    public static void minBubble(int[] arr, int index) {
        int target = arr[index];
        int cur = index;

        while (cur > 0) {
            int parent = (cur - 1) >> 1;
            if (arr[parent] <= target) break;

            arr[cur] = arr[parent];
            cur = parent;
        }

        arr[cur] = target;
    }
}
