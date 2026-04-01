package myhash.probed;

public class HashNode<K, V> {
    public K key;
    public V value;
    public boolean deleted;

    public HashNode(K key, V value) {
        this.key = key;
        this.value = value;
        deleted = false;
    }
}
