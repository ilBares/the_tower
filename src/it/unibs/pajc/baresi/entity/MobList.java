package it.unibs.pajc.baresi.entity;

import it.unibs.pajc.baresi.graphic.Screen;

import java.util.Iterator;
import java.util.LinkedList;

public class MobList {
    private LinkedList<Mob> troops;
    private LinkedList<Mob> enemies;
    private LinkedList<Mob> dead;
    private Tower tower;

    ///
    /// Constructor
    ///
    public MobList() {
        troops = new LinkedList<>();
        enemies = new LinkedList<>();
        dead = new LinkedList<>();
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

    public void setTower(Tower tower) {
        this.tower = tower;
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
        // TODO to remove prev.isAlive() as an advantage for winner
        // check if prev is removed (not alive) as an advantage for the clash winner
        return prev != null && !prev.isRemoved() && mob.getBounds().intersects(prev.getBounds());
    }

    public boolean towerCollision(Mob mob) {
        return tower != null && mob.getBounds().intersects(tower.getBounds());
    }

    public void update() {
        troops.forEach(Mob::update);
        enemies.forEach(Mob::update);

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

    public void render(Screen screen) {
        enemies.forEach((enemy) -> enemy.render(screen));
        troops.forEach((troop) -> troop.render(screen));
        dead.forEach((dead) -> dead.render(screen));
    }
}
