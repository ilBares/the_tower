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
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/minecraft.ttf"));
            ge.registerFont(font);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        double scale = Math.min(Math.min(bounds.getWidth() / width, bounds.getHeight() / height), 2.5);
        String title = "The Tower";

        System.out.println(scale);

        // initializing the game
        Game game = new Game(width, height, scale, title);
        game.initialize();

        // starting the game
        game.start();
    }
}
