package DuGraphics.states;


import DuGraphics.Handler;
import DuGraphics.ui.UIManager;

import java.awt.*;

public abstract class State {

    private static State currentState = null;

    protected Handler handler;
    protected UIManager uiManager;

    public State(Handler handler) {
        this.handler = handler;

        uiManager = new UIManager();
    }

    public static void setCurrentState(State state) {
        if (currentState != null)
            currentState.stop();

        currentState = state;
        state.setUiManager();
        state.start();
    }

    protected void setUiManager() {
        handler.getApp().getMouseManager().setUIManager(uiManager);
        handler.getApp().getKeyManager().setUIManager(uiManager);
    }

    public UIManager getUiManager() {
        return uiManager;
    }

    public static State getCurrentState() {
        return currentState;
    }

    protected void start() {
        initComponents();
    }

    protected void stop() {
        if (currentState != null)
            System.out.println("Stopping " + currentState);
    }

    protected abstract void initComponents();
    public abstract void update();
    public abstract void render(Graphics g);
}
