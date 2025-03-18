package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GamePlayers {
    private static final ArrayList<GamePlayer> players = new ArrayList<>();
    private static final Object lock = new Object();
    public static void add(Player player){
        synchronized (lock){
            players.add(new GamePlayer(player));
            System.out.println("Added player: " + player.getName());
        }
    }
    public static GamePlayer get(Player player){
        synchronized (lock){
            for (int i = 0; i < players.size(); i++){
                if (players.get(i).getPlayer().getName().equals(player.getName())){
                    System.out.println("Retrieved player: " + player.getName() + " | Money: " + players.get(i).getMoney());  // デバッグ用
                    return players.get(i);
                }
            }
        }
        return null;
    }
    public static List<GamePlayer> getAll(){
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
