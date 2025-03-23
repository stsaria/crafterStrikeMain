package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import si.f5.stsaria.crafterStrikeMain.AdvEnchantment;
import si.f5.stsaria.crafterStrikeMain.Game;
import si.f5.stsaria.crafterStrikeMain.RadioChatGui;

import java.util.ArrayList;
import java.util.List;

public class RadioChatOpenerI extends BItem implements Listener {
    @Override
    public Material MATERIAL() {
        return Material.ACACIA_SIGN;
    }

    @Override
    String NAME() {
        return Game.configGetString("wordRadioChat");
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
        else if (!e.getItem().equals(this.itemStack)) return;
        e.setCancelled(true);
        new RadioChatGui().open(e.getPlayer());
    }
}
