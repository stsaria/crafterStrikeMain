package si.f5.stsaria.crafterStrikeMain.guis;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import si.f5.stsaria.crafterStrikeMain.Game;
import si.f5.stsaria.crafterStrikeMain.GamePlayer;
import si.f5.stsaria.crafterStrikeMain.GamePlayers;
import si.f5.stsaria.crafterStrikeMain.items.RadioChatMessageI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RadioChatGui extends BGui implements Listener {
    String TITLE(){
        return "Radio chat";
    }
    List<ItemStack> ITEM_STACKS(){
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        Game.configGetStringList("radioChatMessages").forEach(m -> itemStacks.add(new RadioChatMessageI(m).getItemStack()));
        return itemStacks;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!Arrays.equals(e.getInventory().getContents(), this.inventory.getContents())) return;
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType().isAir()) return;
        GamePlayer gP = GamePlayers.get((Player) e.getWhoClicked());
        if (gP == null) return;
        List<Player> attackPlayers = Game.getAttackTeamPlayers();
        List<Player> defencePlayers = Game.getDefenceTeamPlayers();
        (attackPlayers.contains(gP.getPlayer()) ? attackPlayers : defencePlayers).forEach(p ->
            p.sendMessage(gP.getDisplayName()+" -> "+ Objects.requireNonNull(item.getItemMeta()).getDisplayName())
        );
    }
}