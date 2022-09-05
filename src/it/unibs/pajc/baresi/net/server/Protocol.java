package it.unibs.pajc.baresi.net.server;

import it.unibs.pajc.baresi.level.Level;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Protocol implements Runnable {
    private static HashMap<String, Consumer<ClientEvent>> commandMap = new HashMap<>();

    static {
        commandMap.put("@addTroop", e -> e.getPlayer().addTroop(e.getParameters()));
        commandMap.put("@bares", e -> e.getPlayer().bares());
    }

    private boolean isRunning = true;
    private Socket client;
    private BufferedReader in;
    private ObjectOutputStream out;

    // game parameters
    private ArrayList<Protocol> clientList = new ArrayList<>();
    protected static Level level;
    // client money
    private int playerNo;

    public Protocol(Socket client, int playerNo) {
        this.client = client;
        this.playerNo = playerNo;
        clientList.add(this);
    }

    @Override
    public void run() {
        try {

            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new ObjectOutputStream(client.getOutputStream());

            System.out.println("Client connected: " + client.getPort());

            String request;

            while (isRunning) {
                if ((request = in.readLine()) != null) {
                    System.out.println("Processing request: " + request);

                    ClientEvent e = ClientEvent.parse(this, request);
                    Consumer<ClientEvent> commandExe =
                            e.getCommand() != null && commandMap.containsKey(e.getCommand()) ?
                                    commandMap.get(e.getCommand()) :
                                    commandMap.get("@default");
                    commandExe.accept(e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clientList.remove(this);
            try {
                this.client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void addTroop(ArrayList<String> parameters) {
        switch (parameters.get(0)) {
            case "miniGolem" -> {
                level.addTroop(Level.Troop.MINI_GOLEM, playerNo);
            }
            case "adventurer" -> level.addTroop(Level.Troop.ADVENTURER, playerNo);
            case "dragon" -> level.addTroop(Level.Troop.DRAGON, playerNo);
            case "golem" -> level.addTroop(Level.Troop.GOLEM, playerNo);
        }

        update();
    }

    protected void bares() {
        level.bares();
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

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;

            while (delta >= 1) {
                delta--;

                ///
                /// updating logical part
                ///
                update();

                if (System.currentTimeMillis() - timer > 1000)
                    timer += 1000;
            }
        }
    }

    public void update() {
        System.out.println("Update");
        // TODO print Level
        // TODO update game
        clientList.forEach(c -> {
            try {
                c.out.writeObject(level);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // update game in each client
    }
}
