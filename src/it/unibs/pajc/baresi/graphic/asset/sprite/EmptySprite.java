package it.unibs.pajc.baresi.graphic.asset.sprite;

public class EmptySprite extends Sprite {

    public EmptySprite(int size, int idleSize, int moveSize, int attackSize, int deathSize) {
        super();
        width = size;
        height = size;
        idle = new Sprite[idleSize];
        attack = new Sprite[attackSize];
        move = new Sprite[moveSize];
        death = new Sprite[deathSize];
    }
}
