package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.asset.Asset;
import it.unibs.pajc.baresi.graphic.asset.FireAsset;
import it.unibs.pajc.baresi.graphic.asset.TowerAsset;
import it.unibs.pajc.baresi.sound.Sound;

import java.awt.*;

public class Tower extends Entity {

    public enum State {
        INTACT, COMPROMISED, DAMAGED, DESTROYED
    }

    private long timer;
    private int anim;

    public final double maxHealth;

    private State state;
    private Asset[] assets;

    private boolean soundPlayed;

    public Tower(Point position, double health) {
        this.x = position.getX();
        this.y = position.getY();
        this.health = health;

        soundPlayed = false;

        maxHealth = health;
        state = State.INTACT;

        anim = 0;

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
        screen.renderAsset((int) x, (int) y, assets[Math.min(anim, assets.length - 1)]);

        if (state == State.DESTROYED) {
            if (!soundPlayed && anim == 0) {
                Sound.play(Sound.TOWER_DESTROYED, false);
                soundPlayed = true;
            }

            screen.renderAsset((int) x + 25, (int) y, FireAsset.FIRE[anim % FireAsset.FIRE.length]);
        }

        if (state == State.DESTROYED && System.currentTimeMillis() > timer + 150) {
            anim += 1;
            timer = System.currentTimeMillis();

        }
    }
}
