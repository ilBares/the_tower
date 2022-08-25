package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.asset.Asset;
import it.unibs.pajc.baresi.graphic.asset.BombAsset;
import it.unibs.pajc.baresi.graphic.asset.TowerAsset;
import it.unibs.pajc.baresi.graphic.asset.sprite.Sprite;

import java.awt.*;

public class Bomb extends Entity {

    public static final int GAP = 15;
    private Asset[] assets;
    private int index;
    private int timer;
    private int anim;

    public Bomb(Point position) {
        this.x = position.getX();
        this.y = position.getY();
        assets = BombAsset.BOMB;
        index = 0;
        anim = 0;
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    public void update() {
        //y--;

        index = (++anim / (GAP)) % (assets.length+3);

        anim %= GAP * (assets.length+3);
        if (index > assets.length-1) index = 0;
    }

    public void render(Screen screen) {
        screen.renderAsset((int) x, (int) y, assets[index]);
    }
}
