package it.unibs.pajc.baresi.graphic;

import it.unibs.pajc.baresi.controller.Game;
import it.unibs.pajc.baresi.graphic.ui.UIMenu;
import it.unibs.pajc.baresi.input.Keyboard;

import java.awt.*;

/**
 * Home class used to handle Menu of the game.
 *
 * @see UIMenu
 */
public class Home {

    private final String TITLE = "THE T0WER";
    private final String[] HOME_OPTIONS = new String[] {
            "SINGLE PLAYER",
            "MULTI PLAYER",
            "QUIT"
    };
    private final String[] PAUSE_OPTIONS = new String[] {
            "RESUME",
            "QUIT"
    };
    private final String[] END_OPTIONS = new String[] {
            "QUIT"
    };
    private UIMenu menu;


    ///
    /// Constructor
    ///

    /**
     * @param screenWidth   width of the screen
     * @param screenHeight  height of the screen
     */
    public Home(int screenWidth, int screenHeight) {
        menu = new UIMenu(screenWidth, screenHeight, TITLE, HOME_OPTIONS);
    }

    ///
    /// Updating and Rendering
    ///

    /**
     * Updates the menu.
     *
     * @param key   keyboard listener
     * @param state of the game
     * @return      option number (QUIT is always 0)
     */
    public int update(Keyboard key, Game.State state) {

        switch (state) {
            case HOME -> menu.setOptions(HOME_OPTIONS);
            case WIN -> {
                menu.setOptions(END_OPTIONS);
                menu.setTitle("WIN");
            }
            case GAME_OVER -> {
                menu.setOptions(END_OPTIONS);
                menu.setTitle("GAME OVER");
            }
            case PAUSE -> menu.setOptions(PAUSE_OPTIONS);
        }

        return menu.update(key);
    }

    /**
     * Renders the {@code UIMenu} menu.
     *
     * @param g2        graphics 2d
     * @param screen    screen used to render
     */
    public void render(Graphics2D g2, Screen screen) {
        menu.renderMenu(g2, screen);
    }

}
