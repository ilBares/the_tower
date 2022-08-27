package it.unibs.pajc.baresi.graphic;

import it.unibs.pajc.baresi.controller.Game;
import it.unibs.pajc.baresi.graphic.ui.UIText;
import it.unibs.pajc.baresi.input.Keyboard;

import java.awt.*;

public class Home {

    private final String TITLE = "THE T0WER";
    private final String[] MENU = {
            "PLAY",
            "CREDITS",
            "QUIT",
    };
    private final String RESUME = "RESUME";

    private int cmdNumber;

    private int screenWidth;
    private int screenHeight;

    private boolean blocked;

    public Home(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        cmdNumber = 0;
        blocked = false;
    }

    public Game.State update(Keyboard key) {
        if (key.isUp()) {
            if (!blocked) {
                cmdNumber = (cmdNumber == 0 ? MENU.length : cmdNumber) - 1;
                blocked = true;
            }
        } else if (key.isDown()) {
            if (!blocked) {
                cmdNumber++;
                blocked = true;
            }
        } else if (key.isEnter()) {
            switch (cmdNumber) {
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
        } else if (blocked) {
            blocked = false;
        }

        cmdNumber %= MENU.length;

        return Game.State.HOME;
    }

    public void render(Graphics2D g2, Screen screen, boolean pause)  {
        renderDialogWindow(g2, screen, pause);
        /*
        // background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);
        */


    }

    public void renderDialogWindow(Graphics2D g2, Screen screen, boolean pause) {

        int width = (int) (screenWidth * (2 / 3.));
        int height = (int) (screenHeight * (2 / 3.));

        int x = screenWidth / 2 - width / 2;
        int y = screenHeight / 2 - height / 2 - 50;

        // "a" is transparency
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);

        g2.fillRoundRect(x, y, width, height, 35, 35);


        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));

        g2.drawRoundRect(x+5, y+5, width - 10, height - 10, 25, 25);



        // shadow
        UIText shadow = new UIText(TITLE, 80, Color.GRAY);

        x = UIText.getXCenteredText(g2, screenWidth, shadow);

        y += 220;

        shadow.setX(x + 5);
        shadow.setY(y + 5);

        screen.drawUIText(g2, shadow);

        // title
        UIText title = new UIText(TITLE, x, y, 80, Color.WHITE);
        screen.drawUIText(g2, title);

        // menu
        UIText[] menu = {
                new UIText(pause ? RESUME : MENU[0],40, Color.WHITE),
                new UIText(MENU[1],40, Color.WHITE),
                new UIText(MENU[2],40, Color.WHITE),
        };

        y += 80;

        // options
        for (int i = 0; i < menu.length; i++) {
            x = UIText.getXCenteredText(g2, screenWidth, menu[i]);
            y += 70;

            menu[i].setX(x);
            menu[i].setY(y);

            screen.drawUIText(g2, menu[i]);

            if (i == cmdNumber) {
                screen.drawUIText(g2, new UIText(">", x - 50, y, 40, Color.WHITE));
            }
        }
    }

}
