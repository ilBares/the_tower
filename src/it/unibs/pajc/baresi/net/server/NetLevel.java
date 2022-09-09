package it.unibs.pajc.baresi.net.server;

import it.unibs.pajc.baresi.level.Level;

import java.util.Arrays;

/**
 * NetLevel
 */
public class NetLevel {

    private Level level;
    private Protocol[] clientList = new Protocol[2];

    public NetLevel(Protocol a, Protocol b, Level level) {
        this.level = level;
        clientList[0] = a;
        clientList[1] = b;
    }

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

    public void update() {
        level.update();

        Arrays.asList(clientList).forEach(p -> {
            if (p.isRunning())
                p.sendLevel(level);
        });
    }
}
