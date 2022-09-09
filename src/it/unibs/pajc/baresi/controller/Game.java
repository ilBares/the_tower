package it.unibs.pajc.baresi.controller;

import it.unibs.pajc.baresi.graphic.Home;
import it.unibs.pajc.baresi.graphic.ui.UIManager;
import it.unibs.pajc.baresi.input.Keyboard;
import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.background.Background;
import it.unibs.pajc.baresi.input.Mouse;
import it.unibs.pajc.baresi.level.Level;
import it.unibs.pajc.baresi.net.client.Net;
import it.unibs.pajc.baresi.sound.GameSound;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Class necessary to handle principle aspects of the game.
 * It contains view and model aspects of the game.
 *
 * @see Canvas
 * @see Screen
 * @see Background
 */
public class Game extends Canvas implements Runnable {

    // game state
    public enum State {
        HOME, PAUSE, SINGLE_PLAYER, MULTI_PLAYER, QUIT, WIN, GAME_OVER
    }

    // swing and awt components
    private final JFrame frame = new JFrame();

    // game dimensions
    // window dimensions = gameDimensions * scale
    public static final int GAME_WIDTH = 360 / 9 * 16;
    public static final int GAME_HEIGHT = 360;
    private double scale;

    // game title
    private String title;

    // updates per second
    public int fps = 120;

    // thread used to run the game
    private Thread thread;

    // key used to handle keyboard input
    private final Keyboard key;

    // screen used to render the game
    private Screen screen;

    // ui components manager
    private static UIManager uiManager;

    // image rendered by the Screen class
    private BufferedImage image;
    private int[] pixels;

    // game required objects
    private Background background;
    private static Level level;

    // true if the game is running
    private boolean running;

    // necessary to handle game state
    private static State gameState;
    private Home home;

    // necessary for multiplayer mode
    private static Net net;

    ///
    /// Constructor
    ///
    /**
     * Game class constructor.
     *
     * @param scale scale to fit the {@code JFrame} size
     * @param title title of the game
     */
    public Game(double scale, String title) {
        // necessary for window dimension
        this.scale = scale;

        this.title = title;
        this.running = false;

        ///
        /// setting input handlers
        ///
        key = new Keyboard();
        addKeyListener(key);
        Mouse mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        // default home screen
        home = new Home((int) (GAME_WIDTH * scale), (int) (GAME_HEIGHT * scale));
        gameState = State.HOME;

        newGame();
    }

    /**
     * Creates new Game.
     */
    private void newGame() {
        ///
        /// setting graphic handlers
        ///
        background = new Background(
                key, GAME_WIDTH, scale,
                new String[][]{Background.SKY, Background.MOUNTAINS, Background.CLOUDS, Background.GROUND}
        );

        level = new Level(false);

        screen = new Screen(GAME_WIDTH, GAME_HEIGHT);
        uiManager = new UIManager((int) (GAME_WIDTH * scale));

        // image printed on the screen with specific scale
        image = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    /**
     * Initializes the {@code frame}.
     */
    public void initialize() {
        Dimension size = new Dimension((int) (GAME_WIDTH * scale), (int) (GAME_HEIGHT * scale));
        frame.setPreferredSize(size);

        frame.add(this);
        frame.setResizable(false);
        frame.setTitle(title);

        // needed to resize the Window with the preferred size of the canvas
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // needed to center the Window
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        requestFocus();

        playSoundtrack();
    }


    ///
    /// Getters
    ///

    /**
     * @return Game Level object
     */
    public static Level getLevel() {
        return level;
    }


    ///
    /// Handling Game thread
    ///

    /**
     * Runs "Display" thread.
     */
    public synchronized void start() {
        running = true;
        thread = new Thread(this, "Display");
        thread.start();
    }

    /**
     * Safety interrupts "Display" thread.
     */
    public synchronized void stop() {
        running = false;
        if (level.isMultiplayer())
            Net.quit();
        thread.interrupt();
        System.exit(0);
    }

    /**
     * Core of the game. There are two main invoked method:
     * <ul>
     * <li> update => updates logical part </li>
     * <li> render => updates graphical part </li>
     * </ul>
     * <p>
     * The time is managed to get exactly {@code ups} updates per second.
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();

        // used to count fps
        int frames = 0;

        // 1_000_000_000.0 indicates 1 second,
        // "ns" indicates every how many seconds
        // there will be an update
        final double ns = 1_000_000_000.0 / fps;

        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                delta--;

                ///
                /// updating logical part
                ///

                update();

                ///
                /// updating graphical part
                ///
                render();


                frames++;

                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;

                    frame.setTitle(frames + " fps");
                    frames = 0;
                }
            }
        }

        stop();
    }


    ///
    /// Updating and Rendering
    ///

    /**
     * Updates the game, equivalent to the concept of "step next".
     */
    private void update() {
        int levelState = 0;
        int code = -1;

        key.update();
        background.update(gameState == State.HOME || gameState == State.WIN || gameState == State.GAME_OVER || gameState == State.PAUSE);

        if (key.isEscape() && !(gameState == State.WIN) && !(gameState == State.GAME_OVER))
            gameState = State.PAUSE;

        switch (gameState) {
            case WIN -> {
                level.win();
                code = home.update(key, gameState);
                // newGame();
            }
            case GAME_OVER, HOME, PAUSE -> {
                code = home.update(key, gameState);

                if (gameState == State.PAUSE && level.isMultiplayer()) {

                    if (Net.getNetLevel() != null) {
                        level = Net.getNetLevel();
                        levelState = level.getState();
                    }
                }
            }
            case SINGLE_PLAYER -> {
                screen.setMapOffset(background.getMapOffset());
                levelState = level.update();

                if (key.isBares())
                    level.bares();

                uiManager.update();
            }
            case MULTI_PLAYER -> {
                if (net == null) {
                    net = new Net();
                    Thread netThread = new Thread(net);
                    netThread.start();
                }
                if (Net.getNetLevel() != null) {
                    level = Net.getNetLevel();
                    levelState = level.getState();
                }

                screen.setMapOffset(background.getMapOffset());
                uiManager.update();
            }
        }

        switch (code) {
            case 0 -> gameState = State.QUIT;
            case 1 -> gameState = level.isMultiplayer() ? State.MULTI_PLAYER : State.SINGLE_PLAYER;
            case 2 -> gameState = State.MULTI_PLAYER;
        }

        switch (levelState) {
            case 1 -> gameState = State.WIN;
            case -1 -> gameState = State.GAME_OVER;
        }

        if (gameState == State.QUIT)
            stop();
    }

    /**
     * Renders the screen with all its components with the {@code BufferStrategy}.
     *
     * @see BufferStrategy
     */
    private void render() {
        // mechanism that handles and organizes complex memory
        // a buffer is a temporary storage place when we calculate pixels
        // we want to apply it ups time every second (not immediately)
        // buffer strategy stores temporary data in RAM
        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            // triple buffering
            // one buffer is the buffer to display, other two buffers are
            // used to calculate and store next screens (speed improvements)
            createBufferStrategy(3);
            return;
        }

        // drawing image on the JFrame
        Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();

        screen.clear();

        // game state-independent graphics
        background.render(screen);
        level.render(screen);

        // updating pixels that compose the image to be drawn on the JFrame
        System.arraycopy(screen.getPixels(), 0, pixels, 0, pixels.length);

        // needed? g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        if (gameState == State.SINGLE_PLAYER || gameState == State.MULTI_PLAYER)
            uiManager.render(g2, screen, level.getMoney() + "");

        if (gameState == State.HOME || gameState == State.PAUSE || gameState == State.GAME_OVER || gameState == State.WIN)
            home.render(g2, screen);

        g2.dispose();

        // makes the next available buffer of BufferStrategy visible
        bs.show();
    }

    /**
     * Plays the soundtrack contained in {@code Sound} class.
     */
    private void playSoundtrack() {
        GameSound.play(GameSound.SOUND_TRACK, true);
    }

    /**
     * Used to add troop in case of single player or multiplayer.
     *
     * @param troop troop to add
     */
    public static void addTroop(Level.Troop troop) {
        if (gameState == State.SINGLE_PLAYER)
            level.addTroop(troop);

        if (level.isMultiplayer())
            Net.addTroop(troop);
    }
}