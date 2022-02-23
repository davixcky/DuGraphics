package DuGraphics.ui;

import DuGraphics.Handler;
import DuGraphics.states.State;

public class StaticElements {

    public static UIButton settingsBtn(State parent, Handler handler, float x, float y) {
        UIButton settingsBtn = new UIButton(parent, x, y, UIButton.btnImage, () -> System.out.println("Settings button pressed"));
        settingsBtn.setText("SETTINGS");
        settingsBtn.setHover(UIButton.btnHoverImager, "SETTINGS");

        return settingsBtn;
    }

    public static UIButton exitBtn(State parent, Handler handler, float x, float y) {
        UIButton exitBtn = new UIButton(parent, x, y, UIButton.btnImage, () -> System.out.println("Exit button pressed"));
        exitBtn.setText("EXIT");
        exitBtn.setHover(UIButton.btnHoverImager, "CLOSE APP");

        return exitBtn;
    }

    public static UIButton multiplayerBtn(State parent, Handler handler, float x, float y) {
        UIButton multiplayerBtn = new UIButton(parent, x, y, UIButton.btnImage, () -> System.out.println("Multiplayer button pressed"));
        multiplayerBtn.setText("MULTIPLAYER");
        multiplayerBtn.setHover(UIButton.btnHoverImager, "ONLINE");

        return multiplayerBtn;
    }

}
