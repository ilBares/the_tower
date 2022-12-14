package it.unibs.pajc.baresi.graphic.asset.sprite;

import java.io.Serializable;

public class GolemSprite extends Sprite {

    public GolemSprite(int size) {
        super();
        width = size;
        height = size;
        initialize();
    }

    private GolemSprite(int size, int row, int column, SpriteSheet sheet) {
        super(size, row, column, sheet);
    }

    void initialize() {
        idle = new GolemSprite[7];
        for (int i = 0; i < idle.length; i++)
            idle[i] = new GolemSprite(width, 0, i, SpriteSheet.golemSheet);

        move = new GolemSprite[10];
        for (int i = 0; i < move.length; i++)
            move[i] = new GolemSprite(width, 1, i, SpriteSheet.golemSheet);

        attack = new GolemSprite[5];
        for (int i = 0; i < attack.length; i++)
            attack[i] = new GolemSprite(width, 2, i, SpriteSheet.golemSheet);

        death = new GolemSprite[9];
        for (int i = 0; i < death.length; i++)
            death[i] = new GolemSprite(width, 3, i, SpriteSheet.golemSheet);
    }
}
