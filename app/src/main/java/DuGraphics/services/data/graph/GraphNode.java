package DuGraphics.services.data.graph;

import DuGraphics.services.data.common.Node;

public class GraphNode<T> extends Node<T> {
    private int x, y;

    public GraphNode(T value, int x, int y) {
        super(value);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
