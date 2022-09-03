package it.unibs.pajc.baresi.graphic.ui;

import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.input.Keyboard;

import java.awt.*;

public class UIMenu {
    private int screenWidth, screenHeight;
    private String title;
    private String[] options;

    private int cmdNumber;
    private boolean blocked;

    public UIMenu(int screenWidth, int screenHeight, String title, String[] options) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.title = title;
        this.options = options;

        cmdNumber = 0;
    }

    public int update(Keyboard key) {
        if (key.isUp()) {
            if (!blocked) {
                cmdNumber = (cmdNumber == 0 ? options.length : cmdNumber) - 1;
                blocked = true;
            }
        } else if (key.isDown()) {
            if (!blocked) {
                cmdNumber++;
                blocked = true;
            }
        } else if (key.isEnter()) {
            return cmdNumber;
        } else if (blocked) {
            blocked = false;
        }

        cmdNumber %= options.length;

        return -1;
    }

    public void renderMenu(Graphics2D g2, Screen screen) {

        int w = (int) (screenWidth * (2 / 3.));
        int h = (int) (screenHeight * (2 / 3.));

        int x = screenWidth / 2 - w / 2;
        int y = screenHeight / 2 - h / 2 - 50;

        // "a" is transparency
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);

        g2.fillRoundRect(x, y, w, h, 35, 35);


        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));

        g2.drawRoundRect(x+5, y+5, w - 10, h - 10, 25, 25);



        // shadow
        UIText shadow = new UIText(title, 80, Color.GRAY);

        x = UIText.getXCenteredText(g2, screenWidth, shadow);

        y += 220;

        shadow.setX(x + 5);
        shadow.setY(y + 5);

        screen.drawUIText(g2, shadow);

        // title
        UIText uiTitle = new UIText(title, x, y, 80, Color.WHITE);
        screen.drawUIText(g2, uiTitle);

        // menu
        UIText[] menu = new UIText[options.length];

        for (int i = 0; i < options.length; i++) {
            menu[i] = new UIText(options[i],40, Color.WHITE);
        }

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

    public void setOption(int index, String text) {
        options[index] = text;
    }
}
