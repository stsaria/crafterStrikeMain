package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import si.f5.stsaria.crafterStrikeMain.GamePlayer;
import si.f5.stsaria.crafterStrikeMain.BuyGui;

import java.util.Objects;

public class BuyMenuOpenerI extends BItem implements Listener {
    private int money = 0;

    @Override
    public Material MATERIAL() {
        return Material.COMPASS;
    }

    @Override
    String NAME() {
        return "ショップ";
    }

    @Override
    String ABOUT() {
        return "所持金:"+money;
    }

    public void setMoney(GamePlayer player){
        money = player.getMoney();
    }

    @EventHandler
    public void ballFiring(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        else if (!Objects.requireNonNull(e.getItem().getItemMeta()).getDisplayName().equals(this.NAME())) return;
        else if (!Objects)
        e.setCancelled(true);
        new BuyGui().open(e.getPlayer());
    }
}
