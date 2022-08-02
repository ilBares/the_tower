package it.unibs.pajc.baresi.view.background;

import it.unibs.pajc.baresi.view.Screen;

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
    private int counter;

    public Layer(String[] paths) {
        this.paths = paths;
        index = 0;
        counter = 0;
        load();
    }

    private void load() {
        pixels = new int[paths.length][];
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
        index = (++counter / (100 / paths.length)) % paths.length;
        counter %= 100;
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
