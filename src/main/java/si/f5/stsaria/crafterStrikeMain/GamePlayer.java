package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class GamePlayer {
    private final OfflinePlayer player;
    private int kill;
    private int death;
    private int money;
    private final Object lock = new Object();

    public GamePlayer(Player player){
        this.player = player;
        kill = 0;
        death = 0;
        money = 0;
    }
    public Player getPlayer(){
        return Bukkit.getPlayer(Objects.requireNonNull(this.player.getName()));
    }
    public int getMoney(){
        synchronized (lock) {
            return this.money;
        }
    }
    public int getKill(){
        synchronized (lock) {
            return this.kill;
        }
    }
    public int getDeath(){
        synchronized (lock) {
            return this.death;
        }
    }
    public int getKillDeathRate(){
        synchronized (lock) {
            return this.death > 0 ? this.kill / this.death : this.kill;
        }
    }
    public void addKill(){
        synchronized (lock) {
            this.addMoney(Game.configGetInt("moneyPerOneKill"));
            this.kill++;
        }
    }
    public void addDeath(){
        synchronized (lock) {
            this.death++;
        }
    }
    public void addMoney(int money){
        synchronized (lock) {
            this.money += money;
            System.out.println("Updated money: " + this.money);
        }
    }
}
