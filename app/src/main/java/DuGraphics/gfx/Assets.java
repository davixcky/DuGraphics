package DuGraphics.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Assets {

    private static ArrayList<BufferedImage> backgroundAssets;
    private static HashMap<String, ArrayList<BufferedImage>> uiComponents;
    private static HashMap<String, Font> fonts;
    private static HashMap<String, ArrayList<BufferedImage>> background;
    private static HashMap<String, BufferedImage> arrow;

    public static void init() {
        backgroundAssets = loadSprites(60, 50, 12, "/textures/bgicons.png");

        arrow = new HashMap<>();
        arrow.put(getUiString(UI_ELEMENTS.ARROW_BUTTON_L), ContentLoader.loadImage("/ui/arrow_1.png"));
        arrow.put(getUiString(UI_ELEMENTS.ARROW_BUTTON_R), ContentLoader.loadImage("/ui/arrow.png"));

        uiComponents = new HashMap<>();
        uiComponents.put(getUiString(UI_ELEMENTS.BUTTONS), loadSprites(105, 21, 4, "/ui/labels.png"));
        uiComponents.put(getUiString(UI_ELEMENTS.BUTTONS_NON_SQUARE), loadSprites(134, 119, 2, "/ui/buttons-hexagons.png"));
        uiComponents.put(getUiString(UI_ELEMENTS.BUTTONSMUTE), loadSprites(50, 50, 2, "/ui/mutebtns.png"));

        SpriteSheet slidersSheet = new SpriteSheet(ContentLoader.loadImage("/ui/sheet-slider.png"));
        ArrayList<BufferedImage> sliders = new ArrayList<>();
        sliders.add(slidersSheet.crop(0, 0, 440, 33));
        sliders.add(slidersSheet.crop(440, 0, 39, 32));
        uiComponents.put(getUiString(UI_ELEMENTS.SLIDER), sliders);

        fonts = new HashMap<>();
        fonts.put(getFontString(Fonts.SLKSCR_100), ContentLoader.loadFont("/fonts/slkscr.ttf", 100));
        fonts.put(getFontString(Fonts.SLKSCR_10), ContentLoader.loadFont("/fonts/slkscr.ttf", 10));
    }

    public static BufferedImage getArrow(UI_ELEMENTS uiElement) {
        return arrow.get(getUiString(uiElement));
    }

    public static Font getFont(Fonts font) {
        return fonts.get(getFontString(font));
    }

    public static Font getFont(FontsName fontName, int size) {
        return ContentLoader.loadFont(getFontName(fontName), size);
    }

    public static ArrayList<BufferedImage> getUiComponents(UI_ELEMENTS uiElement) {
        return uiComponents.get(getUiString(uiElement));
    }

    public static BufferedImage rotate(BufferedImage image, double angle) {
        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, image.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w >> 1, h >> 1);
        graphic.drawImage(image, null, 0, 0);
        graphic.dispose();
        return rotated;
    }

    private static ArrayList<BufferedImage> loadSprites(int width, int height, int total, String filename) {
        SpriteSheet sheet = new SpriteSheet(ContentLoader.loadImage(filename));
        int count = 0;

        ArrayList<BufferedImage> assets = new ArrayList<>();

        for (int j = 0; j < sheet.getHeight() && count < total; j += height) {
            for (int i = 0; i < sheet.getWidth() && count < total; i += width) {
                assets.add(sheet.crop(i, j, width, height));
                count++;
            }
        }

        return assets;
    }

    private static String getFontString(Fonts font) {
        String fontStr = "";

        switch (font) {
            case SLKSCR_100 -> fontStr = "slkscr100";
        }

        return fontStr;
    }

    private static String getFontName(FontsName name) {
        String fontStr = "";

        switch (name) {
            case SLKSCR -> fontStr = "/fonts/slkscr.ttf";
            case SPACE_MISSION -> fontStr = "/fonts/space-mission.otf";
            case SPORT_TYPO -> fontStr = "/fonts/sport-typo.ttf";
            case DEBUG -> fontStr = "/fonts/debug.otf";
            case JOYSTIX -> fontStr = "/fonts/joystix_monospace.ttf";
        }

        return fontStr;
    }

    private static String getUiString(UI_ELEMENTS uiElement) {
        String uiElementStr = "";

        switch (uiElement) {
            case BUTTONS -> uiElementStr = "buttons";
            case SLIDER -> uiElementStr = "slider";
            case BUTTONS_NON_SQUARE -> uiElementStr = "buttonsNonSquare";
            case ARROW_BUTTON_L -> uiElementStr = "arrow_l";
            case ARROW_BUTTON_R -> uiElementStr = "arrow_r";
        }

        return uiElementStr;
    }


    public enum Fonts {
        SLKSCR_100,
        SLKSCR_10,
    }

    public enum FontsName {
        SLKSCR,
        SPACE_MISSION,
        SPORT_TYPO,
        DEBUG,
        JOYSTIX
    }

    public enum UI_ELEMENTS {
        BUTTONS,
        SLIDER,
        BUTTONS_NON_SQUARE,
        BUTTONSMUTE,
        ARROW_BUTTON_L,
        ARROW_BUTTON_R,
    }

}
