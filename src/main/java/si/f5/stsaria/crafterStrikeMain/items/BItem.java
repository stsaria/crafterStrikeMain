package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class BItem {
    protected final ItemStack itemStack;

    public abstract Material MATERIAL();
    abstract String NAME();
    abstract String ABOUT();

    public BItem() {
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
    public void bindingEnchant(){
        this.itemStack.addEnchantment(Enchantment.BINDING_CURSE, 1);
    }

}
