package it.unibs.pajc.baresi.net.server;

import it.unibs.pajc.baresi.level.Level;

import java.util.Arrays;

/**
 * NetLevel used to handle two players and their level.
 */
public class NetLevel {

    private Level level;
    private Protocol[] clientList = new Protocol[2];

    ///
    /// Constructor
    ///

    /**
     * Constructor of NetLevel class.
     *
     * @param a     Protocol of Player A
     * @param b     Protocol of Player B
     * @param level shared level
     */
    public NetLevel(Protocol a, Protocol b, Level level) {
        this.level = level;
        clientList[0] = a;
        clientList[1] = b;
    }

    ///
    /// Updating level
    ///

    /**
     * Used to schedule an update 120 times per second.
     */
    public void scheduleUpdate() {

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();

        // used to count fps
        double ups = 120.;

        // 1_000_000_000.0 indicates 1 second,
        // "ns" indicates every how many seconds
        // there will be an update
        final double ns = 1_000_000_000.0 / ups;

        double delta = 0;

        // the update continues even if only one player remains connected
        while ((clientList[0].isRunning() || clientList[1].isRunning()) && (level.getState() != 1 && level.getState() != -1)) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                delta--;

                ///
                /// updating logical part
                ///
                update();

                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                }
            }
        }
    }

    /**
     * Update method invoked by {@code scheduleUpdate}.
     */
    public void update() {
        level.update();

        Arrays.asList(clientList).forEach(p -> {
            if (p.isRunning())
                p.sendLevel(level);
        });
    }
}
