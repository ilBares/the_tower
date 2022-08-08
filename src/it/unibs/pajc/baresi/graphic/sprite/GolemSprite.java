package it.unibs.pajc.baresi.graphic.sprite;

public class GolemSprite extends Sprite {

    public GolemSprite(int size) {
        super(size);
    }

    private GolemSprite(int size, int row, int column, SpriteSheet sheet) {
        super(size, row, column, sheet);
    }

    @Override
    void initialize() {
        idle = new GolemSprite[5];
        for (int i = 0; i < idle.length; i++)
            idle[i] = new GolemSprite(size, 0, i, SpriteSheet.golemSheet);

        move = new GolemSprite[10];
        for (int i = 0; i < move.length; i++)
            move[i] = new GolemSprite(size, 1, i, SpriteSheet.golemSheet);

        attack = new GolemSprite[10];
        for (int i = 0; i < attack.length; i++)
            attack[i] = new GolemSprite(size, 2, i, SpriteSheet.golemSheet);

        death = new GolemSprite[9];
        for (int i = 0; i < death.length; i++)
            death[i] = new GolemSprite(size, 3, i, SpriteSheet.golemSheet);
    }
}
