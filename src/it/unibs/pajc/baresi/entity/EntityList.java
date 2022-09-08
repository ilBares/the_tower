package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Screen;

import java.awt.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class EntityList implements Serializable {
    private LinkedList<Mob> troops;
    private LinkedList<Mob> enemies;
    private LinkedList<Mob> dead;
    private Tower tower;
    private Heart heart;

    ///
    /// Constructor
    ///
    public EntityList(Point troopSpawn, Point enemySpawn) {
        troops = new LinkedList<>();
        enemies = new LinkedList<>();
        dead = new LinkedList<>();
        heart = new Heart(new Point(troopSpawn.x, troopSpawn.y - 5));
        tower = new Tower(new Point(enemySpawn.x - 100, enemySpawn.y + 1), 1000);
    }

    ///
    /// Getters and Setters
    ///
    public Mob getFirstTroop() {
        return troops.size() > 0 ? troops.getFirst() : null;
    }

    public Mob getFirstEnemy() {
        return enemies.size() > 0 ? enemies.getFirst() : null;
    }

    public Mob getLastTroop() {
        return troops.size() > 0 ? troops.getLast() : null;
    }

    public Mob getLastEnemy() {
        return enemies.size() > 0 ? enemies.getLast() : null;
    }

    public Tower getTower() {
        return tower;
    }

    public Heart getHeart() {
        return heart;
    }

    public int enemyNumber() {
        return enemies.size();
    }

    public void addTroop(Mob troop) {
        troop.setPrev(getLastTroop());
        troops.add(troop);
    }

    public void addEnemy(Mob enemy) {
        enemy.setPrev(getLastEnemy());
        enemies.add(enemy);
    }

    ///
    /// Utilities methods
    ///
    public void checkCollisions() {
        Iterator<Mob> troopsIterator = troops.iterator();
        Iterator<Mob> enemiesIterator = enemies.iterator();
        Mob troop = null;
        Mob enemy = null;

        while (troopsIterator.hasNext()) {

            troop = troopsIterator.next();

            if (allyCollision(troop)) {
                troop.idle();
            } else {
                enemy.move();
            }
        }


        while (enemiesIterator.hasNext()) {

            enemy = enemiesIterator.next();

            if (allyCollision(enemy)) {
                enemy.idle();
            } else {
                enemy.move();
            }
        }
        
        if (opponentCollision(troop, enemy)) {
            troop.attack(enemy);
            enemy.attack(troop);
        }
    }

    public Iterator<Mob> getTroopsIterator() {
        return troops.iterator();
    }

    public Iterator<Mob> getEnemiesIterator() {
        return enemies.iterator();
    }

    public boolean opponentCollision(Mob mob, Mob opponent) {
        return opponent != null
                && mob.isAlive()
                && (mob.getPrev() == null || !mob.getPrev().isAlive())
                && opponent.isAlive()
                && mob.getBounds().intersects(opponent.getBounds());
    }

    public boolean allyCollision(Mob mob) {
        Mob prev = mob.getPrev();
        // check if prev is removed (not alive) as an advantage for the clash winner
        return prev != null && !prev.isRemoved() && mob.getBounds().intersects(prev.getBounds());
    }

    public void killEnemies() {
        for (Mob e : enemies) {
            e.hit(e.health);
        }
    }

    public boolean towerCollision(Mob troop) {
        return tower != null && troop.getBounds().intersects(tower.getBounds());
    }

    public boolean heartCollision(Mob enemy) {
        return enemy.getBounds().intersects(heart.getBounds());
    }

    public void update() {
        troops.forEach(Mob::update);
        enemies.forEach(Mob::update);
        tower.update();
        heart.update();

        while (troops.size() > 0 && !troops.getFirst().isAlive()) {
            dead.add(troops.removeFirst());
        }

        while (enemies.size() > 0 && !enemies.getFirst().isAlive()) {
            dead.add(enemies.removeFirst());
        }

        Iterator<Mob> deadIterator = dead.iterator();

        while (deadIterator.hasNext()) {
            Mob d = deadIterator.next();
            if (d.isRemoved())
                deadIterator.remove();
            else
                d.update();
        }
    }

    public void win() {
        tower.update();
        heart.update();
    }

    public void render(Screen screen) {
        tower.render(screen);
        heart.render(screen);
        enemies.forEach((enemy) -> enemy.render(screen));
        troops.forEach((troop) -> troop.render(screen));
        dead.forEach((dead) -> dead.render(screen));
    }
}
