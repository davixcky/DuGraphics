package DuGraphics.states;

import DuGraphics.Handler;
import DuGraphics.gfx.Assets;
import DuGraphics.services.data.BST;
import DuGraphics.services.data.BSTNode;
import DuGraphics.services.data.LinkedList;
import DuGraphics.services.data.ListNode;
import DuGraphics.ui.UIButton;
import DuGraphics.ui.UIInput;
import DuGraphics.ui.UIObject;
import DuGraphics.ui.components.UIBox;
import DuGraphics.ui.components.UINode;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TreeState extends State {

    public static final String STATE_NAME = "TREE_STATE";

    private final BST<Integer> bstData;
    private final HashMap<Integer, UINode> uiNodes;
    private final LinkedList<Integer> listLevel;

    private final Dimension rightColumnDimension;
    private UIInput nodeValueInput, levelInput;
    private UIButton saveNodeBtn, levelSubmitBtn, backBtn;
    private UINode rootNode;

    private final ArrayList<UIBox> levelNodes;

    public TreeState(Handler handler) {
        super(STATE_NAME, handler);

        bstData = new BST<>();
        rightColumnDimension = new Dimension();

        uiNodes = new HashMap<>();
        listLevel = new LinkedList<>();
        levelNodes = new ArrayList<>();
        rootNode = null;
    }

    @Override
    protected void initComponents() {
        nodeValueInput = new UIInput(this, 0, 0);
        nodeValueInput.setListener(this::saveValue);
        nodeValueInput.setCharLimits(1, 3);

        saveNodeBtn = new UIButton(this, 0, 0, UIButton.btnImage, this::saveValue);
        saveNodeBtn.setText("SAVE");

        levelInput = new UIInput(this, 0, 0);
        levelInput.setListener(this::handleLevel);
        levelInput.setCharLimits(1, 2);

        levelSubmitBtn = new UIButton(this, 0, 0, UIButton.btnImage, this::handleLevel);
        levelSubmitBtn.setText("SEARCH LEVEL");

        backBtn = new UIButton(this, 30, 30, UIButton.btnImage, () -> State.goTo(MainState.STATE_NAME));
        backBtn.setText("BACK TO HOME");
        backBtn.updateCoordsBounds(new Rectangle(20, 20, backBtn.getWidth() + 30, backBtn.getHeight() + 10));

        uiManager.addObjects(nodeValueInput, saveNodeBtn, levelInput, levelSubmitBtn, backBtn);

        resizeComponents();
    }

    private void handleLevel() {
        int level = levelInput.getValueAsInteger();

        levelNodes.clear();
        listLevel.reset();

        bstData.level(level, (node) -> listLevel.insert(node.getValue()));

        int x = 20;
        int y = (int) (currentDimension.height * 0.6f);

        for (ListNode<Integer> integerListNode : listLevel) {
            UIBox box = new UIBox(this, x, y, 30, 30, Color.blue, String.valueOf(integerListNode.getValue()));
            levelNodes.add(box);
            x += 50;
        }
    }

    private void saveValue() {
        bstData.insertNode(nodeValueInput.getValueAsInteger());
        bstData.preorder();
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
                    (int) (rightColumnDimension.height * 0.23f),
                    true,
                    Color.white,
                    Assets.getFont(Assets.FontsName.SPACE_MISSION, (int) (rightColumnDimension.width * 0.08f)));

            uiManager.render(g);

            levelNodes.forEach(node -> node.render(g));
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

    private void paintNode(Graphics2D g2, int x, int y, BSTNode<Integer> node) {
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
                uiManager.addObject(uiNode);
            } else {
                uiNode = uiNodes.get(node.getValue());
                uiNode.updateCoordsBounds(new Rectangle(x, y, DIAMETER, DIAMETER));
            }

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

        y = (int) (rightColumnDimension.height * 0.25f);
        levelInput.updateCoordsBounds(new Rectangle(initialX, y, width, inputHeight));
        levelSubmitBtn.updateCoordsBounds(new Rectangle(initialX, (int) (levelInput.getY() + levelInput.getHeight()), width, height));

        saveNodeBtn.setFontSize((int) (containerWidth * 0.05f));
        backBtn.setFontSize((int) (containerWidth * 0.05f));
        levelSubmitBtn.setFontSize((int) (containerWidth * 0.05f));

    }
}
