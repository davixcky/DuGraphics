/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package DuGraphics;

import DuGraphics.gfx.Assets;
import DuGraphics.gfx.ContentLoader;
import DuGraphics.graphics.Display;
import DuGraphics.graphics.DisplayController;
import DuGraphics.input.KeyManager;
import DuGraphics.input.MouseManager;
import DuGraphics.states.GraphState;
import DuGraphics.states.MainState;
import DuGraphics.states.State;
import DuGraphics.states.TreeState;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class App implements Runnable, DisplayController {

    public static final int TICKSPERS = 120;
    public static final boolean ISFRAMECAPPED = false;
    private final KeyManager keyManager;
    private final MouseManager mouseManager;
    public int ticks;
    private boolean running = false;
    private Thread gameThread;
    private BufferStrategy bs;
    private Graphics g;
    private Display display;
    private Dimension windowSize = new Dimension(1080, 720);
    private Image background;

    public App() {
        Assets.init();

        keyManager = new KeyManager();
        mouseManager = new MouseManager();
    }

    private void init() {
        display = new Display(this, "My app", windowSize);
        display.getFrame().addKeyListener(keyManager);
        display.getGameCanvas().addKeyListener(keyManager);
        display.getFrame().addMouseListener(mouseManager);
        display.getFrame().addMouseMotionListener(mouseManager);
        display.getGameCanvas().addMouseListener(mouseManager);
        display.getGameCanvas().addMouseMotionListener(mouseManager);
        display.getGameCanvas().addMouseWheelListener(mouseManager);
        display.getFrame().addMouseWheelListener(mouseManager);

        initStates();
        changeBackground("/backgrounds/background_12.jpg");

        State.goTo(MainState.STATE_NAME);
    }

    private void initStates() {
        new MainState(new Handler(this));
        new TreeState(new Handler(this));
        new GraphState(new Handler(this));
    }

    public synchronized void start() {
        // Exit if the app is already running
        if (running)
            return;

        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stop() {
        // Exit if the game is not running
        if (!running)
            return;

        running = false;
        try {
            System.exit(0);
            display.close();
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResize(Dimension newDimensions) {
        this.windowSize = newDimensions;

        if (State.getCurrentState() != null)
            State.getCurrentState().updateDimensions(newDimensions);
    }

    private void update() {
        // Set mouse and key listeners
        keyManager.update();

        if (State.getCurrentState() != null) {
            State.getCurrentState().update();
            display.setTitle("DuGraphics | " + State.getCurrentNavigationTitle());
        }

    }

    private void render() {
        bs = display.getGameCanvas().getBufferStrategy();
        if (bs == null) {
            display.getGameCanvas().createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.clearRect(0, 0, windowSize.width, windowSize.height);
        g.drawImage(background, 0, 0, windowSize.width, windowSize.height, null);

        if (State.getCurrentState() != null)
            State.getCurrentState().render(g);

        bs.show();
        g.dispose();
    }


    @Override
    public void run() {
        init();
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / TICKSPERS;
        ticks = 0;
        long fpsTimer = System.currentTimeMillis();
        double delta = 0;
        boolean shouldRender;
        while (running) {
            shouldRender = !ISFRAMECAPPED;
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                ticks++;
                update();
                delta -= 1;
                shouldRender = true;
            }

            if (shouldRender) {
                render();
            }

            if (fpsTimer < System.currentTimeMillis() - 1000) {
                ticks = 0;
                fpsTimer = System.currentTimeMillis();
            }
        }
    }

    public Dimension getWindowSize() {
        return windowSize;
    }

    public void changeBackground(String path) {
        background = new ImageIcon(ContentLoader.loadImage(path)).getImage();
    }

    public MouseManager getMouseManager() {
        return mouseManager;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }
}
