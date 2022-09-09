package it.unibs.pajc.baresi.graphic.ui;

import java.awt.*;

/**
 * UIText class used to display text on the screen.
 */
public class UIText {
    private Font font;
    private String text;
    private int x, y;
    private Color color;

    ///
    /// Constructors
    ///
    public UIText(String text, int x, int y, int size) {
        this(text, x, y, size, Color.BLACK);
    }

    public UIText(String text, int size, Color color) {
        this(text, 0, 0, size, color);
    }

    public UIText(String text, int x, int y, int size, Color color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;

        // setting game font
        font = new Font("Minecraftia 2.0", Font.PLAIN, size);
    }

    ///
    /// Getters and setters
    ///
    public Color getColor() {
        return color;
    }

    public Font getFont() {
        return font;
    }

    public String getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setText(String text) {
        this.text = text;
    }

    ///
    /// Utility method
    ///
    public static int getXCenteredText(Graphics2D g2, int screenWidth, UIText lbl) {
        g2.setFont(lbl.getFont());
        int length = (int) g2.getFontMetrics().getStringBounds(lbl.getText(), g2).getWidth();
        return screenWidth / 2 - length / 2;
    }
}
