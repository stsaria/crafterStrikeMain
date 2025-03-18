package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import si.f5.stsaria.crafterStrikeMain.AdvEnchantment;
import si.f5.stsaria.crafterStrikeMain.Game;
import si.f5.stsaria.crafterStrikeMain.GamePlayer;
import si.f5.stsaria.crafterStrikeMain.BuyGui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuyMenuOpenerI extends BItem implements Listener {
    @Override
    public Material MATERIAL() {
        return Material.COMPASS;
    }

    @Override
    String NAME() {
        return Game.configGetString("wordShop");
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
        else if (!Objects.requireNonNull(Objects.requireNonNull(e.getItem()).getData()).equals(this.itemStack.getData())) return;
        e.setCancelled(true);
        new BuyGui().open(e.getPlayer());
    }
}
