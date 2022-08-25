package it.unibs.pajc.baresi.level;

import it.unibs.pajc.baresi.entity.Bomb;
import it.unibs.pajc.baresi.entity.Mob;
import it.unibs.pajc.baresi.entity.MobList;
import it.unibs.pajc.baresi.entity.Tower;
import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.asset.sprite.*;

import java.awt.*;
import java.util.Iterator;
import java.util.Random;

public class Level {

    public static final int MAX_MONEY = 500;
    public static final int MONEY = 10;

    private MobList mobList;

    private long timer;
    private static int money;

    private static Point troopSpawn;
    private static Point enemySpawn;

    private Tower tower;
    // TODO private Bomb bomb;


    ///
    /// Constructor
    ///
    public Level(Point troopSpawn, Point enemySpawn) {
        timer = System.currentTimeMillis();
        money = 0;

        Level.troopSpawn = troopSpawn;
        Level.enemySpawn = enemySpawn;

        mobList = new MobList();

        tower = new Tower(new Point(enemySpawn.x - 100, enemySpawn.y), 1000);
        // TODO bomb = new Bomb(new Point(enemySpawn.x - 80, enemySpawn.y + 30));
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
                false,
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
                false,
                new AdventurerSprite(64));

        addTroop(adventurer);
    }

    public void addDragon() {
        Mob dragon = new Mob(
                troopSpawn,
                Mob.FAST,
                1500,
                200,
                100,
                150,
                false,
                new DragonSprite(64));

        addTroop(dragon);
    }

    public void addGolem() {
        Mob golem = new Mob(
                troopSpawn,
                Mob.SLOW,
                2000,
                400,
                80,
                300,
                false,
                new GolemSprite(64));

        addTroop(golem);
    }

    private void addSkeleton() {
        Mob skeleton = new Mob(
                enemySpawn,
                Mob.MEDIUM,
                500,
                125,
                30,
                50,
                true,
                new SkeletonSprite(64));

        addEnemy(skeleton);
    }

    private void addGhoul() {
        Mob ghoul = new Mob(
                enemySpawn,
                Mob.SLOW,
                1500,
                350,
                40,
                200,
                true,
                new GhoulSprite(64));

        addEnemy(ghoul);
    }

    private synchronized void addTroop(Mob mob) {
        // TODO require to the server
        if (money >= mob.getPrice()) {
            money -= mob.getPrice();

            mobList.addTroop(mob);
        }
    }

    private synchronized void addEnemy(Mob enemy) {
        mobList.addEnemy(enemy);
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
    public void update() {

        updateMoney();

        // TODO bomb.update();
        tower.update();


        // TODO to remove
        // troops.forEach(Mob::update);
        // enemies.forEach(Mob::update);

        mobList.update();

        updateState(mobList.getTroopsIterator(), mobList.getFirstEnemy());
        updateState(mobList.getEnemiesIterator(), mobList.getFirstTroop());

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

        if (mobList.enemyNumber() < 10 && timer%random.nextInt(1, (int) (10_000 - Math.min(0.1 * timer, 3_000))) == 0) {
            if (random.nextInt(5) != 0) addSkeleton();
            else addGhoul();
        }
    }

    private void updateState(Iterator<Mob> iterator, Mob firstOpponent) {

        while (iterator.hasNext()) {
            Mob mob = iterator.next();

            switch (mob.getState()) {
                case IDLE -> {
                    if (!mobList.allyCollision(mob))
                        mob.move();
                    else if (!iterator.hasNext() && mobList.opponentCollision(mob, firstOpponent))
                        mob.attack(firstOpponent);
                }
                case MOVE -> {
                    if (mobList.allyCollision(mob))
                        mob.idle();
                    else if (mobList.opponentCollision(mob, firstOpponent))
                        mob.attack(firstOpponent);
                }
                case ATTACK -> {
                    if (!mobList.opponentCollision(mob, firstOpponent))
                        mob.move();
                    else
                        mob.attack(firstOpponent);
                }
            }
        }
    }

    private void updateMoney() {
        if (System.currentTimeMillis() - timer >= 1_000) {
            timer += 1_000;
            if (money + MONEY < MAX_MONEY) money += MONEY;
        }
    }

    public void render(Screen screen) {
        tower.render(screen);
        // TODO bomb.render(screen);

        mobList.render(screen);
    }

}
