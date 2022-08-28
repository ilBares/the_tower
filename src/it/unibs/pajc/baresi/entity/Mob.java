package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.asset.sprite.Sprite;
import it.unibs.pajc.baresi.sound.Sound;

import java.awt.*;

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

public class Mob extends Entity {

    public enum State {
        IDLE, MOVE, ATTACK, DEATH
    }

    public static final double SLOW = 0.20;
    public static final double MEDIUM = 0.25;
    public static final double FAST = 0.30;
    public static final double VERY_FAST = 0.35;

    public static final int GAP = 15;
    private double dx;
    private int counter;

    protected Sprite model;
    private Sprite[] sprites;
    private int anim;

    // TODO needed by the server
    private int msLoading;

    private double damage;
    private int price;

    protected State state;

    private Mob prev;

    private boolean wait;

    private int soundIndex;

    ///
    /// Constructor
    ///
    public Mob(Point spawn, double speed, int msLoading, double health, double damage, int price, int soundIndex, Sprite model) {
        this.x = spawn.getX();
        this.y = spawn.getY();
        this.dx = speed;
        this.msLoading = msLoading;
        this.health = health;
        this.damage = damage;
        this.model = model;
        this.price = price;
        this.soundIndex = soundIndex;

        idle();
    }


    ///
    /// Mob State needed methods
    ///
    public void idle() {
        if (state != State.IDLE) {
            state = State.IDLE;
            anim = 0;
            counter = 0;
            sprites = model.getIdle();
        }
        // set state to idle
    }

    public void move() {
        // check if the previous animation is completed
        if (anim == sprites.length - 1 && state != State.MOVE) {
            state = State.MOVE;
            anim = 0;
            counter = 0;
            sprites = model.getMove();
        }
        // set state to move
        // set dx to 1
        // if !collision
    }

    public void attack(Entity opponent) {
        if (state != State.ATTACK) {
            state = State.ATTACK;
            anim = 0;
            counter = 0;
            sprites = model.getAttack();
            wait = false;
        }

        // mob attacks only when the attack animation is ended
        if (anim == sprites.length - 1 && wait) {
            if (opponent != null && opponent.isAlive()) {
                opponent.hit(damage);
            }
            wait = false;
        }
    }

    public void death() {
        if (state != State.DEATH) {
            state = State.DEATH;
            anim = 0;
            counter = 0;
            sprites = model.getDeath();
        }

        if (anim == sprites.length - 1) {
            remove();
        }
    }


    ///
    /// Getters and Setters
    ///
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, (int) (model.getWidth() / 3. * 2), model.getHeight());
    }

    public int getPrice() {
        return price;
    }

    public Mob getPrev() {
        return prev;
    }

    public State getState() {
        return state;
    }

    public void setPrev(Mob prev) {
        this.prev = prev;
    }

    public boolean isMoving() {
        return state == State.MOVE;
    }

    ///
    /// Updating and Rendering
    ///
    @Override
    public void update() {

        // todo remove
        if (isMoving()) x += dx;

        if (isAlive() || anim != sprites.length - 1)
            anim = (++counter / (GAP)) % sprites.length;

        counter %= (GAP * sprites.length);

        //  if (state == 2 && opponent != null) opponent.hit(damage);

        if (health <= 0)
            death();

        playSE();
    }

    @Override
    public void render(Screen screen) {
        screen.renderAsset((int) x, (int) y, sprites[anim]);
    }

    ///
    /// Utilities methods
    ///

    /**
     * Plays Mob's sound effects when State is equal to {@code ATTACK}
     */
    private void playSE() {
        if (state == State.ATTACK && anim == 0 && !wait) {
            Sound.play(soundIndex, false);
            wait = true;
        }
    }
}


