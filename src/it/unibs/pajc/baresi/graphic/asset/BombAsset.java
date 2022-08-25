package it.unibs.pajc.baresi.graphic.asset;

import it.unibs.pajc.baresi.graphic.asset.sprite.SpriteSheet;

public class BombAsset extends Asset {

    public static Asset[] BOMB = {
            new BombAsset(128, 128, 0, 0, SpriteSheet.bombSheet),
            new BombAsset(128, 128, 0, 1, SpriteSheet.bombSheet),
            new BombAsset(128, 128, 0, 2, SpriteSheet.bombSheet),
            new BombAsset(128, 128, 0, 3, SpriteSheet.bombSheet),
            new BombAsset(128, 128, 0, 4, SpriteSheet.bombSheet),
            new BombAsset(128, 128, 0, 5, SpriteSheet.bombSheet),
            new BombAsset(128, 128, 0, 6, SpriteSheet.bombSheet),
            new BombAsset(128, 128, 0, 7, SpriteSheet.bombSheet),
            new BombAsset(128, 128, 0, 8, SpriteSheet.bombSheet),
    };


    private BombAsset(int width, int height, int row, int column, SpriteSheet sheet) {
        this.width = width;
        this.height = height;
        this.row = row;
        this.column = column;
        this.sheet = sheet;
        load();
    }
}
