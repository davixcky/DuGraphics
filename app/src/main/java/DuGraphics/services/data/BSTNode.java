package DuGraphics.services.data;

public class BSTNode extends Node<Integer> {
    private BSTNode left, right;

    public BSTNode(int value) {
        super(value);
        left = right = null;
    }

    public void insert(int childValue) {
        if (childValue < value) {
            if (isLeftNull()) {
                left = new BSTNode(childValue);
                return;
            }

            left.insert(childValue);
            return;
        }

        if (childValue > value) {
            if (isRightNull()) {
                right = new BSTNode(childValue);
                return;
            }

            right.insert(childValue);
            return;
        }

        System.out.println("value not inserted");
    }

    public void setRight(BSTNode node) {
        right = node;
    }

    public void setLeft(BSTNode node) {
        left = node;
    }

    public BSTNode getRight() {
        return right;
    }

    public BSTNode getLeft() {
        return left;
    }

    public boolean isRightNull() {
        return right == null;
    }

    public boolean isLeftNull() {
        return left == null;
    }

    public int nodosCompletos(BSTNode n) {
        if (n == null) {
            return 0;
        } else {
            if (n.left != null && n.right != null) {
                return nodosCompletos(n.left) + nodosCompletos(n.right) + 1;
            }
            return nodosCompletos(n.left) + nodosCompletos(n.right);
        }
    }
}
