package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.asset.TowerAsset;
import it.unibs.pajc.baresi.graphic.asset.sprite.Sprite;

import java.awt.*;

public class Tower extends Entity {

    private int health;
    // 0 = undamaged
    // 1 = damaged
    // 2 = very damaged
    // 3 = destroyed
    private int state;
    private Sprite[] sprites;

    public Tower(Point position, int health) {
        this.x = position.getX();
        this.y = position.getY();
        this.health = health;
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    public void update() {

    }

    public void render(Screen screen) {
        screen.renderAsset((int) x, (int) y, TowerAsset.INTACT);
    }
}
