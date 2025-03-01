package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public abstract class GameItem {
    private final ItemStack itemStack;
    abstract Material MATERIAL();
    abstract String NAME();
    abstract String ABOUT();

    public GameItem() {
        this.itemStack = new ItemStack(this.MATERIAL());
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.setDisplayName(this.NAME());
        itemMeta.setLore(Arrays.asList(ABOUT().split("\n")));
        this.itemStack.setItemMeta(itemMeta);
    }

    public ItemStack getItemStack(){
        return itemStack.clone();
    }
}
