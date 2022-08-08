package it.unibs.pajc.baresi.graphic.sprite;

public class GhoulSprite extends Sprite {


    public GhoulSprite(int size) {
        super(size);
    }

    private GhoulSprite(int size, int row, int column, SpriteSheet sheet) {
        super(size, row, column, sheet);
    }

    @Override
    void initialize() {
        idle = new GhoulSprite[8];
        for (int i = 0; i < idle.length; i++)
            idle[i] = new GhoulSprite(size, 0, i, SpriteSheet.ghoulSheet);

        move = new GhoulSprite[8];
        for (int i = 0; i < move.length; i++)
            move[i] = new GhoulSprite(size, 1, i, SpriteSheet.ghoulSheet);

        attack = new GhoulSprite[6];
        for (int i = 0; i < attack.length; i++)
            attack[i] = new GhoulSprite(size, 2, i, SpriteSheet.ghoulSheet);

        death = new GhoulSprite[6];
        for (int i = 0; i < death.length; i++)
            death[i] = new GhoulSprite(size, 3, i, SpriteSheet.ghoulSheet);
    }
}
