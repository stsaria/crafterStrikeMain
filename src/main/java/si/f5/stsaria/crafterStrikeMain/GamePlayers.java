package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GamePlayers {
    private static final ArrayList<GamePlayer> players = new ArrayList<>();
    public static GamePlayer add(Player player){
        synchronized (GamePlayers.class){
            players.add(new GamePlayer(player));
            return players.getLast();
        }
    }
    public static void remove(Player player){
        synchronized (GamePlayers.class){
            players.forEach(p -> {if (p.getName().equals(player.getName())) players.remove(p);});
        }
    }
    public static GamePlayer get(Player player){
        synchronized (GamePlayers.class){
            for (GamePlayer gamePlayer : players) {
                if (gamePlayer.getPlayer().getName().equals(player.getName())) return gamePlayer;
            }
        }
        return null;
    }
    public static List<GamePlayer> getAll(){
        synchronized (GamePlayers.class){
            return new ArrayList<>(players);
        }
    }
    public static void setItem(int slot, ItemStack itemStack){
        synchronized (GamePlayers.class) {
            players.forEach(p -> p.getPlayer().getInventory().setItem(slot, itemStack));
        }
    }
}
