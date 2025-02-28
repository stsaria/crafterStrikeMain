package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class BBuyItem {
    private final ItemStack itemStack;
    abstract Material MATERIAL();
    abstract String NAME();
    abstract int PRICE();

    public BBuyItem() {
        this.itemStack = new ItemStack(this.MATERIAL());
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.setDisplayName(this.NAME());
        itemMeta.setLore(List.of("値段:"+PRICE()));
        this.itemStack.setItemMeta(itemMeta);
    }

    public ItemStack getItemStack(){
        return itemStack.clone();
    }
}
