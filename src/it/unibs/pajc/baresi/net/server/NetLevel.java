package it.unibs.pajc.baresi.net.server;

import it.unibs.pajc.baresi.level.Level;

import java.util.Arrays;

public class NetLevel {

    private Level level;
    private Protocol[] clientList = new Protocol[2];

     public NetLevel(Protocol a, Protocol b, Level level) {
         this.level = level;
         clientList[0] = a;
         clientList[1] = b;
         scheduleUpdate();
     }

    public void scheduleUpdate() {
    }

    public void update() {

    }
}
