package it.unibs.pajc.baresi.graphic.ui;

import java.awt.image.BufferedImage;

/**
 * UIButton Listener.
 */
public interface UIButtonListener {

    default void entered(UIButton button) {

    }

    default void exited(UIButton button) {

    }

    /**
     * Action to do in case of button pressed.
     *
     * @param button    UIButton
     */
    default void pressed(UIButton button) {
        button.setImage(ImageUtils.changeBrightness(button.getImage(), 30));
    }

    /**
     * Action to do in case
     * @param button    UIButton
     */
    default void released(UIButton button) {
        BufferedImage image = button.getImage();
        image.setRGB(0, 0, image.getWidth(), image.getHeight(), button.getDefaultRGB(), 0, image.getWidth());
    }
}
