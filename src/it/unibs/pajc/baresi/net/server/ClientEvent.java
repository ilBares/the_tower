package it.unibs.pajc.baresi.net.server;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientEvent {

    private Protocol player;
    private String command;
    private ArrayList<String> parameters;

    private ClientEvent(Protocol player, String command, ArrayList<String> parameters) {
        this.player = player;
        this.command = command;
        this.parameters = parameters;
    }

    ///
    /// Commands:
    /// 1) TODO createRoom (it returns a gameCode)
    /// 2) TODO joinRoom (required the gameCode)
    /// 3) addTroop:
    ///     - miniGolem
    ///     - adventurer
    ///     - dragon
    ///     - golem
    /// 4) bares
    /// TODO if the game is client Server u can't pause it
    ///
    public static ClientEvent parse(Protocol player, String message) {
        String command = null;
        ArrayList<String> parameters = new ArrayList<>();

        if (message.startsWith("@")) {
            String[] tokens = message.split(":");
            command = tokens[0];

            parameters.addAll(Arrays.asList(tokens));
            
        }
        return new ClientEvent(player, command, parameters);
    }
    
    ///
    /// Getters and Setters
    ///

    public Protocol getPlayer() {
        return player;
    }

    public String getCommand() {
        return command;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }
}
