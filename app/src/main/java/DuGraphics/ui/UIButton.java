package DuGraphics.ui;

import DuGraphics.gfx.Assets;
import DuGraphics.states.State;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class UIButton extends UIObject {

    private static final ArrayList<BufferedImage> buttonsAssets = Assets.getUiComponents(Assets.UI_ELEMENTS.BUTTONS);
    public static BufferedImage btnImage = buttonsAssets.get(0);
    public static BufferedImage btnHoverImage = buttonsAssets.get(3);
    private final ArrayList<BufferedImage> images;
    private final ActionListener clicker;
    private String text = null;
    private String hoverText = null;
    private boolean isCustomSize = false;
    private Dimension size;

    public UIButton(State parent, float x, float y, int width, int height, ArrayList<BufferedImage> images, ActionListener clicker) {
        super(parent, x, y, width, height);
        this.images = images;
        this.clicker = clicker;
        isCustomSize = true;
        size = new Dimension(width, height);
    }

    public UIButton(State parent, float x, float y, BufferedImage image, ActionListener clicker) {
        super(parent, x, y, image.getWidth(), image.getHeight());

        images = new ArrayList<>();
        images.add(UIButton.btnHoverImage);
        images.add(image);

        this.width = image.getWidth();
        this.height = image.getHeight();

        this.clicker = clicker;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        int currentImageIndex = hovering ? 0 : 1;
        BufferedImage currentImage = images.get(currentImageIndex);
        String currentText = hovering ? hoverText : text;

        Dimension currentDimension = new Dimension(width, height);

        int textX = (int) (x + currentDimension.getWidth() / 2);
        int textY = (int) (y + currentDimension.getHeight() / 2);

        g.drawImage(currentImage, (int) x, (int) y, (int) currentDimension.getWidth(), (int) currentDimension.getHeight(), null);

        if (text != null)
            drawString(g, currentText, textX, textY, true, Color.white, new Font(Font.SANS_SERIF, Font.PLAIN, (int) (currentDimension.width * 0.05f)));

    }

    @Override
    public void onClick() {
        clicker.actionPerformed();
    }

    @Override
    public void onMouseChanged(MouseEvent e) {

    }

    @Override
    public void onObjectDragged(MouseEvent e) {

    }

    @Override
    public void onObjectKeyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());
    }

    public void setHoverImage(BufferedImage hoverImage) {
        images.add(0, hoverImage);
    }

    public void setText(String text) {
        this.text = text;

        if (hoverText == null)
            hoverText = text;
    }

    public void setHoverText(String hoverText) {
        this.hoverText = hoverText;

        if (text == null)
            text = hoverText;
    }

    public void setSize(Dimension size) {
        this.size = size;
        isCustomSize = true;

        bounds.width = size.width;
        bounds.height = size.height;

        this.width = size.width;
        this.height = size.height;
    }

    public void setImage(BufferedImage image) {
        this.images.add(0, image);
    }

}
