package it.unibs.pajc.baresi.graphic.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    private ImageUtils() { }

    public static BufferedImage changeBrightness(BufferedImage original, int offset) {
        BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                int  color =  original.getRGB(x,y);

                if (color != 0) {
                    Color c = new Color(color);

                    Color f = new Color(Math.min(c.getRed() + offset, 255),
                            Math.min(c.getGreen() + offset, 255),
                            Math.min(c.getBlue() + offset, 255));

                    result.setRGB(x, y, f.getRGB());
                } else {
                    result.setRGB(x, y, color);
                }
            }
        }

        return result;

    }
}
