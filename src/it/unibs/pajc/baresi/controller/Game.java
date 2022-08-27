package it.unibs.pajc.baresi.controller;

import it.unibs.pajc.baresi.graphic.Home;
import it.unibs.pajc.baresi.graphic.ui.UIManager;
import it.unibs.pajc.baresi.input.Keyboard;
import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.background.Background;
import it.unibs.pajc.baresi.input.Mouse;
import it.unibs.pajc.baresi.level.Level;
import it.unibs.pajc.baresi.sound.Sound;

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

    public enum State {
        HOME, PLAY, QUIT;
    }

    // swing and awt components
    private final JFrame frame;

    // game dimensions
    // window dimensions = gameDimensions * scale
    private int gameWidth, gameHeight;
    private double scale;

    // game title
    private String  title;

    // updates per second
    public int ups = 120;

    // thread used to run the game
    private Thread thread;

    // key used to handle keyboard input
    private final Keyboard key;

    private Screen screen;
    private static UIManager uiManager;

    // image created using pixels array
    private BufferedImage image;
    private int[] pixels;

    private Background background;
    private static Level level;
    private boolean running;

    // 0: menu
    // 1: playing game
    private State gameState;
    private Home home;

    private boolean pause;

    ///
    /// Constructor
    ///
    /**
     * Game class constructor.
     *
     * @param gameHeight    gameHeight before applying the scale
     * @param gameWidth     gameWidth before applying the scale
     * @param scale         scale to fit the {@code JFrame} size
     * @param title         title of the game
     */
    public Game(int gameWidth, int gameHeight, double scale, String title) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.scale = scale;

        this.title = title;
        this.running = false;
        frame = new JFrame();

        ///
        /// setting input handlers
        ///
        key = new Keyboard();
        addKeyListener(key);
        Mouse mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        ///
        /// setting graphic handlers
        ///
        background = new Background(
                key, gameWidth, scale,
                new String[][] {Background.SKY, Background.MOUNTAINS, Background.CLOUDS, Background.GROUND}
        );

        // TODO ADD CONST
        level = new Level(new Point(0, gameHeight - 17), new Point(1200, gameHeight - 17));
        screen = new Screen(gameWidth, gameHeight);
        uiManager = new UIManager((int) (gameWidth * scale));

        // image printed on the screen with specific scale
        image = new BufferedImage(gameWidth, gameHeight, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        home = new Home((int) (gameWidth * scale), (int) (gameHeight * scale));
        gameState = State.HOME;
        pause = false;
    }

    /**
     * Initializes the {@code frame}.
     */
    public void initialize() {
        Dimension size = new Dimension((int) (gameWidth * scale), (int) (gameHeight * scale));
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

        playMusic();
    }

    ///
    /// Getters
    ///
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
        thread.interrupt();
        System.exit(0);
    }

    /**
     * Core of the game. There are two main invoked method:
     * <ul>
     * <li> update => updates logical part </li>
     * <li> render => updates graphical part </li>
     * </ul>
     *
     * The time is managed to get exactly {@code ups} updates per second.
     */
    @Override
    public void run() {
        /*

        ///
        /// Using executor
        ///
        AtomicLong timer = new AtomicLong(System.currentTimeMillis());

        AtomicInteger frames = new AtomicInteger();
        AtomicInteger updates = new AtomicInteger();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        Runnable task = () -> {
            ///
            /// updating logical part
            ///
            update();
            updates.getAndIncrement();

            ///
            /// updating graphical part
            ///
            render();
            frames.getAndIncrement();

            if (System.currentTimeMillis() - timer.get() > 1000) {
                timer.addAndGet(1000);
                frame.setTitle("fps " + frames);
                updates.set(0);
                frames.set(0);
            }
        };

        Runnable taskRender = () -> {

        };

        int initialDelay = 0;
        long period = 1_000_000_000L / ups;

        executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.NANOSECONDS);

         */

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();

        // used to count fps
        int frames = 0;

        // 1_000_000_000.0 indicates 1 second,
        // "ns" indicates every how many seconds
        // there will be an update
        final double ns = 1_000_000_000.0 / ups;

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

                    // TODO to remove
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
        key.update();
        background.update(gameState == State.HOME);

        if (key.isEscape()) {
            gameState = State.HOME;
            pause = true;
        }

        switch (gameState) {
            case HOME -> {
                gameState = home.update(key);

                if (gameState == State.QUIT)
                    stop();

                if (gameState == State.PLAY)
                    pause = false;
            }
            case PLAY -> {
                screen.setMapOffset(background.getMapOffset());
                level.update();
                uiManager.update();
            }
        }
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
        background.render(screen);


        // rendering pixels
        level.render(screen);


        // updating pixels that compose the image to be drawn on the JFrame
        System.arraycopy(screen.getPixels(), 0, pixels, 0, pixels.length);

        // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        if (gameState == State.PLAY) {
            uiManager.render(g2, screen, level.getMoney() + "");
        }


        if (gameState == State.HOME) {
            home.render(g2, screen, pause);
        }


        g2.dispose();

        // makes the next available buffer of BufferStrategy visible
        bs.show();
    }

    private void playMusic() {
        Sound.play(Sound.SOUND_TRACK, true);
    }
}


