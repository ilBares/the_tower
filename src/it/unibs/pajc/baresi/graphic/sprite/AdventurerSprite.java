package it.unibs.pajc.baresi.graphic.sprite;

public class AdventurerSprite extends Sprite {

    public AdventurerSprite(int size) {
        super(size);
    }

    private AdventurerSprite(int size, int row, int column, SpriteSheet sheet) {
        super(size, row, column, sheet);
    }

    @Override
    void initialize() {
        idle = new AdventurerSprite[10];
        for (int i = 0; i < idle.length; i++)
            idle[i] = new AdventurerSprite(size, 0, i, SpriteSheet.adventureSheet);

        move = new AdventurerSprite[8];
        for (int i = 0; i < move.length; i++)
            move[i] = new AdventurerSprite(size, 1, i, SpriteSheet.adventureSheet);

        attack = new AdventurerSprite[10];
        for (int i = 0; i < attack.length; i++)
            attack[i] = new AdventurerSprite(size, 2, i, SpriteSheet.adventureSheet);

        death = new AdventurerSprite[7];
        for (int i = 0; i < death.length; i++)
            death[i] = new AdventurerSprite(size, 3, i, SpriteSheet.adventureSheet);
    }
}
