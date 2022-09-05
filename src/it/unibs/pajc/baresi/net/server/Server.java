package it.unibs.pajc.baresi.net.server;

import it.unibs.pajc.baresi.level.Level;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int CLIENT_NUMBER = 1;
    public static final int PORT = 1234;

    public static void main(String[] args) {
        int connected = 0;
        System.out.println("Server is running...");

        Protocol p = null;

        try ( ServerSocket server = new ServerSocket(PORT) ) {

            while (connected < CLIENT_NUMBER) {
                Socket client = server.accept();
                System.out.println("Accepted");
                p = new Protocol(client, connected++);
                Thread clientThread = new Thread(p);
                clientThread.start();
            }

        } catch (IOException e) {
            System.err.println("Communication error: " + e);
        }

        Protocol.level = new Level(true);
        // TODO effettuo l'update ciclicamente (del protocol) e invio l'aggiornamento ad ogni client

        p.scheduleUpdate();

        // System.out.println("exit...");
    }
}
