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
import org.bukkit.inventory.meta.ItemMeta;
import si.f5.stsaria.crafterStrikeMain.items.BuyMenuOpenerI;
import si.f5.stsaria.crafterStrikeMain.items.IronABI;
import si.f5.stsaria.crafterStrikeMain.items.NetheriteABI;
import si.f5.stsaria.crafterStrikeMain.items.SandABI;

import java.util.Arrays;
import java.util.Objects;

public class BuyGui implements Listener {
    private final Inventory inventory;
    public BuyGui(){
        this.inventory = Bukkit.createInventory(null, 9, "Buy Item");
        this.inventory.addItem(new SandABI().getItemStack());
        this.inventory.addItem(new IronABI().getItemStack());
        this.inventory.addItem(new NetheriteABI().getItemStack());
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
        try {
            int price = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(item.getItemMeta()).getLore())
                        .getFirst().replace(Game.configGetString("wordPrice")+":", ""));
            if (price > gP.getMoney()){
                gP.getPlayer().sendMessage(Game.configGetString("cantBuyMessage"));
                return;
            }
            gP.addMoney(-price);
        } catch (Exception ignore) {
            return;
        }
        gP.getPlayer().getInventory().setItem(8, item);
        if (!Game.getStep().equals(Step.BUY_TIME)) {
            BuyMenuOpenerI openerI = new BuyMenuOpenerI();
            ItemStack openerIS = openerI.getItemStack();
            ItemMeta openerIM = openerIS.getItemMeta();
            Objects.requireNonNull(openerIM).setDisplayName(
                    openerIM.getDisplayName() + " - " + Game.configGetString("wordPocket") + ":" + gP.getMoney()
            );
            openerIS.setItemMeta(openerIM);
            e.getWhoClicked().getInventory().setItem(0, openerIS);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }
}