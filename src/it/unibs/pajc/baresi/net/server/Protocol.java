package it.unibs.pajc.baresi.net.server;

import it.unibs.pajc.baresi.level.Level;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Protocol implements Runnable {
    private static HashMap<String, Consumer<ClientEvent>> commandMap = new HashMap<>();
    private Level level;

    static {
        commandMap.put("@addTroop", e -> e.getPlayer().addTroop(e.getParameters()));
        commandMap.put("@bares", e -> e.getPlayer().bares());
    }

    private boolean isRunning = true;
    private Socket client;
    private ObjectOutputStream out;

    // client money
    private int playerNo;

    public Protocol(Socket client, int playerNo, Level level) {
        this.client = client;
        this.playerNo = playerNo;
        this.level = level;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                ) {
            out = new ObjectOutputStream(client.getOutputStream());

            System.out.println("Client connected: " + client.getPort());

            String request;

            sendLevel(level);

            while ((request = in.readLine()) != null && isRunning) {
                System.out.println("Processing request: " + request);

                ClientEvent e = ClientEvent.parse(this, request);
                Consumer<ClientEvent> commandExe =
                        e.getCommand() != null && commandMap.containsKey(e.getCommand()) ?
                                commandMap.get(e.getCommand()) :
                                commandMap.get("@default");
                commandExe.accept(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // clientList.remove(this);
            try {
                System.out.println("close");
                // this.client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void addTroop(ArrayList<String> parameters) {
        if (level != null) {
            switch (parameters.get(0)) {
                case "miniGolem" -> {
                    level.addTroop(Level.Troop.MINI_GOLEM, playerNo);
                }
                case "adventurer" -> level.addTroop(Level.Troop.ADVENTURER, playerNo);
                case "dragon" -> level.addTroop(Level.Troop.DRAGON, playerNo);
                case "golem" -> level.addTroop(Level.Troop.GOLEM, playerNo);
            }
        }
    }

    protected void bares() {
        if (level != null)
            level.bares();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void sendLevel(Level level) {
        try {
            out.reset();
            out.writeObject(level);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
