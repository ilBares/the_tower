package it.unibs.pajc.baresi.graphic.background;

import it.unibs.pajc.baresi.controller.Game;
import it.unibs.pajc.baresi.input.Keyboard;
import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.input.Mouse;

import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Comparator;

public class Background {

    private double unit;
    private Keyboard input;
    private Layer[] layers;
    private double mapOffset;
    private final double dx = 1;

    private int gameWidth;
    private double gameScale;

    // paths relative to background images
    public static final String[] SKY = { "/background/sky/sky.png" };
    public static final String[] MOUNTAINS = {
        "/background/mountains/mountains_1.png",
        "/background/mountains/mountains_2.png",
        "/background/mountains/mountains_3.png",
        "/background/mountains/mountains_4.png",
        "/background/mountains/mountains_5.png",
        "/background/mountains/mountains_6.png",
        "/background/mountains/mountains_7.png",
        "/background/mountains/mountains_8.png",
    };
    public static final String[] CLOUDS = { "/background/clouds/clouds.png" };
    public static final String[] GROUND = { "/background/ground/ground.png" };

    //
    // Constructor
    //
    public Background(Keyboard input, int gameWidth, double gameScale, String[][] paths) {
        this.input = input;
        this.gameWidth = gameWidth;
        this.gameScale = gameScale;
        mapOffset = 0;
        initialize(paths);
    }

    private void initialize(String[][] paths) {
        layers = new Layer[paths.length];

        for (int i = 0; i < layers.length; i++)
            layers[i] = new Layer(paths[i]);

        // sorts the layer array by width
        Arrays.sort(layers, Comparator.comparing(Layer::getWidth));

        unit = layers[layers.length - 2].getWidth();
    }

    ///
    /// Getters
    ///
    public int getMapOffset() {
        return (int) (mapOffset / ((unit - gameWidth) / (layers[layers.length - 1].getWidth() - gameWidth)));
    }

    ///
    /// Updating and Rendering
    ///
    public void update(boolean home) {
        if (!home && (input.isLeft() || (Mouse.getX() < gameWidth * gameScale * 0.01 && Mouse.getX() >= 0)) && mapOffset > 0) mapOffset -= dx;
        if (!home && (input.isRight() || Mouse.getX() > gameWidth * gameScale * 0.99) && mapOffset < (unit - gameWidth)) mapOffset += dx;

        for (Layer l : layers)
            l.update();
    }

    public void render(Screen screen) {
        // needed to set only empty pixels
        for (int i = layers.length - 1; i >= 0; i--) {
            layers[i].render((int) (mapOffset / ((unit - gameWidth) / (layers[i].getWidth() - gameWidth))), screen);
        }
    }
}
