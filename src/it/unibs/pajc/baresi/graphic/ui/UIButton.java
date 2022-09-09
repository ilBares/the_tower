package it.unibs.pajc.baresi.graphic.ui;

import it.unibs.pajc.baresi.controller.Game;
import it.unibs.pajc.baresi.input.Mouse;
import it.unibs.pajc.baresi.level.Level;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * UIButton class used from the UIManager.
 */
public class UIButton {

    private int x, y;
    private int width, height;
    private String path;
    private BufferedImage image;
    private UIButtonListener buttonListener;
    private UIActionListener actionListener;
    private Rectangle rect;
    private int[] defaultRGB;
    private boolean inside = false;
    private boolean pressed = false;
    private boolean visible = false;

    // buttons used from the game with their actions
    public static UIButton btnMob = new UIButton("/button/button_mob.png", 15, 15, () -> {});
    public static UIButton btnBack = new UIButton("/button/button_back.png", 15, 15, () -> {});
    public static UIButton btnMiniGolem = new UIButton("/button/button_mini_golem.png", 140, 10, () -> Game.addTroop(Level.Troop.MINI_GOLEM));
    public static UIButton btnAdventurer = new UIButton("/button/button_adventurer.png", 250, 10, () -> Game.addTroop(Level.Troop.ADVENTURER));
    public static UIButton btnDragon = new UIButton("/button/button_dragon.png", 360, 10, () -> Game.addTroop(Level.Troop.DRAGON));
    public static UIButton btnGolem = new UIButton("/button/button_golem.png", 470, 10, () -> Game.addTroop(Level.Troop.GOLEM));

    ///
    /// Constructor
    ///
    public UIButton(String path, int x, int y, UIActionListener actionListener) {
        this.path = path;
        this.x = x;
        this.y = y;
        this.actionListener = actionListener;
        setUiButtonListener(new UIButtonListener() { });
        load();
    }


    private void load() {
        try {
            image = ImageIO.read(UIButton.class.getResource(path));

            width = image.getWidth();
            height = image.getHeight();

            defaultRGB = new int[width * height];
            image.getRGB(0, 0, width, height, defaultRGB, 0, width);
            rect = new Rectangle(x, y, width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    ///
    /// Getters and Setters
    ///
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setUiButtonListener(UIButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    public int[] getDefaultRGB() {
        return defaultRGB;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    ///
    /// Updating and rendering
    ///
    public void update() {
        if (visible) {
            if (rect.contains(new Point(Mouse.getX(), Mouse.getY()))) {
                if (!inside) {
                    inside = true;
                    buttonListener.entered(this);
                }
                if (!pressed && Mouse.getButton() == MouseEvent.BUTTON1) {
                    pressed = true;
                    buttonListener.pressed(this);
                } else if (pressed && Mouse.getButton() == MouseEvent.NOBUTTON) {
                    pressed = false;
                    actionListener.perform();
                    buttonListener.released(this);
                }
            } else if (inside) {
                inside = false;
                if (pressed) {
                    pressed = false;
                    actionListener.perform();
                    buttonListener.released(this);
                }
                buttonListener.exited(this);
            }
        }
    }
}
