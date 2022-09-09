package it.unibs.pajc.baresi.net.client;

import it.unibs.pajc.baresi.entity.Mob;
import it.unibs.pajc.baresi.entity.Tower;
import it.unibs.pajc.baresi.graphic.asset.HeartAsset;
import it.unibs.pajc.baresi.graphic.asset.TowerAsset;
import it.unibs.pajc.baresi.graphic.asset.sprite.Sprite;
import it.unibs.pajc.baresi.level.Level;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Net class used from the client Side to communicate with server.
 * Net class implements Runnable to perform updates in parallel with the execution of the program.
 */
public class Net implements Runnable {
    // NetLevel used to update level from the Game class
    private static Level netLevel;

    // out PrintWriter used to send troops request
    private static PrintWriter out;

    public static final String SERVER_NAME = "localHost";
    public static final int PORT = 1234;

    @Override
    public void run() {

        // reads from the server every level update
        try (
                Socket server = new Socket(SERVER_NAME, PORT);
                ObjectInputStream in = new ObjectInputStream(server.getInputStream())
                ){

                out = new PrintWriter(server.getOutputStream(), true);
            Object input;

            // keep reading until the server stops sending updates
            while ((input = in.readObject()) != null) {

                // checks if the input is an instance of Level
                if (input instanceof Level) {
                    if (input != null) {
                        Level receivedLevel = (Level) input;

                        // "adjusts" the Level (adding Sprites, too heavy to be transmitted)
                        if (receivedLevel != null) {
                            // adding heart sprites
                            receivedLevel.getEntityList().getHeart().setAssets(HeartAsset.HEART);

                            // adding tower sprites
                            Tower tower = receivedLevel.getEntityList().getTower();
                            tower.setAssets(TowerAsset.getAsset(tower.getState()));

                            // adding troops sprites
                            for (Mob troop : receivedLevel.getEntityList().getTroops()) {
                                troop.setModel(Sprite.getModel(troop.getCategory()));
                            }
                            // adding enemies sprites
                            for (Mob enemy : receivedLevel.getEntityList().getEnemies()) {
                                enemy.setModel(Sprite.getModel(enemy.getCategory()));
                            }
                            // adding dead sprites
                            for (Mob dead : receivedLevel.getEntityList().getDead()) {
                                dead.setModel(Sprite.getModel(dead.getCategory()));
                            }
                        }
                        netLevel = receivedLevel;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    ///
    /// Utilities methods
    ///

    /**
     * @return updated net level
     */
    public static Level getNetLevel() {
        return netLevel;
    }

    /**
     * Send a request to the server with the troop to add.
     *
     * @param troop to add
     */
    public static void addTroop(Level.Troop troop) {
        if (out != null) {
            out.println("@addTroop:" + troop.name().toLowerCase());
        }
    }

    /**
     * Closes the client socket.
     */
    public static void quit() {
        out.println("quit");
    }
}
