package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import si.f5.stsaria.crafterStrikeMain.AdvEnchantment;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class BItem {
    protected final ItemStack itemStack;

    public abstract Material MATERIAL();
    abstract String NAME();
    abstract String ABOUT();
    abstract ArrayList<AdvEnchantment> ADV_ENCHANTMENTS();

    public BItem() {
        this.itemStack = new ItemStack(this.MATERIAL());
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.setDisplayName(this.NAME());
        itemMeta.setLore(Arrays.asList(ABOUT().split("\n")));
        this.itemStack.setItemMeta(itemMeta);
        ADV_ENCHANTMENTS().forEach(aE -> this.itemStack.addEnchantment(aE.enchantment, aE.level));
    }

    public ItemStack getItemStack(){
        return itemStack.clone();
    }

}
