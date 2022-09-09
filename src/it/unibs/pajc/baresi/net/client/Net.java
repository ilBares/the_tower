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

public class Net implements Runnable {
    private static Level netLevel;
    private static PrintWriter out;

    @Override
    public void run() {
        String serverName = "localHost";
        int port = 1234;

        try (
                Socket server = new Socket(serverName, port);
                ObjectInputStream in = new ObjectInputStream(server.getInputStream())
                ){

                out = new PrintWriter(server.getOutputStream(), true);
            Object input;

            while (true) {
                input = in.readObject();

                if (input instanceof Level) {
                    if (input != null) {
                        Level receivedLevel = (Level) input;

                        if (receivedLevel != null) {
                            receivedLevel.getEntityList().getHeart().setAssets(HeartAsset.HEART);
                            Tower tower = receivedLevel.getEntityList().getTower();
                            tower.setAssets(TowerAsset.getAsset(tower.getState()));

                            for (Mob troop : receivedLevel.getEntityList().getTroops()) {
                                troop.setModel(Sprite.getModel(troop.getCategory()));
                            }
                            for (Mob enemy : receivedLevel.getEntityList().getEnemies()) {
                                enemy.setModel(Sprite.getModel(enemy.getCategory()));
                            }
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

    public static Level getNetLevel() {
        return netLevel;
    }

    public static void addTroop(Level.Troop troop) {

        if (out != null) {
            out.println("@addTroop:" + troop.name().toLowerCase());
        }
    }

    public static void quit() {
        out.println("quit");
    }
}
