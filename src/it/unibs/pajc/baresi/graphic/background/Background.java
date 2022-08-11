package it.unibs.pajc.baresi.graphic.background;

import it.unibs.pajc.baresi.input.Keyboard;
import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.input.Mouse;

import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Comparator;

public class Background {
    private int width, height;
    private double scale;
    private double unit;
    private Keyboard input;
    private Layer[] layers;
    private int mapOffset;
    private final int dx = 1;

    //
    // Constructor
    //
    public Background(int width, int height, double scale, Keyboard input, String[][] paths) {
        this.width = width;
        this.height = height;
        this.input = input;
        this.scale = scale;
        mapOffset = 0;
        initialize(paths);
    }

    private void initialize(String[][] paths) {
        layers = new Layer[paths.length];

        for (int i = 0; i < layers.length; i++)
            layers[i] = new Layer(paths[i]);

        // sorts the layer array by width
        Arrays.sort(layers, Comparator.comparing(Layer::getWidth));
        unit = layers[layers.length - 1].getWidth();
    }

    public int getMapOffset() {
        return (int) (mapOffset / ((unit - width) / (layers[layers.length - 1].getWidth() - width)));
    }

    public void update() {
        if ((input.isLeft() || (Mouse.getX() < width * scale * 0.02 && Mouse.getX() >= 0)) && mapOffset > 0) mapOffset -= dx;
        if ((input.isRight() || Mouse.getX() > width * scale * 0.98) && mapOffset < (unit - width)) mapOffset += dx;

        for (Layer l : layers)
            l.update();
    }

    public void render(Screen screen) {
        // needed to set only empty pixels
        for (int i = layers.length - 1; i >= 0; i--) {
            layers[i].render((int) (mapOffset / ((unit - width) / (layers[i].getWidth() - width))), screen);
        }
    }
}
