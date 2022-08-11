package it.unibs.pajc.baresi.graphic.sprite;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteSheet {
    private int width, height;
    private String path;
    private int[] pixels;

    // It is necessary to add res to source folder:
    // go to File -> Project Structure -> Modules -> press Res and Mark as modules
    // 10 sprites per row, 4 different types of sprites
    public static SpriteSheet dragonSheet = new SpriteSheet("/sheet/dragon_sheet.png");
    public static SpriteSheet golemSheet = new SpriteSheet("/sheet/golem_sheet.png");
    public static SpriteSheet adventureSheet = new SpriteSheet("/sheet/adventurer_sheet.png");
    public static SpriteSheet skeletonSheet = new SpriteSheet("/sheet/skeleton_sheet.png");
    public static SpriteSheet ghoulSheet = new SpriteSheet("/sheet/ghoul_sheet.png");
    public static SpriteSheet miniGolemSheet = new SpriteSheet("/sheet/mini_golem_sheet.png");

    public SpriteSheet(String path) {
        this.path = path;
        load();
    }

    private void load() {
        try {
            BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getWidth() {
        return width;
    }

    public int[] getPixels() {
        return pixels;
    }
}
