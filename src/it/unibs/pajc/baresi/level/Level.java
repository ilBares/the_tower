package it.unibs.pajc.baresi.level;

import it.unibs.pajc.baresi.entity.Bomb;
import it.unibs.pajc.baresi.entity.Mob;
import it.unibs.pajc.baresi.entity.MobList;
import it.unibs.pajc.baresi.entity.Tower;
import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.graphic.asset.sprite.*;
import it.unibs.pajc.baresi.sound.Sound;

import java.awt.*;
import java.util.Iterator;
import java.util.Random;

public class Level {

    public static final int MAX_MONEY = 1000;
    public static final int MONEY_OFFSET = 5;

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
        money = 50;

        Level.troopSpawn = troopSpawn;
        Level.enemySpawn = enemySpawn;

        mobList = new MobList();

        tower = new Tower(new Point(enemySpawn.x - 100, enemySpawn.y), 1000);

        mobList.setTower(tower);
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

        updateState(mobList.getTroopsIterator(), mobList.getFirstEnemy(), true);
        updateState(mobList.getEnemiesIterator(), mobList.getFirstTroop(), false);

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
        if (mobList.enemyNumber() < 5 && timer%random.nextInt(1, (int) (15_000 - Math.min(0.05 * timer, 8_000))) == 0) {
            if (random.nextInt(5) != 0) addSkeleton();
            else addGhoul();
        }
    }

    private void updateState(Iterator<Mob> iterator, Mob firstOpponent, boolean troop) {

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
                    else if (troop && mobList.towerCollision(mob)) {
                        mob.attack(tower);
                    }
                    else if (mobList.opponentCollision(mob, firstOpponent))
                        mob.attack(firstOpponent);
                }
                case ATTACK -> {
                    if (troop && mobList.towerCollision(mob))
                        mob.attack(tower);
                    else {
                        mob.attack(firstOpponent);
                        if (firstOpponent != null && !firstOpponent.isAlive()) {
                            if (troop )money += firstOpponent.getPrice();
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

    public void render(Screen screen) {
        tower.render(screen);
        // TODO bomb.render(screen);

        mobList.render(screen);
    }

}
