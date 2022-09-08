package it.unibs.pajc.baresi.graphic;

import it.unibs.pajc.baresi.controller.Game;
import it.unibs.pajc.baresi.graphic.ui.UIMenu;
import it.unibs.pajc.baresi.input.Keyboard;

import java.awt.*;

public class Home {

    private final String TITLE = "THE T0WER";
    private final String[] HOME_OPTIONS = new String[]{
            "SINGLE PLAYER",
            "MULTI PLAYER",
            "QUIT"
    };
    private final String[] END_OPTIONS = new String[] {
            "QUIT"
    };

    private final String RESUME = "RESUME";
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
    public Game.State update(Keyboard key, Game.State state, boolean pause) {

        if (state == Game.State.WIN) {
            menu.setOptions(END_OPTIONS);
            menu.setTitle("WIN");
        } else if (state == Game.State.GAME_OVER) {
            menu.setOptions(END_OPTIONS);
            menu.setTitle("GAME OVER");
        } else if (pause)
            menu.setOption(0, RESUME);
        else
            menu.setOption(0, HOME_OPTIONS[0]);

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
        return state;
    }

    public void render(Graphics2D g2, Screen screen) {
        menu.renderMenu(g2, screen);
    }

}
