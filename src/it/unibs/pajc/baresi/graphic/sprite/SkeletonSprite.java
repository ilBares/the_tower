package it.unibs.pajc.baresi.graphic.sprite;

public class SkeletonSprite extends Sprite{

    public SkeletonSprite(int size) {
        super(size);
    }

    private SkeletonSprite(int size, int row, int column, SpriteSheet sheet) {
        super(size, row, column, sheet);
    }

    @Override
    void initialize() {
        idle = new SkeletonSprite[10];
        for (int i = 0; i < idle.length; i++)
            idle[i] = new SkeletonSprite(size, 0, i, SpriteSheet.skeletonSheet);

        move = new SkeletonSprite[10];
        for (int i = 0; i < move.length; i++)
            move[i] = new SkeletonSprite(size, 1, i, SpriteSheet.skeletonSheet);

        attack = new SkeletonSprite[10];
        for (int i = 0; i < attack.length; i++)
            attack[i] = new SkeletonSprite(size, 2, i, SpriteSheet.skeletonSheet);

        death = new SkeletonSprite[9];
        for (int i = 0; i < death.length; i++)
            death[i] = new SkeletonSprite(size, 3, i, SpriteSheet.skeletonSheet);
    }
}
