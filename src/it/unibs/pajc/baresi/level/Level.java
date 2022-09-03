package it.unibs.pajc.baresi.level;

import it.unibs.pajc.baresi.entity.Bomb;
import it.unibs.pajc.baresi.entity.Mob;
import it.unibs.pajc.baresi.entity.EntityList;
import it.unibs.pajc.baresi.entity.Tower;
import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.asset.sprite.*;
import it.unibs.pajc.baresi.input.Keyboard;
import it.unibs.pajc.baresi.sound.Sound;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;

/**
 * Level class that contains the code needed for a specific level.
 */
public class Level implements Serializable {

    public static final int MAX_MONEY = 1000;
    public static final int MONEY_OFFSET = 5;

    private EntityList entityList;

    private long timer;
    private static int money;

    private static Point troopSpawn;
    private static Point enemySpawn;

    private Bomb bomb;

    ///
    /// Constructor
    ///
    public Level(Point troopSpawn, Point enemySpawn) {
        timer = System.currentTimeMillis();
        money = 50;

        Level.troopSpawn = troopSpawn;
        Level.enemySpawn = enemySpawn;

        entityList = new EntityList();

        Tower tower = new Tower(new Point(enemySpawn.x - 100, enemySpawn.y), 1000);

        entityList.setTower(tower);
        bomb = new Bomb(new Point(enemySpawn.x - 80, enemySpawn.y + 30));
    }

    ///
    /// Methods exposed to add mobs
    ///
    public void addMiniGolem() {
        Mob miniGolem = new Mob(
                troopSpawn,
                Mob.VERY_FAST,
                500,
                50,
                40,
                25,
                Sound.MINI_GOLEM_ATTACK,
                new MiniGolemSprite(64)
        );

        addTroop(miniGolem);
    }

    public void addAdventurer() {
        Mob adventurer = new Mob(
                troopSpawn,
                Mob.MEDIUM,
                1000,
                125,
                50,
                75,
                Sound.ADVENTURER_ATTACK,
                new AdventurerSprite(64));

        addTroop(adventurer);
    }

    public void addDragon() {
        Mob dragon = new Mob(
                troopSpawn,
                Mob.FAST,
                1500,
                200,
                80,
                150,
                Sound.DRAGON_ATTACK,
                new DragonSprite(64));

        addTroop(dragon);
    }

    public void addGolem() {
        Mob golem = new Mob(
                troopSpawn,
                Mob.SLOW,
                2000,
                450,
                20,
                300,
                Sound.GOLEM_ATTACK,
                new GolemSprite(64));

        addTroop(golem);
    }

    private void addSkeleton() {
        Mob skeleton = new Mob(
                enemySpawn,
                -Mob.MEDIUM,
                500,
                125,
                30,
                50,
                Sound.SKELETON_ATTACK,
                new SkeletonSprite(64));

        addEnemy(skeleton);
    }

    private void addGhoul() {
        Mob ghoul = new Mob(
                enemySpawn,
                -Mob.SLOW,
                1500,
                350,
                30,
                200,
                Sound.GHOUL_ATTACK,
                new GhoulSprite(64));

        addEnemy(ghoul);
    }

    private synchronized void addTroop(Mob mob) {
        // TODO require to the server
        if (money >= mob.getPrice()) {
            money -= mob.getPrice();

            entityList.addTroop(mob);
        }
    }

    private synchronized void addEnemy(Mob enemy) {
        entityList.addEnemy(enemy);
    }

    ///
    /// Getters
    ///
    public int getMoney() {
        return money;
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

        bomb.update();


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
            if (random.nextInt(5) != 0) addSkeleton();
            else addGhoul();
        }

        if (!entityList.getTower().isAlive())
            return 1;
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
                            if (troop)money += firstOpponent.getPrice();
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

            if (money + MONEY_OFFSET < MAX_MONEY) money += MONEY_OFFSET;
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
        entityList.getTower().update();
    }
}
