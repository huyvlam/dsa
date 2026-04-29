package myheap;

public class BinaryHeapUtil {
    public static void buildMax(int[] arr, int size) {
        if (size <= 1) return;

        for (int i = (size >> 1) - 1; i >= 0; i--) {
            sinkMax(arr, i, size);
        }
    }

    public static int[] mergeMax(int[] a, int[] b) {
        int n = a.length;
        int m = b.length;
        int[] combined = new int[n + m];

        System.arraycopy(a, 0, combined, 0, n);
        System.arraycopy(b, 0, combined, n, m);
        buildMax(combined, n + m);

        return combined;
    }

    public static void sortMax(int[] arr, int size) {
        if (size <= 1) return;

        buildMax(arr, size);

        for (int i = size - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            sinkMax(arr, 0, i);
        }
    }

    public static void sinkMax(int[] arr, int index, int size) {
        if (size <= 1) return;

        int target = arr[index];
        int cur = index;

        while (true) {
            int left = (cur << 1) + 1;
            int right = (cur << 1) + 2;
            if (left >= size) break;

            int max = left;
            if (right < size && arr[right] > arr[left]) {
                max = right;
            }

            if (arr[max] <= target) break;

            arr[cur] = arr[max];
            cur = max;
        }
        arr[cur] = target;
    }

    public static void bubbleMax(int[] arr, int index) {
        if (index < 0 || index >= arr.length) throw new IndexOutOfBoundsException("Index is out of bounds");

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

    public static void buildMin(int[] arr, int size) {
        if (size <= 1) return;

        for (int i = (size >> 1) - 1; i >= 0; i--) {
            sinkMin(arr, i, size);
        }
    }

    public static int[] mergeMin(int[] a, int[] b) {
        int n = a.length;
        int m = b.length;
        int[] combined = new int[n + m];

        System.arraycopy(a, 0, combined, 0, n);
        System.arraycopy(b, 0, combined, n, m);
        buildMin(combined, n + m);

        return combined;
    }

    public static void sortMin(int[] arr, int size) {
        if (size <= 1) return;

        buildMin(arr, size);

        for (int i = size - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            sinkMin(arr, 0, i);
        }
    }

    public static void sinkMin(int[] arr, int index, int size) {
        if (size <= 1) return;

        int target = arr[index];
        int cur = index;

        while (true) {
            int left = (cur << 1) + 1;
            int right = (cur << 1) + 2;
            if (left >= size) break;

            int min = left;
            if (right < size && arr[right] < arr[left]) {
                min = right;
            }

            if (arr[min] >= target) break;

            arr[cur] = arr[min];
            cur = min;
        }
        arr[cur] = target;
    }

    public static void bubbleMin(int[] arr, int index) {
        if (index < 0 || index >= arr.length) throw new IndexOutOfBoundsException("Index is out of bounds");

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
