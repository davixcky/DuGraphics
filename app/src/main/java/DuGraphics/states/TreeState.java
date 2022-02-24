package DuGraphics.states;

import DuGraphics.Handler;
import DuGraphics.gfx.Assets;
import DuGraphics.services.data.BST;
import DuGraphics.ui.UIButton;
import DuGraphics.ui.UIInput;
import DuGraphics.ui.UIObject;

import java.awt.*;

public class TreeState extends State {

    public static final String STATE_NAME = "TREE_STATE";

    private UIInput nodeValueInput;
    private UIButton saveNodeBtn;

    private BST<Integer> bstData;

    public TreeState(Handler handler) {
        super(STATE_NAME, handler);

        bstData = new BST<>();
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

    }

    @Override
    public void render(Graphics g) {
        if (currentDimension.width <= 729) return;

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

    @Override
    protected void resizeComponents() {
        // Update buttons dimensions if change
        int width = 40 + (int) (currentDimension.width * 0.05f);
        int height = 20 + (int) (currentDimension.height * 0.03f);

        int containerWidth = (int) (currentDimension.width * 0.2f);
        int inputHeight = (int) (currentDimension.height * 0.04f);
        int x = currentDimension.width - containerWidth / 2;
        int y = 80;

        nodeValueInput.updateCoordsBounds(new Rectangle(x - width / 2, y, width, inputHeight));
        nodeValueInput.setWidth(width);
        saveNodeBtn.updateCoordsBounds(new Rectangle(x - width / 2, (int) (nodeValueInput.getY() + nodeValueInput.getHeight()), width, height));

        saveNodeBtn.setFontSize((int) (containerWidth * 0.05f));
    }
}
