package it.unibs.pajc.baresi.graphic.sprite;

public abstract class Sprite {

    protected int size;
    protected int row, column;
    protected int[] pixels;
    protected SpriteSheet sheet;

    public Sprite[] idle;
    public Sprite[] move;
    public Sprite[] attack;
    public Sprite[] death;
    
    public Sprite(int size) {
        this.size = size;
        initialize();
    }
    
    public Sprite(int size, int row, int column, SpriteSheet sheet) {
        this.size = size;
        this.row = row;
        this.column = column;
        this.sheet = sheet;
        pixels = new int[size * size];
        load();
    }

    private void load() {

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                pixels[x + y * size] = sheet.getPixels()[(x + column * size) + (y + row * size) * sheet.getWidth()];
            }
        }
    }

    public int[] getPixels() {
        return pixels;
    }

    abstract void initialize();

    public int getSize() {
        return size;
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
