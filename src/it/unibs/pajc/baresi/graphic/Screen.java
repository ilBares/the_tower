package it.unibs.pajc.baresi.graphic;

import it.unibs.pajc.baresi.controller.Game;
import it.unibs.pajc.baresi.graphic.background.Background;
import it.unibs.pajc.baresi.graphic.background.Layer;

import java.util.Arrays;

/**
 * Class used to render all game components filling {@code pixels} array
 * used by {@link Game} class.
 * Render methods are used by different classes, such as {@link Background},
 * to render the relative component.
 *
 * @see Game
 */
public class Screen {
    private int width, height;
    private int[] pixels;

    //
    // Constructor
    //
    /**
     * Constructor of Screen class.
     *
     * @param width     width of the window
     * @param height    height of the window
     */
    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }

    //
    // Renderers
    //
    /**
     * Renders a single layer on the screen.
     *
     * @param xOffset   indicates the x offset of the layer
     * @param layer     indicates a single layer of the background
     */
    public void renderLayer(int xOffset, Layer layer) {
        int[] layerPixels = layer.getPixels();
        int color;

        for (int x = 0; x < Math.min(width, layer.getWidth() - xOffset); x++) {
            for (int y = 0; y < Math.min(height, layer.getHeight()); y++) {
                // sets the pixel only if it was empty
                if (pixels[x + y * width] == 0) {
                    color = layerPixels[(x + xOffset) + y * layer.getWidth()];
                    if (color != 0) pixels[x + y * width] = color;
                }
            }
        }
    }

    /**
     * Fills pixels array with black pixel.
     */
    public void clear() {
        Arrays.fill(pixels, 0x000000);
    }

    //
    // Getters
    //
    /**
     * @return width of the screen
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return height of the screen
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return pixels array
     */
    public int[] getPixels() {
        return pixels;
    }

}
