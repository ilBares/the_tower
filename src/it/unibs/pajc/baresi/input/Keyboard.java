package it.unibs.pajc.baresi.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class that process KeyEvent to easily handle input.
 *
 * @see KeyListener
 */
public class Keyboard implements KeyListener {
    public static final int KEY_NUMBER = 120;
    private final boolean[] keys = new boolean[KEY_NUMBER];
    private boolean left, right, up, down;
    private boolean enter;
    private boolean escape;

    /**
     * Updates {@code left} and {@code right} variables based on KeyEvents.
     */
    public void update() {
        right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
        left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
        up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
        enter = keys[KeyEvent.VK_ENTER] || keys[KeyEvent.VK_SPACE];
        escape = keys[KeyEvent.VK_ESCAPE];
    }

    /**
     * @return  true if Left key or A key are selected.
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * @return  true if Right key or D key are selected.
     */
    public boolean isRight() {
        return right;
    }

    /**
     * @return  true if Up key or W key are selected.
     */
    public boolean isUp() {
        return up;
    }

    /**
     * @return  true if Down key or S key are selected.
     */
    public boolean isDown() {
        return down;
    }

    /**
     * @return  true if Enter key or Space key are selected.
     */
    public boolean isEnter() {
        return enter;
    }

    /**
     * @return  true if Escape key is selected.
     */
    public boolean isEscape() {
        return escape;
    }

    public boolean anyKey() {
        for (boolean b : keys)
            if (b) return true;
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length)
            keys[keyCode] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length)
            keys[e.getKeyCode()] = false;
    }
}
