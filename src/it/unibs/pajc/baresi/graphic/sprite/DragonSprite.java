package it.unibs.pajc.baresi.graphic.sprite;

public class DragonSprite extends Sprite {


    public DragonSprite(int size) {
        super(size);
    }

    private DragonSprite(int size, int row, int column, SpriteSheet sheet) {
        super(size, row, column, sheet);
    }

    @Override
    void initialize() {
        idle = new DragonSprite[10];
        for (int i = 0; i < idle.length; i++)
            idle[i] = new DragonSprite(size, 0, i, SpriteSheet.dragonSheet);

        move = new DragonSprite[10];
        for (int i = 0; i < move.length; i++)
            move[i] = new DragonSprite(size, 1, i, SpriteSheet.dragonSheet);

        attack = new DragonSprite[10];
        for (int i = 0; i < attack.length; i++)
            attack[i] = new DragonSprite(size, 2, i, SpriteSheet.dragonSheet);

        death = new DragonSprite[10];
        for (int i = 0; i < death.length; i++)
            death[i] = new DragonSprite(size, 3, i, SpriteSheet.dragonSheet);
    }
}
