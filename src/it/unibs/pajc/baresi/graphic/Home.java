package it.unibs.pajc.baresi.graphic;

import it.unibs.pajc.baresi.controller.Game;
import it.unibs.pajc.baresi.graphic.ui.UIMenu;
import it.unibs.pajc.baresi.input.Keyboard;

import java.awt.*;

public class Home {

    private final String TITLE = "THE T0WER";
    private final String[] OPTIONS = new String[]{
            "PLAY",
            "CREDITS",
            "QUIT"
    };
    private final String RESUME = "RESUME";
    private UIMenu menu;


    ///
    /// Constructor
    ///
    public Home(int screenWidth, int screenHeight) {
        menu = new UIMenu(screenWidth, screenHeight, TITLE, OPTIONS);
    }

    ///
    /// Updating and Rendering
    ///
    public Game.State update(Keyboard key, boolean pause) {
        if (pause)
            menu.setOption(0, RESUME);
        else
            menu.setOption(0, OPTIONS[0]);

        switch (menu.update(key)) {
            case 0 -> {
                return Game.State.PLAY;
            }
            case 1 -> {
                return Game.State.HOME;
            }
            case 2 -> {
                return Game.State.QUIT;
            }

        }
        return Game.State.HOME;
    }

    public void render(Graphics2D g2, Screen screen) {
        menu.renderMenu(g2, screen);
    }

}
