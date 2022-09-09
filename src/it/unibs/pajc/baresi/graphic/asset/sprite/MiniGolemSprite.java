package it.unibs.pajc.baresi.graphic.asset.sprite;

import java.io.Serializable;

public class MiniGolemSprite extends Sprite {

    public MiniGolemSprite(int size) {
        super();
        width = size;
        height = size;
        initialize();
    }

    private MiniGolemSprite(int size, int row, int column, SpriteSheet sheet) {
        super(size, row, column, sheet);
    }

    void initialize() {
        idle = new MiniGolemSprite[5];
        for (int i = 0; i < idle.length; i++)
            idle[i] = new MiniGolemSprite(width, 0, i, SpriteSheet.miniGolemSheet);

        move = new MiniGolemSprite[8];
        for (int i = 0; i < move.length; i++)
            move[i] = new MiniGolemSprite(width, 1, i, SpriteSheet.miniGolemSheet);

        attack = new MiniGolemSprite[8];
        for (int i = 0; i < attack.length; i++)
            attack[i] = new MiniGolemSprite(width, 2, i, SpriteSheet.miniGolemSheet);

        death = new MiniGolemSprite[5];
        for (int i = 0; i < death.length; i++)
            death[i] = new MiniGolemSprite(width, 3, i, SpriteSheet.miniGolemSheet);
    }
}
