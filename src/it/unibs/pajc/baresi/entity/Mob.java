package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Sprite;

public class Mob extends Entity {

    /*
     * ogni truppa ha un tempo di caricamento (almeno mezzo secondo)
     * quando mando la richiesta al server per la truppa, aggiorna tutti
     * i client con la truppa da aggiungere e il momento in cui ha ricevuto la richiesta
     * (currentTimeMillis) oppure usando i nanoseconds
     * quando i client ricevono la richiesta, settano il timer mancante per il caricamento
     * della truppa a quello necessario per arrivare al suo tempo di caricamento.
     * es in secondi:
     *
     * Client1 richiede una truppa al secondo 1 con tempo di caricamento 50
     * il server riceve la richiesta al secondo 3
     * il server invia a tutti l'aggiornamento con (truppa, 3)
     * il Client1 riceve la richiesta al secondo 5, setta il timer a 50 + 3
     * il Client2 riceve la richiesta al secondo 6, setta il timer a 50 + 3
     * per sapere quanto manca basta fare (tempoCaricamento - (currentTimeMillis - timerServer)
     *  es: 50 - (6 - 3)
     *
     * usa currentTimeMillis (NON System.nanoTime())
     */
    protected Sprite sprite;

    // TODO add Rectangle

    // TODO change with Enum
    // directions:
    // 0 = right
    // 1 = left
    protected int direction;

    // TODO change with Enum
    // states:
    // 0 = idle
    // 1 = moving
    // 2 = attack
    // 3 = dying
    protected int state;

    public void idle() {
        // set state to idle
        // set dx to 0
    }

    public void move() {
        // set state to move
        // set dx to 1
    }

    public void attack() {
        // set state to attack
        // set dx to 0
    }

    public void death() {
        // set state to death
        remove();
    }
}
