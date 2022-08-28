package it.unibs.pajc.baresi.graphic.asset;

import it.unibs.pajc.baresi.graphic.asset.sprite.SpriteSheet;

public class FireAsset extends Asset {

    public static Asset[] FIRE = {
            new FireAsset(110, 50, 0, 0, SpriteSheet.fireSheet),
            new FireAsset(110, 50, 0, 1, SpriteSheet.fireSheet),
            new FireAsset(110, 50, 0, 2, SpriteSheet.fireSheet),
            new FireAsset(110, 50, 0, 3, SpriteSheet.fireSheet),
            new FireAsset(110, 50, 0, 4, SpriteSheet.fireSheet),
            new FireAsset(110, 50, 0, 5, SpriteSheet.fireSheet),
            new FireAsset(110, 50, 0, 6, SpriteSheet.fireSheet),
            new FireAsset(110, 50, 0, 7, SpriteSheet.fireSheet),
    };


    private FireAsset(int width, int height, int row, int column, SpriteSheet sheet) {
        this.width = width;
        this.height = height;
        this.row = row;
        this.column = column;
        this.sheet = sheet;
        load();
    }
}
