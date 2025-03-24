package si.f5.stsaria.crafterStrikeMain.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import si.f5.stsaria.crafterStrikeMain.items.IronABI;
import si.f5.stsaria.crafterStrikeMain.items.NetheriteABI;
import si.f5.stsaria.crafterStrikeMain.items.SandABI;

import java.util.ArrayList;
import java.util.List;

public abstract class BGui {
    protected final Inventory inventory;

    abstract String TITLE();
    abstract List<ItemStack> ITEM_STACKS();

    public BGui(){
        this.inventory = Bukkit.createInventory(null, 9, this.TITLE());
        ITEM_STACKS().forEach(this.inventory::addItem);
    }
    public void open(final HumanEntity human) {
        human.openInventory(this.inventory);
    }
}
