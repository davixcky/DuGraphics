package DuGraphics.states;

import DuGraphics.Handler;
import DuGraphics.gfx.Assets;
import DuGraphics.ui.StaticElements;
import DuGraphics.ui.UIButton;
import DuGraphics.ui.UIObject;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

public class MainState extends State {

    private int red, green, blue;
    private int opacity = 0;
    private boolean increase = true;
    private final Random random;

    private UIButton multiplayerBtn, settingsBtn, sourceCodeBtn, exitBtn;

    private Dimension titleDimensions;

    public MainState(Handler handler) {
        super(handler);

        random = new Random();
        red = green = blue = 255;
    }

    private int getDynamicX(int width) {
        return (int) (handler.boardDimensions().width * 0.5f - width / 2);
    }

    private int getDynamicY(int height) {
        return handler.boardDimensions().height / 2 + height;
    }

    @Override
    protected void initComponents() {
        // 125 and 21 are the max values for the current sprites
        int x = getDynamicX(105);
        int y = getDynamicY(21);

        multiplayerBtn = StaticElements.multiplayerBtn(this, handler, x, y);
        multiplayerBtn.setSize(new Dimension(105, 40));

        settingsBtn = StaticElements.settingsBtn(this, handler, x, UIButton.getRelativeHeight(multiplayerBtn));

        sourceCodeBtn = new UIButton(this, x, UIObject.getRelativeHeight(settingsBtn), UIButton.btnImage, () -> {
            try {
                Desktop.getDesktop().browse(URI.create("https://github.com/Norte-invaders/MenuSonido"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sourceCodeBtn.setText("CODEBASE");
        sourceCodeBtn.setHover(UIButton.btnHoverImager, "OPEN GITHUB");

        exitBtn = StaticElements.exitBtn(this, handler, x, UIButton.getRelativeHeight(sourceCodeBtn));

        uiManager.addObjects(multiplayerBtn, settingsBtn, sourceCodeBtn, exitBtn);
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
        int y = (int) (handler.boardDimensions().width * 0.2f);

        titleDimensions = UIObject.drawString(g, "AAB VISUALIZER",
                handler.boardDimensions().width / 2,
                y,
                true,
                textColor,
                Assets.getFont(Assets.FontsName.SPACE_MISSION, (int) (handler.boardDimensions().width * 0.1f)));

        UIObject.drawString(g, "Kevin Cueto & David Orozco",
                handler.boardDimensions().width / 2,
                y + (int) (titleDimensions.height * 0.5f),
                true,
                Color.white,
                Assets.getFont(Assets.FontsName.SLKSCR, (int) (titleDimensions.height * 0.4f)));

        uiManager.render(g);
    }

    @Override
    public void resizeComponents() {
        // Update buttons dimensions if change
        int width = 40 + (int) (handler.boardDimensions().width * 0.3f);
        int height = 20 + (int) (handler.boardDimensions().height * 0.03f);

        int x = getDynamicX(width);
        int y = getDynamicY(height);

        multiplayerBtn.updateCoordsBounds(new Rectangle(x, y, width, height));
        settingsBtn.updateCoordsBounds(new Rectangle(x, (int) UIButton.getRelativeHeight(multiplayerBtn), width, height));
        sourceCodeBtn.updateCoordsBounds(new Rectangle(x, (int) UIButton.getRelativeHeight(settingsBtn), width, height));
        exitBtn.updateCoordsBounds(new Rectangle(x, (int) UIButton.getRelativeHeight(sourceCodeBtn), width, height));
    }
}
