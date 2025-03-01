package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import si.f5.stsaria.crafterStrikeMain.items.IronABI;
import si.f5.stsaria.crafterStrikeMain.items.NetheriteABI;
import si.f5.stsaria.crafterStrikeMain.items.SandABI;

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
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(this.inventory)) return;
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType().isAir()) return;
        e.getWhoClicked().getInventory().setItem(8, item);
    }
}
