package it.unibs.pajc.baresi.graphic.sprite;

public class MiniGolemSprite extends Sprite {

    public MiniGolemSprite(int size) {
        super(size);
    }

    private MiniGolemSprite(int size, int row, int column, SpriteSheet sheet) {
        super(size, row, column, sheet);
    }

    @Override
    void initialize() {
        idle = new MiniGolemSprite[5];
        for (int i = 0; i < idle.length; i++)
            idle[i] = new MiniGolemSprite(size, 0, i, SpriteSheet.miniGolemSheet);

        move = new MiniGolemSprite[8];
        for (int i = 0; i < move.length; i++)
            move[i] = new MiniGolemSprite(size, 1, i, SpriteSheet.miniGolemSheet);

        attack = new MiniGolemSprite[8];
        for (int i = 0; i < attack.length; i++)
            attack[i] = new MiniGolemSprite(size, 2, i, SpriteSheet.miniGolemSheet);

        death = new MiniGolemSprite[5];
        for (int i = 0; i < death.length; i++)
            death[i] = new MiniGolemSprite(size, 3, i, SpriteSheet.miniGolemSheet);
    }
}
