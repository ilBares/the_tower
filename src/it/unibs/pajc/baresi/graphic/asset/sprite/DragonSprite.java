package it.unibs.pajc.baresi.graphic.asset.sprite;

import java.io.Serializable;

public class DragonSprite extends Sprite {


    public DragonSprite(int size) {
        super();
        width = size;
        height = size;
        initialize();
    }

    private DragonSprite(int size, int row, int column, SpriteSheet sheet) {
        super(size, row, column, sheet);
    }

    void initialize() {
        idle = new DragonSprite[10];
        for (int i = 0; i < idle.length; i++)
            idle[i] = new DragonSprite(width, 0, i, SpriteSheet.dragonSheet);

        move = new DragonSprite[10];
        for (int i = 0; i < move.length; i++)
            move[i] = new DragonSprite(width, 1, i, SpriteSheet.dragonSheet);

        attack = new DragonSprite[10];
        for (int i = 0; i < attack.length; i++)
            attack[i] = new DragonSprite(width, 2, i, SpriteSheet.dragonSheet);

        death = new DragonSprite[10];
        for (int i = 0; i < death.length; i++)
            death[i] = new DragonSprite(width, 3, i, SpriteSheet.dragonSheet);
    }
}
