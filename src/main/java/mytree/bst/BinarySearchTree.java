package mytree.bst;

import myqueue.CircularArrayQueue;

public class BinarySearchTree {
    BSTNode root;
    int size;

    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public int leafCount() {
        return size == 0 ? 0 : (size + 1) >> 1;
    }

    public void iterativeInsert(int value) {
        if (root == null) {
            root = new BSTNode(value);
            size++;
            return;
        }

        BSTNode cur = root;
        BSTNode parent = null;

        while (cur != null) {
            parent = cur;

            if (value < cur.value) {
                cur = cur.left;
            } else if (value > cur.value) {
                cur = cur.right;
            } else {
                cur.count++;
                return;
            }
        }

        if (value < parent.value) {
            parent.left = new BSTNode(value);
        } else {
            parent.right = new BSTNode(value);
        }

        size++;
    }

    public void recursiveInsert(int value) {
        root = recursiveInsert(root, value);
    }

    private BSTNode recursiveInsert(BSTNode node, int value) {
        if (node == null) {
            size++;
            return new BSTNode(value);
        }

        if (value < node.value) {
            node.left = recursiveInsert(node.left, value);
        } else if (value > node.value) {
            node.right = recursiveInsert(node.right, value);
        } else {
            node.count++;
        }

        return node;
    }

    public void iterativeDelete(int value) {
        if (root == null) return;

        BSTNode cur = root;
        BSTNode parent = null;

        // scan the tree & track its parent
        while (cur != null && cur.value != value) {
            parent = cur;

            if (value < cur.value) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }

        // value not found
        if (cur == null) return;

        // duplicate
        if (cur.count > 1) {
            cur.count--;
            return;
        }

        // 0 or 1 child
        if (cur.left == null || cur.right == null) {
            BSTNode nextChild = (cur.left != null) ? cur.left : cur.right;

            if (cur == root) {
                root = nextChild;
            } else if (parent.left == cur) {
                parent.left = nextChild;
            } else {
                parent.right = nextChild;
            }
            size--;
            return;
        }

        // 2 children
        BSTNode successorParent = cur;
        BSTNode successor = cur.left;

        while (successor.right != null) {
            successorParent = successor;
            successor = successor.right;
        }

        cur.value = successor.value;
        cur.count = successor.count;

        if (successorParent == cur) {
            cur.left = successor.left;
        } else {
            successorParent.right = successor.left;
        }

        size--;
    }

    public void recursiveDelete(int value) {
        if (root == null) return;

        root = recursiveDelete(root, value);
    }

    private BSTNode recursiveDelete(BSTNode node, int value) {
        // base case
        if (node == null) return null;

        // traverse
        if (value < node.value) {
            node.left = recursiveDelete(node.left, value);
        } else if (value > node.value) {
            node.right = recursiveDelete(node.right, value);
        } else {
            // duplicate
            if (node.count > 1) {
                node.count--;
                return node;
            }

            // node has 0 or 1 child
            if (node.left == null) {
                size--;
                return node.right;
            } else if (node.right == null) {
                size--;
                return node.left;
            }

            // node has 2 children, find min of right branch
            BSTNode parent = node;
            BSTNode cur = node.right;

            while (cur.left != null) {
                parent = cur;
                cur = cur.left;
            }

            node.value = cur.value;
            node.count = cur.count;

            if (parent == node) {
                node.right = cur.right;
            } else {
                parent.left = cur.right;
            }
            size--;
        }

        return node;
    }

    public BSTNode findMin(BSTNode node) {
        BSTNode cur = node;
        while (cur != null && cur.left != null) {
            cur = cur.left;
        }

        return cur;
    }

    public BSTNode findMax(BSTNode node) {
        BSTNode cur = node;
        while (cur != null && cur.right != null) {
            cur = cur.right;
        }

        return cur;
    }
}
