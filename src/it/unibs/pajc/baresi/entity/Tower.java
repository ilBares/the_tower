package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.asset.Asset;
import it.unibs.pajc.baresi.graphic.asset.TowerAsset;

import java.awt.*;

public class Tower extends Entity {

    public enum State {
        INTACT, COMPROMISED, DAMAGED, DESTROYED
    }

    private long timer;
    private int index;

    public final double maxHealth;

    private State state;
    private Asset[] assets;

    public Tower(Point position, double health) {
        this.x = position.getX();
        this.y = position.getY();
        this.health = health;

        maxHealth = health;
        state = State.INTACT;

        index = 0;

        assets = TowerAsset.INTACT;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) (x + assets[0].getWidth() / 5.), (int) y, (int) (assets[0].getWidth() / 5. * 4), assets[0].getHeight());
    }

    public void update() {

        switch (state) {
            case INTACT -> {
                if (health < 0.75 * maxHealth) {
                    state = State.COMPROMISED;
                    assets = TowerAsset.COMPROMISED;
                }
            }
            case COMPROMISED -> {
                if (health < 0.25 * maxHealth) {
                    state = State.DAMAGED;
                    assets = TowerAsset.DAMAGED;
                }
            }
            case DAMAGED -> {
                if (health <= 0) {
                    state = State.DESTROYED;
                    assets = TowerAsset.DESTROYED;
                    timer = System.currentTimeMillis();
                }
            }
        }
    }

    public void render(Screen screen) {
        if (state == State.DESTROYED && System.currentTimeMillis() > timer + 150 && index < assets.length - 1) {
            index += 1;
            timer = System.currentTimeMillis();
        }

        screen.renderAsset((int) x, (int) y, assets[index]);
    }
}
