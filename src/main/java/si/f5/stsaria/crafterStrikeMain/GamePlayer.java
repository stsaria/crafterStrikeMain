package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class GamePlayer {
    private final OfflinePlayer player;
    private int kill;
    private int death;
    private int money;

    public GamePlayer(Player player){
        this.player = player;
        kill = 0;
        death = 0;
        money = 0;
    }
    public String getName(){
        return this.player.getName();
    }
    public Player getPlayer(){
        return Bukkit.getPlayer(Objects.requireNonNull(this.player.getName()));
    }
    public boolean isOnline(){
        return this.getPlayer().isOnline();
    }
    public void teleport(Location location){
        if (this.isOnline()) this.getPlayer().teleport(location);
    }
    public void setItem(int slot, ItemStack itemStack){
        if (this.isOnline()) this.getPlayer().getInventory().setItem(slot, itemStack);
    }
    public void setGameMode(GameMode gameMode){
        if (this.isOnline()) this.getPlayer().setGameMode(gameMode);
    }
    public void setHealth(double health){
        if (this.isOnline()) this.getPlayer().setHealth(health);
    }
    public void setFoodLevel(int foodLevel){
        if (this.isOnline()) this.getPlayer().setFoodLevel(foodLevel);
    }
    public void setSaturation(float saturation){
        if (this.isOnline()) this.getPlayer().setSaturation(saturation);
    }
    public double getMaxHealth(){
        if (this.isOnline()) return Objects.requireNonNull(this.getPlayer().getAttribute(Attribute.MAX_HEALTH)).getBaseValue();
        return 20D;
    }
    public GameMode getGameMode(){
        if (this.isOnline()) return this.getPlayer().getGameMode();
        return GameMode.SURVIVAL;
    }
    public Location getLocation(){
        if (this.isOnline()) return this.getPlayer().getLocation();
        return null;
    }
    public void damage(double health){
        if (this.isOnline()) this.getPlayer().damage(health);
    }

    public synchronized int getMoney(){
        return this.money;
    }
    public synchronized int getKill(){
        return this.kill;
    }
    public synchronized int getDeath(){
        return this.death;
    }
    public synchronized int getKillDeathRate(){
        return this.death > 0 ? this.kill / this.death : this.kill;
    }
    public synchronized void addKill(){
        this.addMoney(Game.configGetInt("moneyPerOneKill"));
        this.kill++;
    }
    public synchronized void addDeath(){
        this.death++;
    }
    public synchronized void addMoney(int money){
        this.money += money;
    }
}
