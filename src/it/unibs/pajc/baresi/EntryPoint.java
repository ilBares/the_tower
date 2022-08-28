package it.unibs.pajc.baresi;

import it.unibs.pajc.baresi.controller.Game;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Entry Point of the Java project.
 * Pajc academic year 2021|22.
 *
 * @author Baresi Marco
 * @version 1.0
 */
public class EntryPoint {
    public static final String FONT_PATH = "res/font/minecraft.ttf";
    public static final String GAME_TITLE = "THE T0WER";

    /**
     * main method that launches our games
     */
    public static void main(String[] args) {
        int height = 360;
        int width = height / 9 * 16;

        ///
        /// setting graphics settings
        ///
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Rectangle bounds = gd.getDefaultConfiguration().getBounds();

        try {
            // font used in the game
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_PATH));
            ge.registerFont(font);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        double scale = Math.min(bounds.getWidth() / width, (bounds.getHeight() - 50) / height);

        // initializing the game
        Game game = new Game(width, height, scale, GAME_TITLE);
        game.initialize();

        // starting the game
        game.start();
    }
}
