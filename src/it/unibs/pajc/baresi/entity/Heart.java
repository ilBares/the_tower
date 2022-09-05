package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.controller.Game;
import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.asset.Asset;
import it.unibs.pajc.baresi.graphic.asset.HeartAsset;

import java.awt.*;
import java.io.Serializable;

public class Heart extends Entity {

    // TODO transient
    private Asset[] assets;
    private double x, y;
    private int anim;
    private long timer;


    public Heart() {
        this.x = Game.troopSpawn.getX();
        this.y = Game.troopSpawn.getY() - 5;

        assets = HeartAsset.HEART;

        timer = System.currentTimeMillis();

        health = 1;

        anim = 0;
    }

    public void destroy() {
        health = 0;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, (int) (assets[0].getWidth() / 3.), assets[0].getHeight());
    }

    public void update() {
        if (System.currentTimeMillis() > timer + 120) {
            anim += 1;
            timer = System.currentTimeMillis();
        }

        anim %= assets.length;
    }

    public void render(Screen screen) {
        screen.renderAsset((int) x, (int) y, assets[anim]);
    }

}
