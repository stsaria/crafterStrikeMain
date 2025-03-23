package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import si.f5.stsaria.crafterStrikeMain.items.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RadioChatGui implements Listener {
    private final Inventory inventory;
    public RadioChatGui(){
        this.inventory = Bukkit.createInventory(null, 9, "Radio Chat");
        Game.configGetStringList("radioChatMessages").forEach(m -> this.inventory.addItem(new RadioChatMessageI(m).getItemStack()));
    }
    public void open(final HumanEntity human) {
        human.openInventory(this.inventory);
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
            p.sendMessage(gP.getPlayer().getDisplayName()+" -> "+ Objects.requireNonNull(item.getItemMeta()).getDisplayName())
        );
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }
}