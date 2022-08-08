package it.unibs.pajc.baresi.controller;

import it.unibs.pajc.baresi.entity.Mob;
import it.unibs.pajc.baresi.graphic.sprite.*;
import it.unibs.pajc.baresi.input.Keyboard;
import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.background.Background;
import it.unibs.pajc.baresi.input.Mouse;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Class necessary to handle principle aspects of the game.
 * It contains view and model aspects of the game.
 *
 * @see Canvas
 * @see Screen
 * @see Background
 */
public class Game extends Canvas implements  Runnable {
    private final JFrame frame;
    private int width, height;
    private double scale;

    private String  title;

    public long ups = 120L;

    private Thread thread;
    private final Keyboard key;
    private boolean running;
    private Screen screen;
    // todo to remove
    private LinkedList<Mob> mobs;

    private Background background;
    // paths relative to background images
    private final String[][] paths = {
            {
                "/background/ground/ground_temp.png",
            },
            {
                "/background/mountains/mountains_1.png",
                "/background/mountains/mountains_2.png",
                "/background/mountains/mountains_3.png",
                "/background/mountains/mountains_4.png",
                "/background/mountains/mountains_5.png",
                "/background/mountains/mountains_6.png",
                "/background/mountains/mountains_7.png",
                "/background/mountains/mountains_8.png",
            },
            {
                "/background/clouds/clouds.png",
            },
            {
                "/background/sky/sky.png",
            }
    };

    private BufferedImage image;
    private int[] pixels;


    /**
     * Game class constructor.
     *
     * @param height    height before applying the scale
     * @param width     width before applying the scale
     * @param scale     scale to fit the {@code JFrame} size
     * @param title     title of the game
     */
    public Game(int width, int height, double scale, String title) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.title = title;
        this.running = false;
        frame = new JFrame();

        ///
        /// handling keyboard input
        ///
        key = new Keyboard();
        addKeyListener(key);

        ///
        /// handling mouse input
        ///
        Mouse mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        background = new Background(width, height, scale, key, paths);
        screen = new Screen(width, height);

        mobs = new LinkedList<>();
        // todo to remove
        Sprite dragon = new DragonSprite(64);
        mobs.push(new Mob(10, height - 18, 0.30, dragon));

        Sprite golem = new GolemSprite(64);
        mobs.push(new Mob(70, height - 18, 0.15, golem));

        Sprite adventurer = new AdventurerSprite(64);
        mobs.push(new Mob(130, height - 18, 0.25, adventurer));

        Sprite mini = new MiniGolemSprite(64);
        mobs.push(new Mob(190, height - 18, 0.35, mini));

        Sprite skeleton = new SkeletonSprite(64);
        mobs.push(new Mob(1100, height - 18, -0.2, skeleton));

        Sprite ghoul = new GhoulSprite(64);
        mobs.push(new Mob(1000, height - 18, -0.15, ghoul));

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    /**
     * Initializes {@code image}, {@code pixels} and all stuff needed by the game.
     */
    public void initialize() {
        Dimension size = new Dimension((int) (width * scale), (int) (height * scale));
        setPreferredSize(size);

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
    }

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
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        AtomicLong timer = new AtomicLong(System.currentTimeMillis());
        AtomicInteger frames = new AtomicInteger();
        AtomicInteger updates = new AtomicInteger();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
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
                // TODO to remove
                frame.setTitle(frames + " fps");
                updates.set(0);
                frames.set(0);
            }
        };

        int initialDelay = 0;
        long period = 1_000_000_000L / ups;
        executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.NANOSECONDS);

        /*

        ///
        /// WITHOUT EXECUTOR
        ///
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();

        int frames = 0;
        int updates = 0;

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
                updates++;

                ///
                /// updating graphical part
                ///
                render();
                frames++;

                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    // TODO to remove
                    frame.setTitle(frames + " fps");
                    updates = 0;
                    frames = 0;
                }
            }
        }
         */
        stop();
    }

    /**
     * Updates the game, equivalent to the concept of "step next".
     */
    private void update() {
        key.update();
        background.update();
        screen.setMapOffset(background.getMapOffset());

        // TODO to remove
        mobs.forEach(Mob::update);
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

        screen.clear();
        background.render(screen);

        // TODO to remove
        mobs.forEach((mob) -> mob.render(screen));

        // Updating pixels that compose the image to be drawn on the JFrame
        System.arraycopy(screen.getPixels(), 0, pixels, 0, pixels.length);

        // Drawing image on the JFrame
        Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();
        g2.setColor(Color.BLACK);
        g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        // TODO to remove
        // g2.setColor(Color.WHITE);
        // g2.setFont(new Font("Verdana", Font.PLAIN, 10));
        // g2.drawString(frame.getTitle(), 5, 15);
        //

        g2.dispose();

        // makes the next available buffer of BufferStrategy visible
        bs.show();
    }
}
