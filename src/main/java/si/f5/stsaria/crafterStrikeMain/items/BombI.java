package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import si.f5.stsaria.crafterStrikeMain.Game;
import si.f5.stsaria.crafterStrikeMain.GamePlayers;

import java.util.Objects;
import java.util.Random;

public class BombI extends BItem implements Listener {
    @Override
    Material MATERIAL() {
        return Material.TNT;
    }

    @Override
    String NAME() {
        return "爆弾";
    }

    @Override
    String ABOUT() {
        return "攻撃側の爆弾。特定の爆弾ポイントに設置する。";
    }

    @EventHandler
    public void ballFiring(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (!Objects.requireNonNull(e.getItem()).equals(this.getItemStack())) return;
        e.setCancelled(true);
        Game.bombPlantPlayerPlan = GamePlayers.get(e.getPlayer());
        Game.bombPlantCode = String.valueOf(new Random().nextInt(1000000));
        e.getPlayer().sendMessage("爆弾コードをチャットに打て", "コード:"+Game.bombPlantCode);
    }
}
