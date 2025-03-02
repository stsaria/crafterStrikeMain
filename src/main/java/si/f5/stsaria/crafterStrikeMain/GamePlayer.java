package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class GamePlayer {
    private final OfflinePlayer player;
    private int kill = 0;
    private int death = 0;
    private int money = 0;
    public GamePlayer(Player player){
        this.player = player;
    }
    public Player getPlayer(){
        return Bukkit.getPlayer(Objects.requireNonNull(this.player.getName()));
    }
    public int getMoney(){
        return this.money;
    }
    public int getKill(){
        return this.kill;
    }
    public int getKillDeathRate(){
        return this.death > 0 ? this.kill/this.death : this.kill;
    }
    public void addKill(){
        this.kill++;
        this.money += Constants.KILL_MONEY;
    }
    public void addDeath(){
        this.death++;
    }
    public void addMoney(int money){
        this.money += money;
    }
}
