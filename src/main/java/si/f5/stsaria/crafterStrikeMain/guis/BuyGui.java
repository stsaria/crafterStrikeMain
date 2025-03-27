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
import si.f5.stsaria.crafterStrikeMain.Step;
import si.f5.stsaria.crafterStrikeMain.items.BuyMenuOpenerI;
import si.f5.stsaria.crafterStrikeMain.items.IronABI;
import si.f5.stsaria.crafterStrikeMain.items.NetheriteABI;
import si.f5.stsaria.crafterStrikeMain.items.SandABI;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class BuyGui extends BGui implements Listener {
    String TITLE(){
        return "Buy Item";
    }
    List<ItemStack> ITEM_STACKS(){
        return new ArrayList<>(List.of(new SandABI().getItemStack(), new IronABI().getItemStack(), new NetheriteABI().getItemStack()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!Arrays.equals(e.getInventory().getContents(), this.inventory.getContents())) return;
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType().isAir()) return;
        GamePlayer gP = GamePlayers.get((Player) e.getWhoClicked());
        if (gP == null) return;
        try {
            int price = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(item.getItemMeta()).getLore())
                        .getFirst().replace(Game.configGetString("wordPrice")+":", ""));
            if (price > gP.getMoney()){
                gP.message(Game.configGetString("cantBuyMessage"));
                return;
            }
            gP.addMoney(-price);
        } catch (Exception ignore) {
            return;
        }
        item.setAmount(60);
        gP.setItem(8, item);
        if (!Game.getStep().equals(Step.BUY_TIME)) {
            BuyMenuOpenerI openerI = new BuyMenuOpenerI(String.valueOf(gP.getMoney()));
            e.getWhoClicked().getInventory().setItem(0, openerI.getItemStack());
        }
    }
}