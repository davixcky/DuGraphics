package DuGraphics.states;

import DuGraphics.Handler;
import DuGraphics.gfx.Assets;
import DuGraphics.states.graph.GraphState;
import DuGraphics.ui.StaticElements;
import DuGraphics.ui.UIButton;
import DuGraphics.ui.UIObject;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

public class MainState extends State {

    private final Random random;
    private int red, green, blue;
    private int opacity = 0;
    private boolean increase = true;
    private UIButton treeVisualizerBtn, graphVisualizerBtn, sourceCodeBtn, exitBtn;

    private Dimension titleDimensions;

    public static final String STATE_NAME = "MAIN_STATE";

    public MainState(Handler handler) {
        super(STATE_NAME, handler, "Home");

        random = new Random();

        red = random.nextInt(256);
        green = random.nextInt(256);
        blue = random.nextInt(256);
    }

    private int getDynamicX(int width) {
        return (int) (currentDimension.width * 0.5f - width / 2);
    }

    private int getDynamicY(int height) {
        return currentDimension.height / 2 + height;
    }

    @Override
    protected void initComponents() {
        treeVisualizerBtn = new UIButton(this, 0, 0, UIButton.btnImage, () -> {
            State.goTo(TreeState.STATE_NAME);
            State.getCurrentState().updateDimensions(currentDimension);
        });
        treeVisualizerBtn.setText("TREE VISUALIZER");

        graphVisualizerBtn = new UIButton(this, 0, 0, UIButton.btnImage, () -> {
           State.goTo(GraphState.STATE_NAME);
           State.getCurrentState().updateDimensions(currentDimension);
        });
        graphVisualizerBtn.setText("GRAPH VISUALIZER");

        exitBtn = StaticElements.exitBtn(this, handler, 0, 0);
        sourceCodeBtn = new UIButton(this, 0, 0, UIButton.btnImage, () -> {
            try {
                Desktop.getDesktop().browse(URI.create("https://github.com/davixcky/DuGraphics"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sourceCodeBtn.setText("CODEBASE");

        uiManager.addObjects(treeVisualizerBtn, graphVisualizerBtn, sourceCodeBtn, exitBtn);

        resizeComponents();
    }

    @Override
    public void update() {
        opacity += increase ? 1 : -1;
        if (opacity >= 255) {
            increase = false;
            red = random.nextInt(256);
            green = random.nextInt(256);
            blue = random.nextInt(256);
            opacity = 240;
        } else if (opacity <= 0) {
            increase = true;
            red = random.nextInt(256);
            green = random.nextInt(256);
            blue = random.nextInt(256);
            opacity = 10;
        }
    }

    @Override
    public void render(Graphics g) {
        Color textColor = new Color(red, green, blue, opacity);
        int y = (int) (currentDimension.width * 0.2f);

        titleDimensions = UIObject.drawString(g, "AAB VISUALIZER",
                currentDimension.width / 2,
                y,
                true,
                textColor,
                Assets.getFont(Assets.FontsName.SPACE_MISSION, (int) (currentDimension.width * 0.09f)));

        UIObject.drawString(g, "Developed by David Orozco",
                currentDimension.width / 2,
                y + (int) (titleDimensions.height * 0.5f),
                true,
                Color.white,
                Assets.getFont(Assets.FontsName.SLKSCR, (int) (titleDimensions.width * 0.04f)));

        uiManager.render(g);
    }

    @Override
    protected void resizeComponents() {
        // Update buttons dimensions if change
        int width = 40 + (int) (currentDimension.width * 0.3f);
        int height = 20 + (int) (currentDimension.height * 0.03f);

        int x = getDynamicX(width);
        int y = getDynamicY(height);

        treeVisualizerBtn.updateCoordsBounds(new Rectangle(x, y, width, height));
        graphVisualizerBtn.updateCoordsBounds(new Rectangle(x, (int) UIButton.getRelativeHeight(treeVisualizerBtn), width, height));
        sourceCodeBtn.updateCoordsBounds(new Rectangle(x, (int) UIButton.getRelativeHeight(graphVisualizerBtn), width, height));
        exitBtn.updateCoordsBounds(new Rectangle(x, (int) UIButton.getRelativeHeight(sourceCodeBtn), width, height));
    }
}
