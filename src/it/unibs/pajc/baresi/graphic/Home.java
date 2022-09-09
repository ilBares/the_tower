package it.unibs.pajc.baresi.graphic;

import it.unibs.pajc.baresi.controller.Game;
import it.unibs.pajc.baresi.graphic.ui.UIMenu;
import it.unibs.pajc.baresi.input.Keyboard;

import java.awt.*;

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
    public Home(int screenWidth, int screenHeight) {
        menu = new UIMenu(screenWidth, screenHeight, TITLE, HOME_OPTIONS);
    }

    ///
    /// Updating and Rendering
    ///
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

        /*
        switch (menu.update(key)) {
            case 1 -> {
                return Game.State.SINGLE_PLAYER;
            }
            case 2 -> {
                return Game.State.MULTI_PLAYER;
            }
            case 0 -> {
                return Game.State.QUIT;
            }

        }

         */
        return menu.update(key);
    }

    public void render(Graphics2D g2, Screen screen) {
        menu.renderMenu(g2, screen);
    }

}
