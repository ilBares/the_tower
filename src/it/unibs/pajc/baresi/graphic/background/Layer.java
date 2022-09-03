package it.unibs.pajc.baresi.graphic.background;

import it.unibs.pajc.baresi.graphic.Screen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 */
public class Layer {
    private int width, height;
    private String[] paths;
    private int[][] pixels;
    private int index;
    private int anim;

    public Layer(String[] paths) {
        this.paths = paths;
        index = 0;
        anim = 0;
        pixels = new int[paths.length][];
        load();
    }

    private void load() {
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            try {
                BufferedImage image = ImageIO.read(Layer.class.getResource(path));

                width = image.getWidth();
                height = image.getHeight();

                pixels[i] = new int[width * height];
                image.getRGB(0, 0, width, height, pixels[i], 0, width);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {
        if (paths.length > 1) {
            index = (++anim / (100 / paths.length)) % paths.length;
            anim %= 100;
        }
    }

    public void render(int xOffset, Screen screen) {
        screen.renderLayer(xOffset, this);
    }

    //
    // Getters
    //
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getPixels() {
        return pixels[index];
    }
}
