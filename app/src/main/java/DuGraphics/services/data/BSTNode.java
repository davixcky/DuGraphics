package DuGraphics.services.data;

public class BSTNode<T> extends Node<T> {
    private BSTNode<T> left, right;

    public BSTNode(T value) {
        super(value);
        left = right = null;
    }

    public void insert(T childValue) {
        int comparisonValue = childValue.toString().compareTo(value.toString());
        if (comparisonValue < 0) {
            if (isLeftNull()) {
                left = new BSTNode<>(childValue);
                return;
            }

            left.insert(childValue);
            return;
        }

        if (comparisonValue > 0) {
            if (isRightNull()) {
                right = new BSTNode<>(childValue);
                return;
            }

            right.insert(childValue);
            return;
        }

        System.out.println("value not inserted");
    }

    public void setRight(BSTNode<T> node) {
        right = node;
    }

    public void setLeft(BSTNode<T> node) {
        left = node;
    }

    public BSTNode<T> getRight() {
        return right;
    }

    public BSTNode<T> getLeft() {
        return left;
    }

    public boolean isRightNull() {
        return right == null;
    }

    public boolean isLeftNull() {
        return left == null;
    }

    public int nodosCompletos(BSTNode<T> n) {
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
