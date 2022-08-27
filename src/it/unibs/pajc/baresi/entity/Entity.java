package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Screen;

import java.awt.*;

public abstract class Entity {

    protected double x, y;
    protected boolean removed = false;
    protected double health;

    public void update() { }

    public void render(Screen screen) { }

    public void remove() {
        // TODO remove in update method
        removed = true;
    }

    public void hit(double damage) {
        health -= damage;
    }

    public boolean isAlive() {
        return true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public abstract Rectangle getBounds();
}
