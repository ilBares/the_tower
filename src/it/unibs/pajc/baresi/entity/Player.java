package it.unibs.pajc.baresi.entity;

public class Player {

    private String player;
    private int money;
    public final int maxMoney;

    public Player(String player, int money, int maxMoney) {
        this.player = player;
        this.money = money;
        this.maxMoney = maxMoney;
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        if (money + amount < maxMoney)
            money += Math.abs(amount);
    }

    public boolean subMoney(int amount) {
        amount = Math.abs(amount);
        if (money >= amount) {
            money -= amount;
            return true;
        }
        return false;
    }
}
