package si.f5.stsaria.crafterStrikeMain;

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
    public static ArrayList<GamePlayer> getAll(){
        synchronized (lock){
            return new ArrayList<>(players);
        }
    }
    public static void setItem(int slot, ItemStack itemStack){
        synchronized (lock) {
            players.forEach(p -> p.getPlayer().getInventory().setItem(slot, itemStack));
        }
    }
}
