package myhash.probed;

public class FlatNode<K, V> {
    public K key;
    public V value;
    public boolean deleted;

    public FlatNode(K key, V value) {
        this.key = key;
        this.value = value;
        deleted = false;
    }
}
