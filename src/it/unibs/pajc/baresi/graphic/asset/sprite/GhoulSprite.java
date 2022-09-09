package it.unibs.pajc.baresi.graphic.asset.sprite;

import java.io.Serializable;

public class GhoulSprite extends Sprite {


    public GhoulSprite(int size) {
        super();
        width = size;
        height = size;
        initialize();
    }

    public GhoulSprite(int size, int row, int column, SpriteSheet sheet) {
        super(size, row, column, sheet);
    }

    void initialize() {
        idle = new GhoulSprite[8];
        for (int i = 0; i < idle.length; i++)
            idle[i] = new GhoulSprite(width, 0, i, SpriteSheet.ghoulSheet);

        move = new GhoulSprite[8];
        for (int i = 0; i < move.length; i++)
            move[i] = new GhoulSprite(width, 1, i, SpriteSheet.ghoulSheet);

        attack = new GhoulSprite[6];
        for (int i = 0; i < attack.length; i++)
            attack[i] = new GhoulSprite(width, 2, i, SpriteSheet.ghoulSheet);

        death = new GhoulSprite[6];
        for (int i = 0; i < death.length; i++)
            death[i] = new GhoulSprite(width, 3, i, SpriteSheet.ghoulSheet);
    }
}
