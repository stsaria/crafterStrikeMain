package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import si.f5.stsaria.crafterStrikeMain.AdvEnchantment;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class BItem {
    protected final ItemStack itemStack;
    protected final String arg;

    public abstract Material MATERIAL();
    abstract String NAME();
    abstract String ABOUT();
    abstract ArrayList<AdvEnchantment> ADV_ENCHANTMENTS();

    public BItem() {
        this.arg = "";

        this.itemStack = new ItemStack(this.MATERIAL());
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.setDisplayName(this.NAME());
        itemMeta.setLore(Arrays.asList(ABOUT().split("\n")));
        ADV_ENCHANTMENTS().forEach(aE -> itemMeta.addEnchant(aE.enchantment(), aE.level(), true));
        this.itemStack.setItemMeta(itemMeta);
    }
    public ItemStack getItemStack(){
        return itemStack.clone();
    }

}
