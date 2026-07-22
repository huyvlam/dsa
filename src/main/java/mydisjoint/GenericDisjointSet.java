package mydisjoint;

import java.util.*;

public class GenericDisjointSet<T> {
    private final DisjointSet disjointSet;
    private final Map<T, Integer> objectToIndex;
    private final List<T> indexToObject;

    public GenericDisjointSet(Collection<T> objects) {
        int n = objects.size();
        this.disjointSet = new DisjointSet(n);
        this.objectToIndex = new HashMap<>(n);
        this.indexToObject = new ArrayList<>(n);

        int index = 0;
        for (T obj : objects) {
            objectToIndex.put(obj, index);
            indexToObject.add(obj);
            index++;
        }
    }

    public boolean union(T a, T b) {
        Integer indexA = objectToIndex.get(a);
        Integer indexB = objectToIndex.get(b);

        if (indexA == null || indexB == null) {
            return false;
        }

        return disjointSet.union(indexA, indexB);
    }

    public T findRoot(T object) {
        Integer index = objectToIndex.get(object);

        if (index == null) {
            return null;
        }

        int rootIndex = disjointSet.find(index);
        return indexToObject.get(rootIndex);
    }

    /**
     * Extract and return all merged sets
     */
    public Map<T, List<T>> getClusters() {
        Map<T, List<T>> clusters = new HashMap<>();

        for (int i = 0; i < indexToObject.size(); i++) {
            int rootIndex = disjointSet.find(i);
            T rootObject = indexToObject.get(rootIndex);
            T curObject = indexToObject.get(i);

            clusters.computeIfAbsent(rootObject, k -> new ArrayList<>()).add(curObject);
        }

        return clusters;
    }

    public boolean connected(T a, T b) {
        Integer indexA = objectToIndex.get(a);
        Integer indexB = objectToIndex.get(b);

        if (indexA == null || indexB == null) {
            return false;
        }

        return disjointSet.find(indexA) == disjointSet.find(indexB);
    }

    public int getCount() {
        return disjointSet.getCount();
    }
}
