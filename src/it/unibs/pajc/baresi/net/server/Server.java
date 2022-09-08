package it.unibs.pajc.baresi.net.server;

import it.unibs.pajc.baresi.level.Level;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int CLIENT_NUMBER = 2;
    public static final int PORT = 1234;

    public static void main(String[] args) {
        int connected = 0;
        System.out.println("Server is running...");

        Protocol[] protocols = new Protocol[2];

        try ( ServerSocket server = new ServerSocket(PORT) ) {

            Level level = new Level(true);

            while (connected < CLIENT_NUMBER) {
                Socket client = server.accept();
                System.out.println("Accepted");
                protocols[connected] = new Protocol(client, connected++, level);
                Thread clientThread = new Thread(protocols[connected]);
                clientThread.start();
            }

            NetLevel netLevel = new NetLevel(protocols[0], protocols[1], level);
            netLevel.scheduleUpdate();

        } catch (IOException e) {
            System.err.println("Communication error: " + e);
        }

        System.out.println("exit...");
    }
}
