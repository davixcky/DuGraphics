package DuGraphics.states.tree;

import DuGraphics.Handler;
import DuGraphics.gfx.Assets;
import DuGraphics.services.data.common.Node;
import DuGraphics.services.data.tree.BST;
import DuGraphics.services.data.tree.BSTNode;
import DuGraphics.states.State;
import DuGraphics.states.main.MainState;
import DuGraphics.ui.UIButton;
import DuGraphics.ui.UIInput;
import DuGraphics.ui.UIObject;
import DuGraphics.ui.components.UIBoxList;
import DuGraphics.ui.components.UINode;

import java.awt.*;
import java.util.HashMap;

public class TreeState extends State {

    public static final String STATE_NAME = "TREE_STATE";

    private final BST bstData;
    private final HashMap<Integer, UINode> uiNodes;

    private final Dimension rightColumnDimension;
    private UIInput nodeValueInput, levelInput;
    private UIButton
            saveNodeBtn,
            levelSubmitBtn,
            backBtn,
            clearTreeBtn,
            searchUncleBtn,
            preorderBtn,
            postorderBtn,
            inorderBtn;
    private UINode rootNode;
    private boolean deletingTree = false;
    private int currentIndexDeletion = 0; // Max index per deletion is 150
    private int currentUncle = -1;

    private UIBoxList boxList;

    private LIST_TYPE currentListType = LIST_TYPE.PREORDER;
    private int currentLevel = 1;
    private boolean shouldUpdate = false;

    enum LIST_TYPE {
        PREORDER,
        INORDER,
        POSTORDER,
        LEVEL,
    }

    private static final String[] LIST_TYPE_ALIASES = new String[]{"PREORDER", "INORDER", "POSTORDER", "LEVEL"};

    public TreeState(Handler handler) {
        super(STATE_NAME, handler, "Tree visualizer");

        bstData = new BST();
        rightColumnDimension = new Dimension();

        uiNodes = new HashMap<>();
        rootNode = null;
    }

    @Override
    protected void initComponents() {
        nodeValueInput = new UIInput(this);
        nodeValueInput.setListener(this::saveValue);
        nodeValueInput.setCharLimits(1, 3);

        saveNodeBtn = new UIButton(this, this::saveValue);
        saveNodeBtn.setText("SAVE");

        searchUncleBtn = new UIButton(this, this::searchUncle);
        searchUncleBtn.setText("SEARCH UNCLE");

        levelInput = new UIInput(this);
        levelInput.setListener(this::handleLevel);
        levelInput.setCharLimits(1, 2);

        levelSubmitBtn = new UIButton(this, this::handleLevel);
        levelSubmitBtn.setText("SEARCH LEVEL");

        clearTreeBtn = new UIButton(this, this::clearTree);
        clearTreeBtn.setText("CLEAR TREE");

        backBtn = new UIButton(this, 30, 30, () -> {
            State.goTo(MainState.STATE_NAME);
            State.getCurrentState().updateDimensions(currentDimension);
        });
        backBtn.setText("BACK TO HOME");
        backBtn.updateCoordsBounds(new Rectangle(20, 20, backBtn.getWidth() + 30, backBtn.getHeight() + 10));

        preorderBtn = new UIButton(this, () -> setCurrentType(LIST_TYPE.PREORDER));
        preorderBtn.setText("PREORDER");

        postorderBtn = new UIButton(this, () -> setCurrentType(LIST_TYPE.POSTORDER));
        postorderBtn.setText("POSTORDER");

        inorderBtn = new UIButton(this, () -> setCurrentType(LIST_TYPE.INORDER));
        inorderBtn.setText("INORDER");

        boxList = new UIBoxList(
                this,
                20, (int) (currentDimension.height * 0.7f),
                currentDimension.width / 2,
                currentDimension.height
        );

        resizeComponents();

        uiManager.addObjects(
                nodeValueInput,
                saveNodeBtn,
                levelInput,
                levelSubmitBtn,
                clearTreeBtn,
                backBtn,
                searchUncleBtn,
                boxList,
                preorderBtn,
                postorderBtn,
                inorderBtn
        );
    }

    private void calculateListValues() {
        System.out.println("Should update");
        System.out.println(currentListType);

        boxList.reset();
        boxList.setTitle(LIST_TYPE_ALIASES[currentListType.ordinal()]);
        switch (currentListType) {
            case LEVEL -> {
                bstData.level(currentLevel, (node) -> {
                    boxList.addNode(node.getValue());
                });
                boxList.setTitle("Values of level #" + currentLevel);
            }
            case PREORDER -> bstData.preorder((node) -> boxList.addNode(node.getValue()));
            case POSTORDER -> bstData.postorder((node) -> boxList.addNode(node.getValue()));
            case INORDER -> bstData.inorder((node) -> boxList.addNode(node.getValue()));
        }

        shouldUpdate = false;
    }

    private void setCurrentType(LIST_TYPE currentListType) {
        shouldUpdate = true;
        this.currentListType = currentListType;
    }

    private void clearTree() {
        deletingTree = true;
        currentIndexDeletion = 0;
        boxList.reset();
    }

    private void handleLevel() {
        currentLevel = levelInput.getValueAsInteger();
        setCurrentType(LIST_TYPE.LEVEL);
    }

    private void saveValue() {
        shouldUpdate = true;
        bstData.insertNode(nodeValueInput.getValueAsInteger());
        bstData.preorder();
    }

    private void searchUncle() {
        int uncle = bstData.getUncle(nodeValueInput.getValueAsInteger());
        System.out.println(uncle);
        currentUncle = uncle;
    }

    private void createRect(Graphics2D g2) {
        int width = (int) (currentDimension.width * 0.2f);
        int x = currentDimension.width - width;

        g2.setColor(Color.red);
        g2.fillRect(x, 0, width, currentDimension.height);
    }

    @Override
    public void update() {
        uiManager.update();

        if (shouldUpdate)
            calculateListValues();

        if (deletingTree) {
            currentIndexDeletion++;

            if (currentIndexDeletion >= 150) {
                bstData.deleteLeafs(this::deleteNode);
                currentIndexDeletion = 0;
            }
            if (bstData.size() == 0) {
                deletingTree = false;
            }
        }
    }

    private void deleteNode(Node<Integer> integerNode) {
        UIObject obj = uiNodes.get(integerNode.getValue());
        uiNodes.remove(integerNode.getValue());
        uiManager.removeObject(obj);
    }


    @Override
    public void render(Graphics g) {
        if (currentDimension.width <= 729) {
            rightColumnDimension.setSize(0, 0);
        }

        paintTree((Graphics2D) g);

        if (currentDimension.width > 729) {
            createRect((Graphics2D) g);
            int width = 40 + (int) (currentDimension.width * 0.04f);

            int containerWidth = (int) (currentDimension.width * 0.2f);
            int x = currentDimension.width - containerWidth / 2;
            UIObject.drawString(g, "Type node value",
                    x + width / 2 - 60,
                    40,
                    true,
                    Color.white,
                    Assets.getFont(Assets.FontsName.SPACE_MISSION, (int) (rightColumnDimension.width * 0.08f)));

            UIObject.drawString(g, "Type level value",
                    x + width / 2 - 60,
                    (int) (rightColumnDimension.height * 0.3f),
                    true,
                    Color.white,
                    Assets.getFont(Assets.FontsName.SPACE_MISSION, (int) (rightColumnDimension.width * 0.08f)));

            UIObject.drawString(g, bstData.size() + " size",
                    x + width / 2 - 60,
                    (int) (rightColumnDimension.height * 0.8f),
                    true,
                    Color.white,
                    Assets.getFont(Assets.FontsName.SLKSCR, (int) (rightColumnDimension.width * 0.08f)));

            UIObject.drawString(g, bstData.height() + "# levels",
                    x + width / 2 - 60,
                    (int) (rightColumnDimension.height * 0.85f),
                    true,
                    Color.white,
                    Assets.getFont(Assets.FontsName.SLKSCR, (int) (rightColumnDimension.width * 0.08f)));

            uiManager.render(g);
        }

    }

    private void paintTree(Graphics2D g) {
        g.setColor(new Color(48, 52, 63, 255));
        g.fillRect(0, 0, currentDimension.width - rightColumnDimension.width, currentDimension.height);

        int x = currentDimension.width / 2;
        int y = 50;
        if (uiNodes.size() > 0) {
            x = (int) rootNode.getX();
            y = (int) rootNode.getY();
        }
        paintNode(g, x, y, bstData.getRoot());
    }

    private void paintNode(Graphics2D g2, int x, int y, BSTNode node) {
        int DIAMETER = 30;
        int RADIO = DIAMETER / 2;
        int WIDTH = 50;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (node != null) {
            int EXTRA = bstData.internal_height(node.getRight()) * WIDTH / 2
                    + bstData.internal_height(node.getLeft()) * WIDTH / 2
                    + node.nodosCompletos(node) * WIDTH / 2;

            UINode uiNode;
            if (!uiNodes.containsKey(node.getValue())) {
                uiNode = new UINode(this, x, y, DIAMETER, DIAMETER, node);
                uiNodes.put(node.getValue(), uiNode);
            } else {
                uiNode = uiNodes.get(node.getValue());

                uiNode.setUncle(false);
                if (currentUncle != -1 && node.getValue() == currentUncle) {
                    uiNode.setUncle(true);
                }
                uiNode.updateCoordsBounds(new Rectangle(x, y, DIAMETER, DIAMETER));
            }

            uiManager.addObject(uiNode);

            if (rootNode == null) {
                rootNode = uiNode;
            }

            g2.setColor(Color.white);
            if (node.getLeft() != null) {
                g2.drawLine(x + RADIO, y + RADIO, x - WIDTH - EXTRA + RADIO, y + WIDTH + RADIO);
            }
            if (node.getRight() != null) {
                g2.drawLine(x + RADIO, y + RADIO, x + WIDTH + EXTRA + RADIO, y + WIDTH + RADIO);
            }

            paintNode(g2, x - WIDTH - EXTRA, y + WIDTH, node.getLeft());
            paintNode(g2, x + WIDTH + EXTRA, y + WIDTH, node.getRight());
        }
    }

    @Override
    protected void resizeComponents() {
        // Update buttons dimensions if change
        int height = 20 + (int) (currentDimension.height * 0.03f);

        int containerWidth = (int) (currentDimension.width * 0.2f);
        int inputHeight = (int) (currentDimension.height * 0.04f);

        int width = (int) (containerWidth * 0.8f);
        int y = 60;

        int spacingX = (int) (containerWidth * 0.1f);
        int initialX = currentDimension.width - containerWidth + spacingX;

        rightColumnDimension.setSize(containerWidth, currentDimension.height);
        nodeValueInput.updateCoordsBounds(new Rectangle(initialX, y, width, inputHeight));
        nodeValueInput.setWidth(width);
        saveNodeBtn.updateCoordsBounds(new Rectangle(initialX, (int) (nodeValueInput.getY() + nodeValueInput.getHeight()), width, height));
        searchUncleBtn.updateCoordsBounds(new Rectangle(initialX, (int) UIObject.getRelativeHeight(saveNodeBtn), width, height));

        y = (int) (rightColumnDimension.height * 0.32f);
        levelInput.updateCoordsBounds(new Rectangle(initialX, y, width, inputHeight));
        levelSubmitBtn.updateCoordsBounds(new Rectangle(initialX, (int) (levelInput.getY() + levelInput.getHeight()), width, height));
        clearTreeBtn.updateCoordsBounds(new Rectangle(initialX, (int) UIObject.getRelativeHeight(levelSubmitBtn), width, height));

        preorderBtn.updateCoordsBounds(new Rectangle(initialX, (int) (UIObject.getRelativeHeight(clearTreeBtn) + 20), width, height));
        postorderBtn.updateCoordsBounds(new Rectangle(initialX, (int) UIObject.getRelativeHeight(preorderBtn), width, height));
        inorderBtn.updateCoordsBounds(new Rectangle(initialX, (int) UIObject.getRelativeHeight(postorderBtn), width, height));

        boxList.updateCoordsBounds(new Rectangle(
                20,
                (int) (currentDimension.height * 0.7f),
                currentDimension.width / 2,
                currentDimension.height
        ));

        int fontSize = (int) (containerWidth * 0.05f);
        saveNodeBtn.setFontSize(fontSize);
        searchUncleBtn.setFontSize(fontSize);
        backBtn.setFontSize(fontSize);
        levelSubmitBtn.setFontSize(fontSize);
        clearTreeBtn.setFontSize(fontSize);
        preorderBtn.setFontSize(fontSize);
    }
}
