package DuGraphics.states;

import DuGraphics.Handler;

import java.awt.*;

public class GraphState extends State {

    public static final String STATE_NAME = "GRAPH_STATE";

    public GraphState(Handler handler) {
        super(STATE_NAME, handler, "Graph visualizer");
    }

    @Override
    protected void initComponents() {
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
    }


    @Override
    protected void resizeComponents() {
    }
}
