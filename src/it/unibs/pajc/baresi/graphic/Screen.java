package it.unibs.pajc.baresi.graphic;

import it.unibs.pajc.baresi.controller.Game;
import it.unibs.pajc.baresi.graphic.asset.Asset;
import it.unibs.pajc.baresi.graphic.background.Background;
import it.unibs.pajc.baresi.graphic.background.Layer;
import it.unibs.pajc.baresi.graphic.asset.sprite.Sprite;
import it.unibs.pajc.baresi.graphic.ui.UIButton;
import it.unibs.pajc.baresi.graphic.ui.UIText;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    private int mapOffset;
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

    public void renderAsset(int xOffset, int yOffset, Asset asset) {
        int[] spritePixels = asset.getPixels();
        int color;
        xOffset -= mapOffset;

        for (int x = 0; x < asset.getWidth(); x++) {
            for (int y = 0; y < asset.getHeight(); y++) {
                color = spritePixels[x + y * asset.getWidth()];
                if (color != 0 && (xOffset + x) >= 0 && (xOffset + x) < width)
                    pixels[(x + xOffset) + (yOffset + y - asset.getHeight()) * width] = color;
            }
        }
    }

    public void drawUIText(Graphics2D g2, UIText lbl) {
        g2.setColor(lbl.getColor());
        g2.setFont(lbl.getFont());
        g2.drawString(lbl.getText(), lbl.getX(), lbl.getY());
    }

    public void drawUIButton(Graphics2D g2, UIButton btn) {
        g2.drawImage(btn.getImage(), btn.getX(), btn.getY(), null);
    }
    /*
    public void renderUIComponent(int xOffset, int yOffset, UIButton component) {
        int[] componentPixels = component.getPixels();
        int color;

        for (int x = 0; x < component.getWidth(); x++) {
            for (int y = 0; y < component.getHeight(); y++) {
                color = componentPixels[x + y * component.getWidth()];
                if (color != 0)
                    pixels[(x + xOffset) + (y + yOffset) * width] = color;
            }
        }
    }

     */

    ///
    /// Getters and Setters
    ///
    public void setMapOffset(int mapOffset) {
        this.mapOffset = mapOffset;
    }

    /**
     * @return pixels array
     */
    public int[] getPixels() {
        return pixels;
    }

    /**
     * Fills pixels array with black pixel.
     */
    public void clear() {
        Arrays.fill(pixels, 0x000000);
    }
}
