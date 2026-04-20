package mytree;

import myqueue.CircularArrayQueue;

import java.util.function.Predicate;

public class LinkedBinaryTree {
    BTNode root;
    private int size;
    private int height;
    private int maxPathSum;
    private boolean computed;

    public LinkedBinaryTree() {
        root = null;
        size = 0;
        height = -1;
        computed = false;
    }

    public void clear() {
        root = null;
        size = 0;
        height = -1;
        computed = false;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        boolean[] res = {true};
        levelOrderScan((node) -> {
            if ((node.left == null || node.right == null) && node.left != node.right) {
                res[0] = false;
                return false;
            }
            return true;
        });
        return res[0];
//        CircularArrayQueue<BTNode> queue = new CircularArrayQueue<>(1 << (height + 1));
//        queue.add(root);
//
//        while (!queue.isEmpty()) {
//            BTNode cur = queue.remove();
//
//            if ((cur.left == null || cur.right == null) && cur.left != cur.right) return false;
//            if (cur.left != null) queue.add(cur.left);
//            if (cur.right != null) queue.add(cur.right);
//        }
//        return true;
    }

    public int size() {
        return size;
    }

    public int height() {
        return (size == 0) ? -1 : 31 - Integer.numberOfLeadingZeros(size);
    }

    public void insert(int value) {
        BTNode node = new BTNode(value);
        if (root == null) {
            root = node;
        } else {
            CircularArrayQueue<BTNode> queue = new CircularArrayQueue<>(1 << (height + 1));
            queue.add(root);

            while (!queue.isEmpty()) {
                int n = queue.size();

                for (int i = 0; i < n; i++) {
                    BTNode cur = queue.remove();

                    if (cur.left == null) {
                        cur.left = node;
                        queue.clear();
                        break;
                    }
                    if (cur.right == null) {
                        cur.right = node;
                        queue.clear();
                        break;
                    }

                    queue.add(cur.left);
                    queue.add(cur.right);
                }
            }
        }
        size++;
        height = height();
        computed = false;
    }

    public void delete(int value) {
        if (root == null) return;

        if (size == 1) {
            if (root.value == value) {
                root = null;
                size = 0;
                height = -1;
                computed = false;
            }
            return;
        }

        BTNode deletingNode = null;
        BTNode cur = null;
        BTNode prev = null;

        CircularArrayQueue<BTNode> queue = new CircularArrayQueue<>(1 << (height + 1));
        queue.add(root);

        while (!queue.isEmpty()) {
            cur = queue.remove();

            // Found the match, store it
            if (cur.value == value) {
                deletingNode = cur;
            }
            if (cur.left != null) {
                prev = cur;
                queue.add(cur.left);
            }
            if (cur.right != null) {
                prev = cur;
                queue.add(cur.right);
            }
        }

        if (deletingNode == null) return;

        deletingNode.value = cur.value;

        if (prev != null) {
            if (prev.right == cur) prev.right = null;
            else prev.left = null;
        }
        size--;
        height = height();
        computed = false;
    }

    public int getMaxPathSum() {
        if (!computed) {
            maxPathSum = getMaxPathSum(root);
            computed = true;
        }
        return maxPathSum;
    }

    public int getMaxPathSum(BTNode subroot) {
        int[] res = {Integer.MIN_VALUE};
        computeMaxPathSum(subroot, res);
        return res[0];
    }

    private int computeMaxPathSum(BTNode subroot, int[] res) {
        if (subroot == null) return 0;

        int leftVal = computeMaxPathSum(subroot.left, res);
        int rightVal = computeMaxPathSum(subroot.right, res);

        leftVal = (leftVal > 0) ? leftVal : 0;
        rightVal = (rightVal > 0) ? rightVal : 0;

        int curSum = leftVal + rightVal + subroot.value;
        if (curSum > res[0]) res[0] = curSum;

        return subroot.value + (leftVal > rightVal ? leftVal : rightVal);
    }

    public BTNode find(int value) {
        if (root == null) return null;

        BTNode[] res = {null};
        levelOrderScan((node) -> {
            if (node.value == value) {
                res[0] = node;
                return false;
            }
            return true;
        });
        return res[0];
    }

    public int getDepth(int value) {
        if (root == null) return -1;
        int nodeDepth = 0;

        CircularArrayQueue<BTNode> queue = new CircularArrayQueue<>(1 << (height + 1));
        queue.add(root);

        while (!queue.isEmpty()) {
            int n = queue.size();
            for (int i = 0; i < n; i++) {
                BTNode cur = queue.remove();

                if (cur.value == value) return nodeDepth;
                if (cur.left != null) queue.add(cur.left);
                if (cur.right != null) queue.add(cur.right);
            }
            nodeDepth++;
        }
        return -1;
    }

    public int getHeight(int value) {
        if (root == null) return -1;

        int nodeHeight = -1;
        boolean found = false;
        CircularArrayQueue<BTNode> queue = new CircularArrayQueue<>(1 << (height + 1));
        queue.add(root);

        while (!queue.isEmpty()) {
            int n = queue.size();

            for (int i = 0; i < n; i++) {
                BTNode cur = queue.remove();

                if (cur.value == value) found = true;
                if (cur.left != null) queue.add(cur.left);
                if (cur.right != null) queue.add(cur.right);
            }

            if (found) nodeHeight++;
        }
        return nodeHeight;
    }

    private void levelOrderScan(Predicate<BTNode> action) {
        CircularArrayQueue<BTNode> queue = new CircularArrayQueue<>(1 << (height + 1));
        queue.add(root);

        while (!queue.isEmpty()) {
            int n = queue.size();

            for (int i = 0; i < n; i++) {
                BTNode cur = queue.remove();
                if (!action.test(cur)) {
                    queue.clear();
                    return;
                }
                if (cur.left != null) queue.add(cur.left);
                if (cur.right != null) queue.add(cur.right);
            }
        }
    }
}
