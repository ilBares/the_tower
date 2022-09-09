package it.unibs.pajc.baresi.net.server;

import it.unibs.pajc.baresi.level.Level;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Protocol class used to communicate through sockets.
 */
public class Protocol implements Runnable {
    // command map that will contain all possible commands sent by clients
    private static HashMap<String, Consumer<ClientEvent>> commandMap = new HashMap<>();

    static {
        commandMap.put("@addTroop", e -> e.getPlayer().addTroop(e.getParameters().get(0)));
        commandMap.put("@bares", e -> e.getPlayer().bares());
    }

    private Level level;
    private boolean isRunning = true;
    private Socket client;

    // necessary to write updated level to the client
    private ObjectOutputStream out;

    private int playerNo;

    ///
    /// Constructor
    ///

    /**
     * Protocol constructor.
     *
     * @param client    socket
     * @param playerNo  player number
     * @param level     level of the NetLevel
     */
    public Protocol(Socket client, int playerNo, Level level) {
        this.client = client;
        this.playerNo = playerNo;
        this.level = level;
    }

    @Override
    public void run() {
        try ( BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream())) ) {

            out = new ObjectOutputStream(client.getOutputStream());
            String request;

            while (isRunning && (request = in.readLine()) != null) {
                // break the loop if the client send "quit"
                if (request.equals("quit")) break;

                // parsing client request
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
            try {
                isRunning = false;
                // closes the client socket
                this.client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ///
    /// Commands methods
    ///

    /**
     * Add troop as result of "@addTroop" command.
     *
     * @param troop to add
     */
    protected void addTroop(String troop) {
        if (level != null) {
            switch (troop) {
                case "mini_golem" -> level.addTroop(Level.Troop.MINI_GOLEM, playerNo);
                case "adventurer" -> level.addTroop(Level.Troop.ADVENTURER, playerNo);
                case "dragon" -> level.addTroop(Level.Troop.DRAGON, playerNo);
                case "golem" -> level.addTroop(Level.Troop.GOLEM, playerNo);
            }
        }
    }

    /**
     * Execute bares hack if invoked.
     */
    protected void bares() {
        if (level != null)
            level.bares();
    }

    ///
    /// Utilities methods
    ///

    /**
     * @return true if the thread is running
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Send updated level to the connected client.
     *
     * @param level     of the NetLevel
     */
    public synchronized void sendLevel(Level level) {
        try {
            out.reset();
            level.setPlayerNo(playerNo);
            out.writeObject(level);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
