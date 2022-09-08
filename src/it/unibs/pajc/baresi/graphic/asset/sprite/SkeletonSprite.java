package it.unibs.pajc.baresi.graphic.asset.sprite;

import java.io.Serializable;

public class SkeletonSprite extends Sprite implements Serializable {

    public SkeletonSprite(int size) {
        super();
        width = size;
        height = size;
        initialize();
    }

    private SkeletonSprite(int size, int row, int column, SpriteSheet sheet) {
        super(size, row, column, sheet);
    }

    void initialize() {
        idle = new SkeletonSprite[10];
        for (int i = 0; i < idle.length; i++)
            idle[i] = new SkeletonSprite(width, 0, i, SpriteSheet.skeletonSheet);

        move = new SkeletonSprite[10];
        for (int i = 0; i < move.length; i++)
            move[i] = new SkeletonSprite(width, 1, i, SpriteSheet.skeletonSheet);

        attack = new SkeletonSprite[10];
        for (int i = 0; i < attack.length; i++)
            attack[i] = new SkeletonSprite(width, 2, i, SpriteSheet.skeletonSheet);

        death = new SkeletonSprite[9];
        for (int i = 0; i < death.length; i++)
            death[i] = new SkeletonSprite(width, 3, i, SpriteSheet.skeletonSheet);
    }
}
