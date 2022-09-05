package it.unibs.pajc.baresi.graphic.asset;

import it.unibs.pajc.baresi.graphic.asset.sprite.SpriteSheet;

public class HeartAsset extends Asset {

    public static Asset[] HEART = {
            new HeartAsset(32, 0, 0, SpriteSheet.heartSheet),
            new HeartAsset(32, 0, 1, SpriteSheet.heartSheet),
            new HeartAsset(32, 0, 2, SpriteSheet.heartSheet),
            new HeartAsset(32, 0, 3, SpriteSheet.heartSheet),
            new HeartAsset(32, 0, 4, SpriteSheet.heartSheet),
    };


    private HeartAsset(int size, int row, int column, SpriteSheet sheet) {
        this.width = size;
        this.height = size;
        this.row = row;
        this.column = column;
        this.sheet = sheet;
        load();
    }
}
