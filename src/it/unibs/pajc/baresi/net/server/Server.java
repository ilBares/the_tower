package it.unibs.pajc.baresi.net.server;

import it.unibs.pajc.baresi.level.Level;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // number of clients necessary to play the game
    public static final int CLIENT_NUMBER = 2;

    // port number of the service
    public static final int PORT = 1234;

    /**
     * Main method of the Server.
     */
    public static void main(String[] args) {
        // number of clients connected
        int connected = 0;

        System.out.println("Server is running...");

        // one protocol for each player
        Protocol[] players = new Protocol[CLIENT_NUMBER];

        try ( ServerSocket server = new ServerSocket(PORT) ) {

            // level of the two clients
            Level level = new Level(true);

            while (connected < CLIENT_NUMBER) {
                Socket client = server.accept();

                // defining one player
                players[connected] = new Protocol(client, connected, level);
                Thread clientThread = new Thread(players[connected]);
                clientThread.start();

                connected++;
            }

            // Object used to handle the two players
            NetLevel netLevel = new NetLevel(players[0], players[1], level);
            netLevel.scheduleUpdate();

        } catch (IOException e) {
            System.err.println("Communication error: " + e);
        }
    }
}
