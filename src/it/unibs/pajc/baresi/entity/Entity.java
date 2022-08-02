package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.view.Screen;

public abstract class Entity {

    private int x, y;
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
}
