package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import si.f5.stsaria.crafterStrikeMain.items.IronA;
import si.f5.stsaria.crafterStrikeMain.items.NetheriteA;
import si.f5.stsaria.crafterStrikeMain.items.SandA;

public class BuyGui implements Listener {
    private final Inventory inventory;
    public BuyGui(){
        this.inventory = Bukkit.createInventory(null, 9, "Buy Item");
        this.inventory.addItem(new SandA().getItemStack());
        this.inventory.addItem(new IronA().getItemStack());
        this.inventory.addItem(new NetheriteA().getItemStack());
    }
}
