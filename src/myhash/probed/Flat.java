package myhash.probed;

public class Flat<K, V> {
    public K key;
    public V value;
    public boolean deleted;

    public Flat(K key, V value) {
        this.key = key;
        this.value = value;
        deleted = false;
    }
}
