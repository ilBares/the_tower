package it.unibs.pajc.baresi.net.client;

import it.unibs.pajc.baresi.controller.Game;
import it.unibs.pajc.baresi.level.Level;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

    @Override
    public void run() {
        String serverName = "localHost";
        int port = 1234;

        try (
                Socket server = new Socket(serverName, port);
                PrintWriter out = new PrintWriter(server.getOutputStream(), true);
                ObjectInputStream in = new ObjectInputStream(server.getInputStream())
                ){

            Object input;

            while ((input = in.readObject()) != null) {
                if (input instanceof Level) {
                    Level level = (Level) input;
                    Game.updateLevel(level);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
