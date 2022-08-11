package it.unibs.pajc.baresi;

import it.unibs.pajc.baresi.controller.Game;

import java.awt.*;

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

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Rectangle bounds = gd.getDefaultConfiguration().getBounds();

        double scale = Math.min(bounds.getWidth() / width, bounds.getHeight() / height);
        String title = "The Tower";

        // initializing the game
        Game game = new Game(width, height, scale, title);
        game.initialize();

        // starting the game
        game.start();
    }
}
