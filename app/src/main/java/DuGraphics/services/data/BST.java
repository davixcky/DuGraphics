package DuGraphics.services.data;

public class BST {
    private BSTNode root;

    public BST() {
        root = null;
    }

    public void insertNode(int nodeValue) {
        if (root == null) {
            root = new BSTNode(nodeValue);
            return;
        }

        root.insert(nodeValue);
    }

    public void print() {
        System.out.println("Printing tree");
        print(root);
    }

    private void print(BSTNode node) {
        if (node == null) return;

        System.out.println(node.getValue());
        print(node.getLeft());
        print(node.getRight());
    }

    public void preorder() {
        System.out.print("Preorder: ");
        internal_preorder(root, node -> System.out.print(node.getValue() + " "));
        System.out.println();
    }

    public void preorder(INodeListener<Integer> listener) {
        System.out.print("Preorder: ");
        internal_preorder(root, listener);
        System.out.println();
    }

    public void postorder() {
        System.out.print("Postorder: ");
        internal_postorder(root);
        System.out.println();
    }

    public void inorder() {
        System.out.print("Inorder: ");
        internal_inorder(root);
        System.out.println();
    }

    public int height() {
        return internal_height(root);
    }

    public int size() {
        return internal_size(root);
    }

    public void level(int level) {
        System.out.print("Printing level #" + level + ": ");
        internal_level(root, level, (node) -> System.out.print(node.getValue() + " "));
        System.out.println();
    }

    public void level(int level, INodeListener<Integer> listener) {
        internal_level(root, level, listener);
    }

    public void deleteLeafs() {
        if (root != null && root.isRightNull() && root.isLeftNull()) {
            System.out.println("root value: " + root.value);
            root = null;
            return;
        }

        internal_deleteLeafs(root, (node) -> System.out.println("deleting node " + node.getValue()));
    }

    public void deleteLeafs(INodeListener<Integer> listener) {
        if (root != null && root.isRightNull() && root.isLeftNull()) {
            listener.action(root);
            root = null;
            return;
        }

        internal_deleteLeafs(root, listener);
    }

    private void internal_preorder(BSTNode node, INodeListener<Integer> listener) {
        if (node == null) return;

        listener.action(node);
        internal_preorder(node.getLeft(), listener);
        internal_preorder(node.getRight(), listener);
    }

    private void internal_postorder(BSTNode node) {
        if (node == null) return;

        internal_postorder(node.getLeft());
        internal_postorder(node.getRight());
        System.out.print(node.getValue() + " ");
    }

    private void internal_inorder(BSTNode node) {
        if (node == null) return;

        internal_inorder(node.getLeft());
        System.out.print(node.getValue() + " ");
        internal_inorder(node.getRight());
    }

    public int internal_height(BSTNode node) {
        if (node == null) return -1;

        return 1 + Math.max(internal_height(node.getLeft()), internal_height(node.getRight()));
    }

    private int internal_size(BSTNode node) {
        if (node == null) return 0;

        return 1 + internal_size(node.getLeft()) + internal_size(node.getRight());
    }

    private BSTNode internal_deleteLeafs(BSTNode node, INodeListener<Integer> listener) {
        if (node == null) return null;

        if (node.isLeftNull() && node.isRightNull()) {
            listener.action(node);
            return null;
        }
        node.setLeft(internal_deleteLeafs(node.getLeft(), listener));
        node.setRight(internal_deleteLeafs(node.getRight(), listener));
        return node;
    }

    public void internal_level(BSTNode node, int i, INodeListener<Integer> listener) {
        if (node == null) return;

        if (i == 1) {
            listener.action(node);
            return;
        }

        internal_level(node.getLeft(), i - 1, listener);
        internal_level(node.getRight(), i - 1, listener);
    }

    public BSTNode getRoot() {
        return root;
    }

}
