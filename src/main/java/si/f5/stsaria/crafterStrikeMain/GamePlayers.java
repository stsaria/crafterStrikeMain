package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GamePlayers {
    private static final ArrayList<GamePlayer> players = new ArrayList<>();
    private static final Object lock = new Object();
    public static void add(Player player){
        synchronized (lock){
            players.add(new GamePlayer(player));
        }
    }
    public static GamePlayer get(Player player){
        synchronized (lock){
            for (GamePlayer p : players){
                if (p.getPlayer().getName().equals(player.getName())) return p;
            }
        }
        return null;
    }
    public static void setItem(int slot, ItemStack itemStack){
        synchronized (lock) {
            players.forEach(p -> p.getPlayer().getInventory().setItem(slot, itemStack));
        }
    }
    public static void distanceKill(int x, int y, int z, int d){
        synchronized (lock) {
            players.forEach( p -> {
                Location l = p.getPlayer().getLocation();
                if (Math.abs(l.getX()-x) + Math.abs(l.getY()-y) + Math.abs(l.getZ()-z) <= d) p.getPlayer().setHealth(0);
            });
        }
    }
}
