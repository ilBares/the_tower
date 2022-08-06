package it.unibs.pajc.baresi.graphic.background;

import it.unibs.pajc.baresi.input.Keyboard;
import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.input.Mouse;

import java.util.Arrays;
import java.util.Comparator;

public class Background {
    private int width, height;
    private double scale;
    public static final double UNIT = 1120.;
    private Keyboard input;
    private Layer[] layers;
    private int x;

    //
    // Constructor
    //
    public Background(int width, int height, double scale, Keyboard input, String[][] paths) {
        this.width = width;
        this.height = height;
        this.input = input;
        this.scale = scale;
        x = 0;
        initialize(paths);
    }

    private void initialize(String[][] paths) {
        layers = new Layer[paths.length];

        for (int i = 0; i < layers.length; i++)
            layers[i] = new Layer(paths[i]);

        // sorts the layer array by width
        Arrays.sort(layers, Comparator.comparing(Layer::getWidth));
    }

    public void update() {
        if ((input.isLeft() || Mouse.getX() < width * scale * 0.02) && x > 0) x--;
        if ((input.isRight() || Mouse.getX() > width * scale * 0.98) && x < (UNIT - width)) x++;
        for (Layer l : layers)
            l.update();
    }

    public void render(Screen screen) {
        // needed to set only empty pixels
        for (int i = layers.length - 1; i >= 0; i--) {
            layers[i].render((int) (x / ((UNIT - width) / (layers[i].getWidth() - width))), screen);
        }
    }
}
