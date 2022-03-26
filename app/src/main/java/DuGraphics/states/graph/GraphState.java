package DuGraphics.states.graph;

import DuGraphics.Handler;
import DuGraphics.services.data.LinkedList.LinkedList;
import DuGraphics.services.data.graph.GraphNode;
import DuGraphics.services.data.parsing.GraphParser;
import DuGraphics.states.State;
import DuGraphics.states.main.MainState;
import DuGraphics.ui.StaticElements;
import DuGraphics.ui.UIButton;
import DuGraphics.ui.components.UIGraphNode;
import DuGraphics.ui.components.wrapper.fileChooser.IFileController;
import DuGraphics.ui.components.wrapper.fileChooser.UIFileChooser;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

public class GraphState extends State {

    public static final String STATE_NAME = "GRAPH_STATE";

    private final Dimension rightColumnDimension;
    private UIButton
            backBtn,
            fileChooserBtn;

    private final HashMap<String, UIGraphNode> uiNodes;

    private final UIFileChooser fileChooser;
    private final GraphParser graphParser;

    public GraphState(Handler handler) {
        super(STATE_NAME, handler, "Graph visualizer");

        rightColumnDimension = new Dimension();
        fileChooser = new UIFileChooser();
        graphParser = new GraphParser();
        uiNodes = new HashMap<>();
    }

    private void createRect(Graphics2D g2) {
        int width = (int) (currentDimension.width * 0.2f);
        int x = currentDimension.width - width;

        g2.setColor(Color.red);
        g2.fillRect(x, 0, width, currentDimension.height);
    }

    @Override
    protected void initComponents() {
        fileChooserBtn = new UIButton(this, this::fileChooserController);
        fileChooserBtn.setText("CHOOSE FILE");

        backBtn = StaticElements.backBtn(this, MainState.STATE_NAME);

        uiManager.addObjects(fileChooserBtn, backBtn);
    }

    private void fileChooserController() {
        fileChooser.launchDialog(new IFileController() {
            @Override
            public void onSuccess(File f) {
                System.out.println("File: " + f.getAbsolutePath());

                graphParser.reloadFile(f);
            }

            @Override
            public void onCancel() {
                // TODO: Handle file cancellation
                System.out.println("File not selected");
            }
        });
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

        if (graphParser != null && graphParser.isGraphLoaded()) {
            drawGraph((Graphics2D) g, 220, 220);
        }

        backBtn.render(g);

        if (currentDimension.width > 729) {
            createRect((Graphics2D) g);

            uiManager.render(g);
        }
    }

    private void drawGraph(Graphics2D g2, int x, int y) {
        LinkedList<GraphNode<String>> positionalNodes = graphParser.getPositionalNodes();
        for (GraphNode<String> positionalNode : positionalNodes) {
            UIGraphNode uiNode;
            if (!uiNodes.containsKey(positionalNode.getValue())) {
                uiNode = new UIGraphNode(this, x, y, 40, 40, positionalNode);
                uiNodes.put(positionalNode.getValue(), uiNode);
            } else {
                uiNode = uiNodes.get(positionalNode.getValue());
                uiNode.updateCoordsBounds(new Rectangle((int) uiNode.getX(), (int) uiNode.getY(), 40, 40));
            }

            x += 40;
            y += 50;
            uiManager.addObject(uiNode);
        }

        g2.setStroke(new BasicStroke(3));

        int[][] data = graphParser.getGraph().toArray();
        for (int i = 0; i < positionalNodes.size(); i++) {
            GraphNode<String> positionalNode = positionalNodes.get(i);
            UIGraphNode startNode = uiNodes.get(positionalNode.getValue());
            for (int j = 0; j < positionalNodes.size(); j++) {
                if (data[i][j] == 0) continue;

                if (data[i][j] == 2) {
                    g2.drawOval((int) startNode.getX() + 30, (int) startNode.getY(), 30, 30);
                    continue;
                }

                UIGraphNode endNode = uiNodes.get(positionalNodes.get(j).getValue());
                g2.drawLine((int) startNode.getX() + 20, (int) startNode.getY() + 20, (int) endNode.getX() + 20, (int) endNode.getY() + 20);
                g2.fillRect((int) endNode.getX(), (int) endNode.getY(), 10, 10);
            }
        }
    }


    @Override
    protected void resizeComponents() {
        int height = 20 + (int) (currentDimension.height * 0.03f);

        int containerWidth = (int) (currentDimension.width * 0.2f);
        int inputHeight = (int) (currentDimension.height * 0.04f);

        int width = (int) (containerWidth * 0.8f);
        int y = 60;

        int spacingX = (int) (containerWidth * 0.1f);
        int initialX = currentDimension.width - containerWidth + spacingX;

        rightColumnDimension.setSize(containerWidth, currentDimension.height);
        fileChooserBtn.updateCoordsBounds(new Rectangle(initialX, y, width, height));
        backBtn.updateCoordsBounds(new Rectangle(20, 20, width, height));

        int fontSize = (int) (containerWidth * 0.05f);
        backBtn.setFontSize(fontSize);
        fileChooserBtn.setFontSize(fontSize);
    }
}
