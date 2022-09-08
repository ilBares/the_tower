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
 */
public class Level implements Serializable {

    public enum Troop {
        MINI_GOLEM, ADVENTURER, DRAGON, GOLEM
    }

    private enum Enemy {
        SKELETON, GHOUL
    }

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


    ///
    /// Constructor
    ///
    public Level(boolean multiplayer) {
        // TODO
        troopSpawn = new Point(0, 342);
        enemySpawn = new Point(1200, 343);

        timer = System.currentTimeMillis();

        entityList = new EntityList(troopSpawn, enemySpawn);

        players = new ArrayList<>();

        players.add(new Player("Player 1", START_MONEY, MAX_MONEY));

        if (multiplayer)
            players.add(new Player("Player 2", START_MONEY, MAX_MONEY));

    }

    ///
    /// Methods exposed to add mobs
    ///

    public void addTroop(Troop troop) {
        addTroop(troop, 0);
    }

    public void addTroop(Troop troop, int playerNo) {
        Mob mob = null;

        switch (troop) {
            case MINI_GOLEM -> mob = new Mob(
                    troopSpawn,
                    Mob.VERY_FAST,
                    50,
                    40,
                    25,
                    GameSound.MINI_GOLEM_ATTACK,
                    new MiniGolemSprite(64)
            );
            case ADVENTURER -> mob = new Mob(
                    troopSpawn,
                    Mob.MEDIUM,
                    125,
                    50,
                    75,
                    GameSound.ADVENTURER_ATTACK,
                    new AdventurerSprite(64)
            );
            case DRAGON -> mob = new Mob(
                    troopSpawn,
                    Mob.FAST,
                    200,
                    80,
                    150,
                    GameSound.DRAGON_ATTACK,
                    new DragonSprite(64)
            );
            case GOLEM -> mob = new Mob(
                   troopSpawn,
                   Mob.SLOW,
                   450,
                   20,
                   300,
                   GameSound.GOLEM_ATTACK,
                   new GolemSprite(64)
            );
        }

        synchronized (this) {
            if (players.get(playerNo).subMoney(mob.getPrice()))
                entityList.addTroop(mob);
        }
    }

    private void addEnemy(Enemy enemy) {
        Mob mob = null;

        switch (enemy) {
            case SKELETON -> mob = new Mob(
                    enemySpawn,
                    - Mob.MEDIUM,
                    125,
                    30,
                    50,
                    GameSound.SKELETON_ATTACK,
                    new SkeletonSprite(64));
            case GHOUL -> mob = new Mob(
                    enemySpawn,
                    - Mob.SLOW,
                    350,
                    30,
                    200,
                    GameSound.GHOUL_ATTACK,
                    new GhoulSprite(64));
        }

        synchronized (this) {
            entityList.addEnemy(mob);
        }
    }

    ///
    /// Getters
    ///
    public int getMoney() {
        return players.get(0).getMoney();
    }

    public int getMoney(int playerNo) {
        return players.get(playerNo).getMoney();
    }

    public EntityList getEntityList() {
        return entityList;
    }

    ///
    /// Updating and Rendering
    ///
    // TODO CHANGE

    /**
     *
     * @return 1 -> Win
     *         0 -> Playing
     *         -1 -> Game Over
     */
    public int update() {
        updateMoney();


        // TODO to remove
        // troops.forEach(Mob::update);
        // enemies.forEach(Mob::update);

        entityList.update();

        updateState(entityList.getTroopsIterator(), entityList.getFirstEnemy(), true);
        updateState(entityList.getEnemiesIterator(), entityList.getFirstTroop(), false);

        /*
        for (Mob m : troops) {
            if (m.isRemoved()) troops.remove(m);
            if (m.getPrev() == null || m.getPrev().isRemoved()) {
                if (enemies.size() > 0 && enemies.getFirst().isMoving() && m.getBounds().intersects(enemies.getFirst().getBounds())) {
                    m.attack(enemies.getFirst());
                    enemies.getFirst().attack(m);
                    if (m.isRemoved()) {
                        enemies.getFirst().move();
                    }
                    if (enemies.getFirst().isRemoved()) m.move();
                }
            } else if(m.getPrev() != null && m.isMoving() && m.getBounds().intersects(m.getPrev().getBounds())) {
                m.idle();
            }
        }
         */


        Random random = new Random();

        // TODO BETTER
        if (entityList.enemyNumber() < 5 && timer%random.nextInt(1, (int) (15_000 - Math.min(0.05 * timer, 8_000))) == 0) {
            if (random.nextInt(5) != 0) addEnemy(Enemy.SKELETON);
            else addEnemy(Enemy.GHOUL);
        }

        if (!entityList.getTower().isAlive())
            return 1;

        // TODO
        if (!entityList.getHeart().isAlive())
            return -1;
        return 0;
    }

    private void updateState(Iterator<Mob> iterator, Mob firstOpponent, boolean troop) {

        while (iterator.hasNext()) {
            Mob mob = iterator.next();

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

    private void updateMoney() {
        if (System.currentTimeMillis() - timer >= 2_000) {
            timer += 2_000;

            // to avoid pause problem
            timer = System.currentTimeMillis();

            players.forEach(p -> p.addMoney(MONEY_OFFSET));
        }
    }

    public void bares() {
        entityList.killEnemies();
    }

    public void render(Screen screen) {
        
        // TODO bomb.render(screen);

        entityList.render(screen);
    }

    public void win() {
        entityList.win();
    }
}
