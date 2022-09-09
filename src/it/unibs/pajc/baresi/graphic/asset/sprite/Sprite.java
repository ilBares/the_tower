package it.unibs.pajc.baresi.graphic.asset.sprite;

import it.unibs.pajc.baresi.graphic.asset.Asset;
import it.unibs.pajc.baresi.level.Level;

import static it.unibs.pajc.baresi.level.Level.Troop.*;

public class Sprite extends Asset {

    public Sprite[] idle;
    public Sprite[] move;
    public Sprite[] attack;
    public Sprite[] death;

    public Sprite() { }

    public Sprite(int size, int row, int column, SpriteSheet sheet) {
        this(size, size, row, column, sheet);
    }

    public Sprite(int width, int height, int row, int column, SpriteSheet sheet) {
        this.width = width;
        this.height = height;
        this.row = row;
        this.column = column;
        this.sheet = sheet;
        load();
    }

    public Sprite[] getIdle() {
        return idle;
    }

    public Sprite[] getMove() {
        return move;
    }

    public Sprite[] getAttack() {
        return attack;
    }

    public Sprite[] getDeath() {
        return death;
    }

    public static Sprite getModel(Level.Category category) {
        if (category instanceof Level.Troop) {
            switch ((Level.Troop) category) {
                case MINI_GOLEM -> {
                    return new MiniGolemSprite(64);
                }
                case ADVENTURER -> {
                    return new AdventurerSprite(64);
                }
                case DRAGON -> {
                    return new DragonSprite(64);
                }
                case GOLEM -> {
                    return new GolemSprite(64);
                }
            }
        }
        if (category instanceof Level.Enemy) {
            switch ((Level.Enemy) category) {
                case SKELETON -> {
                    return new SkeletonSprite(64);
                }
                case GHOUL -> {
                    return new GhoulSprite(64);
                }
            }
        }
        return null;
    }
}
