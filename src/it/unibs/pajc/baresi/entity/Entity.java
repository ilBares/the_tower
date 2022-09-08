package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Screen;

import java.awt.*;
import java.io.Serializable;

public abstract class Entity implements Serializable {

    protected double x, y;
    protected boolean removed = false;
    protected double health;

    public void update() { }

    public void render(Screen screen) { }

    public void remove() {
        removed = true;
    }

    public void hit(double damage) {
        health -= damage;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isRemoved() {
        return removed;
    }

    public abstract Rectangle getBounds();
}
