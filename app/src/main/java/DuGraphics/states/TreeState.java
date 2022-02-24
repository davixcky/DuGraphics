package DuGraphics.states;

import DuGraphics.Handler;
import DuGraphics.gfx.Assets;
import DuGraphics.services.data.BST;
import DuGraphics.services.data.BSTNode;
import DuGraphics.ui.UIButton;
import DuGraphics.ui.UIInput;
import DuGraphics.ui.UIObject;
import DuGraphics.ui.components.UINode;

import java.awt.*;
import java.util.HashMap;

public class TreeState extends State {

    public static final String STATE_NAME = "TREE_STATE";
    private final BST<Integer> bstData;
    private final HashMap<Integer, UINode> uiNodes;
    private final Dimension rightColumnDimension;
    private UIInput nodeValueInput;
    private UIButton saveNodeBtn;
    private UINode rootNode;

    public TreeState(Handler handler) {
        super(STATE_NAME, handler);

        bstData = new BST<>();
        rightColumnDimension = new Dimension();

        uiNodes = new HashMap<>();
        rootNode = null;
    }

    @Override
    protected void initComponents() {
        nodeValueInput = new UIInput(this, 0, 0);
        nodeValueInput.setListener(this::saveValue);
        nodeValueInput.setCharLimits(1, 3);

        saveNodeBtn = new UIButton(this, 0, 0, UIButton.btnImage, this::saveValue);
        saveNodeBtn.setText("SAVE");

        uiManager.addObjects(nodeValueInput, saveNodeBtn);

        resizeComponents();
    }

    private void saveValue() {
        String literalValue = nodeValueInput.getValue();
        int integerValue;
        try {
            integerValue = Integer.parseInt(literalValue);
        } catch (Exception e) {
            System.out.println(literalValue + " is not a valid integer node value");
            // TODO: Handle literal value when is not a string
            return;
        }

        bstData.insertNode(integerValue);
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
                    x + width / 2 - 40,
                    40,
                    true,
                    Color.white,
                    Assets.getFont(Assets.FontsName.SPACE_MISSION, (int) (currentDimension.height * 0.03f)));
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

    private void paintNode(Graphics2D g2, int x, int y, BSTNode<Integer> node) {
        int DIAMETRO = 30;
        int RADIO = DIAMETRO / 2;
        int ANCHO = 50;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (node != null) {
            int EXTRA = bstData.internal_height(node.getRight()) * ANCHO / 2
                    + bstData.internal_height(node.getLeft()) * ANCHO / 2
                    + node.nodosCompletos(node) * ANCHO / 2;

            UINode uiNode;
            if (!uiNodes.containsKey(node.getValue())) {
                uiNode = new UINode(this, x, y, DIAMETRO, DIAMETRO, node);
                uiNodes.put(node.getValue(), uiNode);
                uiManager.addObject(uiNode);
            } else {
                uiNode = uiNodes.get(node.getValue());
                uiNode.updateCoordsBounds(new Rectangle(x, y, DIAMETRO, DIAMETRO));
            }

            if (rootNode == null) {
                rootNode = uiNode;
            }

            g2.setColor(Color.white);
            if (node.getLeft() != null) {
                g2.drawLine(x + RADIO, y + RADIO, x - ANCHO - EXTRA + RADIO, y + ANCHO + RADIO);
            }
            if (node.getRight() != null) {
                g2.drawLine(x + RADIO, y + RADIO, x + ANCHO + EXTRA + RADIO, y + ANCHO + RADIO);
            }

            paintNode(g2, x - ANCHO - EXTRA, y + ANCHO, node.getLeft());
            paintNode(g2, x + ANCHO + EXTRA, y + ANCHO, node.getRight());
        }
    }

    @Override
    protected void resizeComponents() {
        // Update buttons dimensions if change
        int width = 40 + (int) (currentDimension.width * 0.05f);
        int height = 20 + (int) (currentDimension.height * 0.03f);

        int containerWidth = (int) (currentDimension.width * 0.2f);
        int inputHeight = (int) (currentDimension.height * 0.04f);
        int x = currentDimension.width - containerWidth / 2;
        int y = 80;

        rightColumnDimension.setSize(containerWidth, currentDimension.height);
        nodeValueInput.updateCoordsBounds(new Rectangle(x - width / 2, y, width, inputHeight));
        nodeValueInput.setWidth(width);
        saveNodeBtn.updateCoordsBounds(new Rectangle(x - width / 2, (int) (nodeValueInput.getY() + nodeValueInput.getHeight()), width, height));

        saveNodeBtn.setFontSize((int) (containerWidth * 0.05f));
    }
}
