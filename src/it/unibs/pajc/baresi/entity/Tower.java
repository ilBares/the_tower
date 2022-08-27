package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.asset.Asset;
import it.unibs.pajc.baresi.graphic.asset.TowerAsset;

import java.awt.*;

public class Tower extends Entity {

    public enum State {
        INTACT, COMPROMISED, DAMAGED, DESTROYED
    }

    public final double maxHealth;

    private State state;
    private Asset[] assets;

    public Tower(Point position, double health) {
        this.x = position.getX();
        this.y = position.getY();
        this.health = health;

        maxHealth = health;
        state = State.INTACT;

        assets = TowerAsset.INTACT;
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    public void update() {
        double healthOffset = maxHealth - health;

        switch (state) {
            case INTACT -> {
                if (healthOffset < 0.75 * maxHealth) {
                    state = State.COMPROMISED;
                    assets = TowerAsset.COMPROMISED;
                }
            }
            case COMPROMISED -> {
                if (healthOffset < 0.25 * maxHealth) {
                    state = State.DAMAGED;
                    assets = TowerAsset.DAMAGED;
                }
            }
            case DAMAGED -> {
                if (healthOffset <= 0) {
                    state = State.DESTROYED;
                    assets = TowerAsset.DESTROYED;
                }
            }
        }
    }

    public void render(Screen screen) {
        screen.renderAsset((int) x, (int) y, TowerAsset.INTACT[0]);
        screen.renderAsset((int) x - 100, (int) y, TowerAsset.COMPROMISED[0]);
    }
}
