package it.unibs.pajc.baresi.level;

import it.unibs.pajc.baresi.entity.*;
import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.asset.sprite.*;
import it.unibs.pajc.baresi.sound.GameSound;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Level class that contains the code needed for a specific level.
 * It could be used in Single Player Mode or in Multiplayer Mode.
 */
public class Level implements Serializable {

    // necessary to create new mob
    public interface Category { }

    // Different types of troops of the current level
    public enum Troop implements Category {
        MINI_GOLEM, ADVENTURER, DRAGON, GOLEM
    }

    // Different types of enemies of the current level
    public enum Enemy implements Category {
        SKELETON, GHOUL
    }

    // enemy spawn and troop spawn
    public static Point troopSpawn;
    public static Point enemySpawn;

    public static final int MAX_MONEY = 1000;
    public static final int START_MONEY = 50;
    public static final int MONEY_OFFSET = 5;

    // entity list containing troops, enemies and tower
    private EntityList entityList;

    // used to randomly add enemies
    private long timer;

    // needed to play in single player or multiplayer
    private ArrayList<Player> players;

    // true if the mode selected is Multiplayer
    private boolean multiplayer;

    // player number
    private int playerNo;

    ///
    /// Constructor
    ///
    /**
     * Level constructor.
     *
     * @param multiplayer   true if the game mode is multiplayer
     */
    public Level(boolean multiplayer) {
        // initializing troop spawn
        troopSpawn = new Point(0, 342);
        enemySpawn = new Point(1200, 343);

        timer = System.currentTimeMillis();

        entityList = new EntityList(troopSpawn, enemySpawn);

        players = new ArrayList<>();

        // adding players
        players.add(new Player("Player 1", START_MONEY, MAX_MONEY));

        if (multiplayer)
            players.add(new Player("Player 2", START_MONEY, MAX_MONEY));

        this.multiplayer = multiplayer;

    }

    ///
    /// Methods exposed to add mobs
    ///

    /**
     * Adding troop to Entity List.
     *
     * @param troop     indicates which troop to add
     */
    public void addTroop(Troop troop) {
        addTroop(troop, 0);
    }

    /**
     * Adding troop to Entity list.
     *
     * @param troop     indicates which troop to add
     * @param playerNo  indicates player number.
     */
    public void addTroop(Troop troop, int playerNo) {
        Mob mob = null;

        // select which troops to add
        switch (troop) {
            case MINI_GOLEM -> mob = new Mob(
                    troop,
                    troopSpawn,
                    Mob.VERY_FAST,
                    50,
                    40,
                    25,
                    GameSound.MINI_GOLEM_ATTACK,
                    multiplayer ? new EmptySprite(64, 5, 8, 8, 5) : new MiniGolemSprite(64)
            );
            case ADVENTURER -> mob = new Mob(
                    troop,
                    troopSpawn,
                    Mob.MEDIUM,
                    125,
                    50,
                    75,
                    GameSound.ADVENTURER_ATTACK,
                    multiplayer ? new EmptySprite(64, 10, 8, 10, 7) : new AdventurerSprite(64)
            );
            case DRAGON -> mob = new Mob(
                    troop,
                    troopSpawn,
                    Mob.FAST,
                    200,
                    80,
                    150,
                    GameSound.DRAGON_ATTACK,
                    multiplayer ? new EmptySprite(64, 10, 10, 10, 10) : new DragonSprite(64)
            );
            case GOLEM -> mob = new Mob(
                    troop,
                    troopSpawn,
                    Mob.SLOW,
                   450,
                   20,
                   300,
                   GameSound.GOLEM_ATTACK,
                   multiplayer ? new EmptySprite(64, 7, 10, 5, 9) : new GolemSprite(64)
            );
        }

        // checks if the player has enough money to add the new troop
        if (players.get(playerNo).subMoney(mob.getPrice())) {
            entityList.addTroop(mob);
        }
    }

    ///
    /// Private methods to add enemy
    ///

    /**
     * Adding enemy to Entity list.
     *
     * @param enemy     indicates which enemy to add
     */
    private void addEnemy(Enemy enemy) {
        Mob mob = null;

        // select which enemy to add
        switch (enemy) {
            case SKELETON -> mob = new Mob(
                    Enemy.SKELETON,
                    enemySpawn,
                    - Mob.MEDIUM,
                    125,
                    30,
                    50,
                    GameSound.SKELETON_ATTACK,
                    multiplayer ? new EmptySprite(64, 10, 10, 10, 9) : new SkeletonSprite(64));
            case GHOUL -> mob = new Mob(
                    Enemy.GHOUL,
                    enemySpawn,
                    - Mob.SLOW,
                    350,
                    30,
                    200,
                    GameSound.GHOUL_ATTACK,
                    multiplayer ? new EmptySprite(64, 8, 8, 6, 6) : new GhoulSprite(64));
        }

        synchronized (this) {
            entityList.addEnemy(mob);
        }
    }

    ///
    /// Getters
    ///

    /**
     * @return the money
     */
    public int getMoney() {
        return players.get(playerNo).getMoney();
    }

    /**
     * @return the entity list
     */
    public EntityList getEntityList() {
        return entityList;
    }

    ///
    /// Updating and Rendering
    ///
    /**
     *  updates level (and entity list)
     *
     * @return 1 -> Win
     *         0 -> Playing
     *         -1 -> Game Over
     */
    public int update() {
        // necessary to update money
        updateMoney();

        entityList.update();

        // updates state of entity list
        updateState(entityList.getTroopsIterator(), entityList.getFirstEnemy(), true);
        updateState(entityList.getEnemiesIterator(), entityList.getFirstTroop(), false);

        Random random = new Random();

        // algorithm necessary to add enemies
        if (entityList.enemyNumber() < 5 && timer%random.nextInt(1, (int) (15_000 - Math.min(0.05 * timer, 8_000))) == 0) {
            if (random.nextInt(5) != 0) addEnemy(Enemy.SKELETON);
            else addEnemy(Enemy.GHOUL);
        }

        return getState();
    }

    /**
     * Updates state of the level.
     *
     * @param iterator      necessary to iterate different mobs
     * @param firstOpponent first opponent object
     * @param troop         true if mobs are troops
     */
    private void updateState(Iterator<Mob> iterator, Mob firstOpponent, boolean troop) {

        while (iterator.hasNext()) {
            Mob mob = iterator.next();

            // switches different mob state
            switch (mob.getState()) {
                case IDLE -> {
                    if (!entityList.allyCollision(mob) && (!troop || !entityList.towerCollision(mob)))
                        mob.move();
                    else if (!iterator.hasNext() && entityList.opponentCollision(mob, firstOpponent))
                        mob.attack(firstOpponent);
                }
                case MOVE -> {
                    if (entityList.allyCollision(mob))
                        mob.idle();
                    else if (troop && entityList.towerCollision(mob) && entityList.getTower().isAlive()) {
                        mob.attack(entityList.getTower());
                    }
                    else if (entityList.opponentCollision(mob, firstOpponent))
                        mob.attack(firstOpponent);
                    else if (!troop && entityList.heartCollision(mob)) {
                        entityList.getHeart().destroy();
                    }
                }
                case ATTACK -> {
                    if (troop && entityList.towerCollision(mob)) {

                        // attacks the tower if the troop collides with it
                        mob.attack(entityList.getTower());
                        if (!entityList.getTower().isAlive())
                            mob.idle();
                    }
                    else {
                        mob.attack(firstOpponent);

                        // case of Bares hack
                        if (firstOpponent == null) {
                            mob.move();
                        } else if (!firstOpponent.isAlive()) {
                            if (troop) players.forEach(p -> p.addMoney(firstOpponent.getPrice()));
                            mob.move();
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates level money every 2 seconds.
     */
    private void updateMoney() {
        if (System.currentTimeMillis() - timer >= 2_000) {
            timer += 2_000;

            // to avoid pause problem
            timer = System.currentTimeMillis();

            players.forEach(p -> p.addMoney(MONEY_OFFSET));
        }
    }

    /**
     *
     * @param screen
     */
    public void render(Screen screen) {
        entityList.render(screen);
    }

    ///
    /// utilities methods
    ///
    /**
     * Bares hack used to kill all enemies.
     */
    public void bares() {
        entityList.killEnemies();
    }

    /**
     * Invokes entityList.win() in case of win.
     */
    public void win() {
        entityList.win();
    }

    /**
     * @return      1 in case of win
     *              -1 in case of defeat
     *              0 in all others cases
     */
    public int getState() {
        if (!entityList.getTower().isAlive())
            return 1;

        if (!entityList.getHeart().isAlive())
            return -1;

        return 0;
    }

    /**
     * Sets the player number.
     * @param playerNo  player number
     */
    public void setPlayerNo(int playerNo) {
        this.playerNo = playerNo;
    }

    /**
     * @return      true if multiplayer mode.
     */
    public boolean isMultiplayer() {
        return multiplayer;
    }
}
