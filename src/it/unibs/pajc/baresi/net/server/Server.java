package it.unibs.pajc.baresi.net.server;

import it.unibs.pajc.baresi.level.Level;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int CLIENT_NUMBER = 2;
    public static final int PORT = 3030;

    public static void main(String[] args) {
        int connected = 0;
        System.out.println("Server is running...");

        try ( ServerSocket server = new ServerSocket(PORT) ) {

            while (connected < CLIENT_NUMBER) {
                Socket client = server.accept();
                Protocol p = new Protocol(client);
                Thread clientThread = new Thread(p);
                clientThread.start();
                connected++;
            }

        } catch (IOException e) {
            System.err.println("Communication error: " + e);
        }

        // TODO quando si collegano 2 giocatori avvio la partita (creo un livello nel protocol)
        // effettuo l'update ciclicamente (del protocol) e invio l'aggiornamento ad ogni client


        System.out.println("exit...");
    }
}
