package mytree.bst;

public class BSTNode {
    int value;
    int count;
    BSTNode left, right;

    public BSTNode(int value) {
        this.value = value;
        count = 1;
        left = null;
        right = null;
    }
}
