package DuGraphics.states;

import DuGraphics.Handler;
import DuGraphics.services.data.graph.Graph;
import DuGraphics.services.data.parsing.GraphParser;
import DuGraphics.ui.UIButton;
import DuGraphics.ui.components.wrapper.fileChooser.IFileController;
import DuGraphics.ui.components.wrapper.fileChooser.UIFileChooser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GraphState extends State {

    public static final String STATE_NAME = "GRAPH_STATE";

    private final Dimension rightColumnDimension;
    private UIButton
            fileChooserBtn;

    private UIFileChooser fileChooser;
    private GraphParser graphParser;

    public GraphState(Handler handler) {
        super(STATE_NAME, handler, "Graph visualizer");

        rightColumnDimension = new Dimension();
        fileChooser = new UIFileChooser();
    }

    private void createRect(Graphics2D g2) {
        int width = (int) (currentDimension.width * 0.2f);
        int x = currentDimension.width - width;

        g2.setColor(Color.red);
        g2.fillRect(x, 0, width, currentDimension.height);
    }

    @Override
    protected void initComponents() {
        fileChooserBtn = new UIButton(this, () -> fileChooser.launchDialog(new IFileController() {
            @Override
            public void onSuccess(File f) {
                System.out.println("File: " + f.getAbsolutePath());

                graphParser = new GraphParser(f);
            }

            @Override
            public void onCancel() {
                System.out.println("Dialog was cancelled");
            }
        }));
        fileChooserBtn.setText("CHOOSE FILE");

        uiManager.addObjects(fileChooserBtn);
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

        if (currentDimension.width > 729) {
            createRect((Graphics2D) g);

            uiManager.render(g);
        }
    }


    @Override
    protected void resizeComponents() {
        int containerWidth = (int) (currentDimension.width * 0.2f);
        rightColumnDimension.setSize(containerWidth, currentDimension.height);
    }
}
