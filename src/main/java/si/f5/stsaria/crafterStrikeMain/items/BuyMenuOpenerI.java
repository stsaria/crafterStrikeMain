package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import si.f5.stsaria.crafterStrikeMain.AdvEnchantment;
import si.f5.stsaria.crafterStrikeMain.Game;
import si.f5.stsaria.crafterStrikeMain.BuyGui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuyMenuOpenerI extends BItem implements Listener {
    private final int money;
    public BuyMenuOpenerI(int money) {
        super();
        this.money = money;
    }

    @Override
    public Material MATERIAL() {
        return Material.COMPASS;
    }

    @Override
    String NAME() {
        return Game.configGetString("wordShop")+" - "+Game.configGetString("wordPocket")+":"+this.money;
    }

    @Override
    String ABOUT() {
        return "";
    }

    @Override
    ArrayList<AdvEnchantment> ADV_ENCHANTMENTS() {
        return new ArrayList<>(List.of(new AdvEnchantment(Enchantment.BINDING_CURSE, 1)));
    }

    @EventHandler
    public void ballFiring(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        ItemStack item = e.getItem().clone();
        ItemMeta itemMeta = item.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(Game.configGetString("wordShop")+" - "+Game.configGetString("wordPocket")+":");
        item.setItemMeta(itemMeta);
        if (!item.equals(this.itemStack)) return;
        e.setCancelled(true);
        new BuyGui().open(e.getPlayer());
    }
}
