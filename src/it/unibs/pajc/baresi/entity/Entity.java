package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Screen;

import java.awt.*;

public abstract class Entity {

    protected double x, y;
    private boolean removed = false;

    public void update() { }

    public void render(Screen screen) { }

    public void remove() {
        // TODO remove in update method
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public abstract Rectangle getBounds();
}
