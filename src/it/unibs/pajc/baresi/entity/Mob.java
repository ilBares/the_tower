package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.sprite.Sprite;
import it.unibs.pajc.baresi.input.Mouse;

import java.awt.*;

public class Mob extends Entity {

    public static final int GAP = 18;
    private double dx;
    private boolean moving;
    private int anim = 0;
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
    protected Sprite model;
    protected Sprite[] sprites;
    private int count;
    private int index;

    public Mob(int x, int y, double dx, Sprite model) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.model = model;
        // TODO to replace with enum
        state = 0;
        index = 0;
        idle();
    }
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
    protected boolean first;

    public void idle() {
        sprites = model.getIdle();
        count = sprites.length;
        state = 0;
        // set state to idle
        moving = false;
        anim = 0;
    }

    public void move() {
        sprites = model.getMove();
        count = sprites.length;
        state = 1;
        moving = true;
        // set state to move
        // set dx to 1
        // if !collision
        anim = 0;
    }

    public void attack() {
        sprites = model.getAttack();
        count = sprites.length;
        state = 2;
        moving = false;
        // set state to attack
        // set dx to 0
        anim = 0;
    }

    public void death() {
        sprites = model.getDeath();
        count = sprites.length;
        state = 3;
        moving = false;
        // set state to death
        anim = 0;
        remove();
    }

    boolean wait = false;
    @Override
    public void update() {

        if (!wait) {
            if (Mouse.getButton() == 1) {
                wait = true;
                if (state == 0 || state == 3) {
                    attack();
                } else {
                    death();
                }
            }

            if (Mouse.getButton() == 3) {
                wait = true;
                if (state == 0) {
                    move();
                } else {
                    idle();
                }
            }
        } else if (Mouse.getButton() == -1){
            wait = false;
        }

        // todo remove
        if (moving) x += dx;

        index = (++anim / (GAP)) % count;
        anim %= GAP * count;
    }

    @Override
    public void render(Screen screen) {
        screen.renderMob((int) x, (int) y, sprites[index]);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, model.getSize() / 2, model.getSize() / 2);
    }

    private boolean collision() {
        boolean collision = false;

        return collision;
    }
}
