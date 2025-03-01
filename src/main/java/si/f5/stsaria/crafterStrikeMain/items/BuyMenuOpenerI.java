package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import si.f5.stsaria.crafterStrikeMain.BuyGui;

import java.util.Objects;

public class BuyMenuOpenerI extends GameItem implements Listener {
    public final

    @Override
    Material MATERIAL() {
        return Material.COMPASS;
    }

    @Override
    String NAME() {
        return "ショップ";
    }

    @Override
    String ABOUT() {
        return "";
    }

    @EventHandler
    public void ballFiring(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (!Objects.requireNonNull(e.getItem()).equals(this.getItemStack())) return;
        e.setCancelled(true);
        new BuyGui().open(e.getPlayer());
    }
}
