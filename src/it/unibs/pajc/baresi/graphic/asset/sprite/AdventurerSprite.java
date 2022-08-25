package it.unibs.pajc.baresi.graphic.asset.sprite;

public class AdventurerSprite extends Sprite {

    public AdventurerSprite(int size) {
        super();
        width = size;
        height = size;
        initialize();
    }

    private AdventurerSprite(int size, int row, int column, SpriteSheet sheet) {
        super(size, row, column, sheet);
    }

    void initialize() {
        idle = new Sprite[10];
        for (int i = 0; i < idle.length; i++)
            idle[i] = new AdventurerSprite(width, 0, i, SpriteSheet.adventureSheet);

        move = new AdventurerSprite[8];
        for (int i = 0; i < move.length; i++)
            move[i] = new AdventurerSprite(width, 1, i, SpriteSheet.adventureSheet);

        attack = new AdventurerSprite[10];
        for (int i = 0; i < attack.length; i++)
            attack[i] = new AdventurerSprite(width, 2, i, SpriteSheet.adventureSheet);

        death = new AdventurerSprite[7];
        for (int i = 0; i < death.length; i++)
            death[i] = new AdventurerSprite(width, 3, i, SpriteSheet.adventureSheet);
    }
}
