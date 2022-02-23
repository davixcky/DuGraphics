package DuGraphics.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Display {

    private JFrame frame;
    private Canvas gameCanvas;

    private String title;
    private Dimension windowSize;

    private DisplayController controller;

    public Display(DisplayController controller, String title, Dimension windowSize) {
        this.title = title;
        this.windowSize = windowSize;
        this.controller = controller;

        createDisplay();
    }

    private void createDisplay() {
        createFrame();
        createCanvas();
    }

    private void createFrame() {
        frame = new JFrame(title);
        frame.setSize(windowSize);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component)evt.getSource();
                System.out.printf("w: %d, h: %d\n", c.getBounds().width, c.getBounds().height);
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                controller.stop();
                System.exit(0);
            }
        });
    }

    private void createCanvas() {
        gameCanvas = new Canvas();
        gameCanvas.setPreferredSize(windowSize);
        gameCanvas.setMaximumSize(windowSize);
        gameCanvas.setMinimumSize(windowSize);
        gameCanvas.setFocusable(true);

        frame.add(gameCanvas);
        frame.pack();
    }

    public void addKeyListener(KeyListener l) {
        frame.addKeyListener(l);
    }

    public void addMouseListener(MouseListener l) {
        frame.addMouseListener(l);
        gameCanvas.addMouseListener(l);
    }

    public void addMouseMotionListener(MouseMotionListener l) {
        frame.addMouseMotionListener(l);
        gameCanvas.addMouseMotionListener(l);
    }

    public JFrame getFrame() {
        return frame;
    }

    public Canvas getGameCanvas() {
        return gameCanvas;
    }

    public void close() {
        frame.dispose();
    }
}
