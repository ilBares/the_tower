package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.controller.Game;
import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.asset.sprite.Sprite;
import it.unibs.pajc.baresi.level.Level;
import it.unibs.pajc.baresi.sound.GameSound;

import java.awt.*;
import java.io.Serializable;

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

public class Mob extends Entity implements Serializable {

    public enum State {
        IDLE, MOVE, ATTACK, DEATH
    }

    public static final double SLOW = 0.20;
    public static final double MEDIUM = 0.25;
    public static final double FAST = 0.30;
    public static final double VERY_FAST = 0.35;

    private Level.Category category;

    // public static final int GAP = 15;
    private double dx;
    // private int counter;

    protected Sprite model;
    // the server does not send that array
    transient private Sprite[] sprites;
    private double damage;
    private int price;
    private int soundIndex;

    private Mob prev;
    private long timer;
    private int anim;

    protected State state;

    private boolean wait;


    ///
    /// Constructor
    ///
    public Mob(Level.Category category, Point spawn,  double speed, double health, double damage, int price, int soundIndex, Sprite model) {
        this.x = spawn.getX();
        this.y = spawn.getY();

        this.category = category;

        this.dx = speed;
        this.health = health;
        this.damage = damage;
        this.model = model;
        this.price = price;
        this.soundIndex = soundIndex;

        timer = System.currentTimeMillis();

        idle();
    }

    ///
    /// Mob State needed methods
    ///
    public void idle() {
        if (state != State.IDLE) {
            state = State.IDLE;
            anim = 0;
            // counter = 0;
            timer = System.currentTimeMillis();

            sprites = model.getIdle();
        }
        // set state to idle
    }

    public void move() {
        // check if the previous animation is completed
        if (anim == sprites.length - 1 && state != State.MOVE) {
            state = State.MOVE;
            anim = 0;
            // counter = 0;
            timer = System.currentTimeMillis();
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
            // counter = 0;
            timer = System.currentTimeMillis();
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
            // counter = 0;
            timer = System.currentTimeMillis();
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

        if (isMoving()) x += dx;

        if (System.currentTimeMillis() > timer + 110) {
            anim++;
            anim %= sprites.length;
            timer = System.currentTimeMillis();
        }

        if (health <= 0)
            death();

        playSE();
    }

    @Override
    public void render(Screen screen) {
        if (sprites != null)
            screen.renderAsset((int) x, (int) y, sprites[anim]);
        else
            System.out.println("NULL " + state);
    }

    ///
    /// Utilities methods
    ///
    /**
     * Plays Mob's sound effects when State is equal to {@code ATTACK}
     */
    private void playSE() {
        if (state == State.ATTACK && anim == 0 && !wait) {
            GameSound.play(soundIndex, false);
            wait = true;
        }
    }

    public void setModel(Sprite model) {
        this.model = model;
        switch (state) {
            case IDLE -> sprites = model.getIdle();
            case MOVE -> sprites = model.getMove();
            case ATTACK -> sprites = model.getAttack();
            case DEATH -> sprites = model.getDeath();
        }
    }

    public Level.Category getCategory() {
        return category;
    }
}


