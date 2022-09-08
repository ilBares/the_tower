package it.unibs.pajc.baresi.graphic.asset;

import it.unibs.pajc.baresi.graphic.asset.sprite.SpriteSheet;

import java.io.Serializable;

public abstract class Asset implements Serializable {

    protected int width, height;
    protected int row, column;
    protected int[] pixels;
    protected SpriteSheet sheet;

    protected void load() {
        pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x + y * width] = sheet.getPixels()[(x + column * width) + (y + row * height) * sheet.getWidth()];
            }
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
}
