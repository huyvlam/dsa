package myheap;

public class VirtualBinaryHeap {
    public static void buildMax(int[] a, int[] b) {
        int n = a.length + b.length;
        if (n <= 1) return;

        for (int i = (n >> 1) - 1; i >= 0; i--) {
            sinkMax(a, b, i, n);
        }
    }

    public static void mergeMax(int[] a, int[] b) {
        buildMax(a, b);
    }

    public static void sortMax(int[] a, int[] b) {
        buildMax(a, b);

        int n = a.length + b.length;
        for (int i = n - 1; i > 0; i--) {
            int temp = get(a, b, 0);
            set(a, b, 0, get(a, b, i));
            set(a, b, i, temp);
            sinkMax(a, b, 0, i);
        }
    }

    public static void sinkMax(int[] a, int[] b, int i, int n) {
        if (n <= 1) return;

        int target = get(a, b, i);
        int cur = i;

        while (true) {
            int left = (cur << 1) + 1;
            int right = (cur << 1) + 2;
            if (left >= n) break;

            int max = left;
            if (right < n && get(a, b, right) > get(a, b, left)) {
                max = right;
            }

            if (get(a, b, max) <= target) break;

            set(a, b, cur, get(a, b, max));
            cur = max;
        }
        set(a, b, cur, target);
    }

    public static void buildMin(int[] a, int[] b) {
        int n = a.length + b.length;
        if (n <= 1) return;

        for (int i = (n >> 1) - 1; i >= 0; i--) {
            sinkMin(a, b, i, n);
        }
    }

    public static void mergeMin(int[] a, int[] b) {
        buildMin(a, b);
    }

    public static void sortMin(int[] a, int[] b) {
        buildMin(a, b);

        int n = a.length + b.length;
        for (int i = n - 1; i > 0; i--) {
            int temp = get(a, b, 0);
            set(a, b, 0, get(a, b, i));
            set(a, b, i, temp);
            sinkMin(a, b, 0, i);
        }
    }

    public static void sinkMin(int[] a, int[] b, int i, int n) {
        if (n <= 1) return;

        int target = get(a, b, i);
        int cur = i;

        while (true) {
            int left = (cur << 1) + 1;
            int right = (cur << 1) + 2;
            if (left >= n) break;

            int min = left;
            if (right < n && get(a, b, right) < get(a, b, left)) {
                min = right;
            }

            if (get(a, b, min) >= target) break;

            set(a, b, cur, get(a, b, min));
            cur = min;
        }
        set(a, b, cur, target);
    }

    public static int get(int[] a, int[] b, int i) {
        return (i < a.length) ? a[i] : b[i - a.length];
    }

    public static void set(int[] a, int[] b, int i, int val) {
        if (i < a.length) a[i] = val;
        else b[i - a.length] = val;
    }
}
