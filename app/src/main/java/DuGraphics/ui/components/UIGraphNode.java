package DuGraphics.ui.components;

import DuGraphics.services.data.graph.GraphNode;
import DuGraphics.states.State;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class UIGraphNode extends UIBufferedObject {

    private final GraphNode<String> nodeData;
    private float nextX, nextY;

    private int currentStatus;

    private Color mainColor;

    public UIGraphNode(State parent, float x, float y, int width, int height, GraphNode<String> node) {
        super(parent, x, y, width, height);
        this.nodeData = node;

        nextX = x;
        nextY = y;
        bounds.setBounds(0, 0, width + 30, height + 30);
        currentStatus = 0;
        mainColor = new Color(255, 0, 0, 0);
        init();
    }

    @Override
    protected void create(Graphics2D g2) {
        g2.setColor(mainColor);
        g2.fillOval(0, 0, width, height);
        g2.setColor(Color.white);

        drawString(g2, nodeData.getValue(), width / 2, height / 2, true, Color.white, new Font(Font.SERIF, Font.BOLD, 14));
    }

    @Override
    public void update() {
        if (currentStatus < 254) {
            currentStatus++;
            mainColor = new Color(255, 0, 0, currentStatus);
        }

        if (currentStatus > 254) {
            currentStatus = 0;
        }

        if (x == nextX && y == nextY) return;

        updateCoordsBounds(new Rectangle((int) nextX, (int) nextY, width, height));
    }

    @Override
    public void onClick() {
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
