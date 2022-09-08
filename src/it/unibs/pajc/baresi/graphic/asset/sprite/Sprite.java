package it.unibs.pajc.baresi.graphic.asset.sprite;

import it.unibs.pajc.baresi.graphic.asset.Asset;

import java.io.Serializable;

public class Sprite extends Asset implements Serializable {

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
}
