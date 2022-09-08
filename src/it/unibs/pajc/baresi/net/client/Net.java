package it.unibs.pajc.baresi.net.client;

import it.unibs.pajc.baresi.graphic.asset.HeartAsset;
import it.unibs.pajc.baresi.graphic.asset.TowerAsset;
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
                        netLevel = (Level) input;
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
        if (netLevel != null) {
            netLevel.getEntityList().getHeart().setAssets(HeartAsset.HEART);
            netLevel.getEntityList().getTower().setAssets(TowerAsset.INTACT);
        }
        return netLevel;
    }

    public static void addTroop(Level.Troop troop) {
        if (out != null) {
            out.print("@addTroop:" + troop.name().toLowerCase());
        }
    }
}
