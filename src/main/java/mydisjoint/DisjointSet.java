package mydisjoint;

public class DisjointSet {
    private final int[] parent;
    private final int[] size; // track the size of each set
    private int count; // count the number of set

    public DisjointSet(int n) {
        parent = new int[n];
        size = new int[n];
        count = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int p) {
        if (p < 0 || p >= parent.length) {
            throw new IllegalArgumentException("Index out of bounds: " + p);
        }

        // 1. Find the parent
        if (p == parent[p]) {
            return p;
        }

        // 2. Path Compression: link children of subtree directly to root
        return parent[p] = find(parent[p]);
    }

    public int iterativeFind(int p) {
        if (p < 0 || p >= parent.length) {
            throw new IllegalArgumentException("Index out of bounds: " + p);
        }

        // 1. Find the root
        int root = p;
        while (root != parent[root]) {
            root = parent[root];
        }

        // 2. Path Compression: link elements of subtree directly to the root
        int cur = p;
        while (cur != root) {
            int next = parent[cur];
            parent[cur] = root;
            cur = next;
        }

        return root;
    }

    // Union by size
    public boolean union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);

        if (rootP == rootQ) {
            return false;
        }

        // Merge the smaller tree under larger tree
        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        } else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }

        count--;
        return true;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public int getCount() {
        return count;
    }

    public int getSize(int p) {
        return size[find(p)];
    }
}
