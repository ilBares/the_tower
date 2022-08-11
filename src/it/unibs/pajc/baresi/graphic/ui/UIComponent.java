package it.unibs.pajc.baresi.graphic.ui;

import it.unibs.pajc.baresi.graphic.Screen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UIComponent {
    private int width, height;
    private int[] pixels;
    private String path;
    // public Vector2i position

    public static UIComponent brnBack = new UIComponent("/buttons/button_back.png");
    public static UIComponent btnMiniGolem = new UIComponent("/buttons/button_mini_golem.png");
    public static UIComponent btnAdventurer = new UIComponent("/buttons/button_adventurer.png");
    public static UIComponent btnDragon = new UIComponent("/buttons/button_dragon.png");
    public static UIComponent btnGolem = new UIComponent("/buttons/button_golem.png");

    public UIComponent(String path) {
        this.path = path;
        load();
    }

    private void load() {
        try {
            BufferedImage image = ImageIO.read(UIComponent.class.getResource(path));

            width = image.getWidth();
            height = image.getHeight();

            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void update() {

    }

    public void render(int x, int y, Screen screen) {
        screen.renderUIComponent(x, y, this);
    }
}
