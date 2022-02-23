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
        exitBtn.setHover(UIButton.btnHoverImager, "CLOSE GAME");

        return exitBtn;
    }

    public static UIButton multiplayerBtn(State parent, Handler handler, float x, float y) {
        UIButton multiplayerBtn = new UIButton(parent, x, y, UIButton.btnImage, () -> System.out.println("Multiplayer button pressed"));
        multiplayerBtn.setText("MULTIPLAYER");
        multiplayerBtn.setHover(UIButton.btnHoverImager, "ONLINE");

        return multiplayerBtn;
    }

//    public static UIButton newGameBtn(State parent, Handler handler, float x, float y) {
//        UIButton newGameBtn = new UIButton(parent, x, y, UIButton.btnImage, () -> {
//            ((GameState) handler.getGame().gameSate).reset();
//            try {
//                handler.getGame().setVolume(0,handler.getGame().lastVolume);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            ((SingleplayerState) handler.getGame().singlePlayerState).resetLevel();
//            State.setCurrentState(handler.getGame().gameSate);
//        });
//        newGameBtn.setText("NEW GAME");
//        newGameBtn.setHover(UIButton.btnHoverImager, "START GAME");
//        newGameBtn.setSize(new Dimension(105, 40));
//
//        return newGameBtn;
//    }
//
//    public static UIButton menuBtn(State parent, Handler handler, float x, float y) {
//        UIButton menuBtn = new UIButton(parent, x, y, UIButton.btnImage, () -> {
//            State.setCurrentState(handler.getGame().mainState);
//            handler.getGame().setVolume(0,handler.getGame().lastVolume);
//        });
//        menuBtn.setText("MENU");
//        menuBtn.setHover(UIButton.btnHoverImager, "GO TO MAIN");
//
//        return menuBtn;
//    }


}
