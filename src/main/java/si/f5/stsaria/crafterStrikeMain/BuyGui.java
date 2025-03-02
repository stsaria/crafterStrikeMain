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
import si.f5.stsaria.crafterStrikeMain.items.BuyMenuOpenerI;
import si.f5.stsaria.crafterStrikeMain.items.IronABI;
import si.f5.stsaria.crafterStrikeMain.items.NetheriteABI;
import si.f5.stsaria.crafterStrikeMain.items.SandABI;

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
        if (!e.getInventory().equals(this.inventory)) return;
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType().isAir()) return;
        GamePlayer gP = GamePlayers.get((Player) e.getWhoClicked());
        if (gP == null) return;
        try {
            int price = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(item.getItemMeta()).getLore())
                        .getFirst().replace("値段:", ""));
            if (price > gP.getMoney()) return;
            gP.addMoney(-price);
        } catch (Exception ignore) {
            return;
        }
        gP.getPlayer().getInventory().setItem(8, item);
        BuyMenuOpenerI openerI = new BuyMenuOpenerI();
        openerI.setMoney(gP);
        openerI.bindingEnchant();
        e.getWhoClicked().getInventory().setItem(0, openerI.getItemStack());
    }

    @EventHandler
    public void onInventoryClick(InventoryDragEvent e) {
        if (e.getInventory().equals(this.inventory)) {
            e.setCancelled(true);
        }
    }
}
