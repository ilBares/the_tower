package it.unibs.pajc.baresi.graphic.ui;

import java.awt.image.BufferedImage;

public interface UIButtonListener {

    default void entered(UIButton button) {

    }

    default void exited(UIButton button) {

    }

    default void pressed(UIButton button) {
        button.setImage(ImageUtils.changeBrightness(button.getImage(), 30));
    }

    default void released(UIButton button) {
        BufferedImage image = button.getImage();
        image.setRGB(0, 0, image.getWidth(), image.getHeight(), button.getDefaultRGB(), 0, image.getWidth());
    }
}
