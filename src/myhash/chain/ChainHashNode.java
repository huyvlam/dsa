package myhash.chain;

public class ChainHashNode<K, V> {
    public final K key;
    public V value;
    public ChainHashNode<K, V> next;

    public ChainHashNode(K key, V value, ChainHashNode<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }
}
