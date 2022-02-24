package DuGraphics.ui.components;

import DuGraphics.services.data.BSTNode;
import DuGraphics.states.State;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class UINode extends UIBufferedObject {

    private final BSTNode nodeData;
    private float nextX, nextY;

    public UINode(State parent, float x, float y, int width, int height, BSTNode node) {
        super(parent, x, y, width, height);
        this.nodeData = node;

        nextX = x;
        nextY = y;
        bounds.setBounds(0, 0, width + 30, height + 30);
        init();
    }

    @Override
    protected void create(Graphics2D g2) {
        g2.setColor(Color.red);
        g2.fillOval(0, 0, width, height);
        g2.setColor(Color.white);
        drawString(g2, nodeData.getValue() + "", 12, 18, false, Color.white, new Font(Font.SERIF, Font.BOLD, 14));
    }

    @Override
    public void update() {
        if (x == nextX && y == nextY) return;

        updateCoordsBounds(new Rectangle((int) nextX, (int) nextY, width, height));
    }

    @Override
    public void onClick() {
        System.out.println("Node clicked");
    }

    @Override
    public void onMouseChanged(MouseEvent e) {

    }

    @Override
    public void onObjectDragged(MouseEvent e) {
        nextX = e.getX();
        nextY = e.getY();
    }

    @Override
    public void onObjectKeyPressed(KeyEvent e) {
    }
}
