package DuGraphics.ui;

import DuGraphics.states.State;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class UIBufferedObject extends UIObject {

    private BufferedImage bufferedObject;

    public UIBufferedObject(State parent, float x, float y, int width, int height) {
        super(parent, x, y, width, height);

        bufferedObject = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedObject.createGraphics();
        create(g2);
        g2.dispose();

    }

    protected abstract void create(Graphics2D graphics2D);

    @Override
    public void render(Graphics g) {
        g.drawImage(bufferedObject, (int) x, (int) y, width, height, null);
    }
}
