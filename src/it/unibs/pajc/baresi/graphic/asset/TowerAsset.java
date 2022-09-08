package it.unibs.pajc.baresi.graphic.asset;

import it.unibs.pajc.baresi.graphic.asset.sprite.SpriteSheet;

public class TowerAsset extends Asset {

    public static final Asset[] INTACT = { new TowerAsset(150, 260, 0, 0, SpriteSheet.towerSheet) };
    public static final Asset[] COMPROMISED = { new TowerAsset(150, 260, 1, 0, SpriteSheet.towerSheet) };
    public static final Asset[] DAMAGED = { new TowerAsset(150, 260, 2, 0, SpriteSheet.towerSheet) };
    public static final Asset[] DESTROYED = {
            new TowerAsset(150, 260, 3, 0, SpriteSheet.towerSheet),
            new TowerAsset(150, 260, 3, 1, SpriteSheet.towerSheet),
            new TowerAsset(150, 260, 3, 2, SpriteSheet.towerSheet),
            new TowerAsset(150, 260, 3, 3, SpriteSheet.towerSheet),
            new TowerAsset(150, 260, 3, 4, SpriteSheet.towerSheet),
            new TowerAsset(150, 260, 3, 5, SpriteSheet.towerSheet),
    };

    public TowerAsset() {

    }

    private TowerAsset(int width, int height, int row, int column, SpriteSheet sheet) {
        this.width = width;
        this.height = height;
        this.row = row;
        this.column = column;
        this.sheet = sheet;
        load();
    }
}
