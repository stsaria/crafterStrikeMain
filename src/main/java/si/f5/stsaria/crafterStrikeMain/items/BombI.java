package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import si.f5.stsaria.crafterStrikeMain.Game;
import si.f5.stsaria.crafterStrikeMain.GamePlayers;

import java.util.Objects;
import java.util.Random;

public class BombI extends BItem implements Listener {
    @Override
    public Material MATERIAL() {
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
    public void getPlacedBlock(BlockPlaceEvent e) {
        if (!e.getItemInHand().equals(this.getItemStack())) return;
        e.setCancelled(true);
        Game.bombPlantPlayer = GamePlayers.get(e.getPlayer());
        Game.bombPlantCode = String.valueOf(new Random().nextInt(1000000));
        Game.bombPlantLocation = e.getBlock().getLocation();
        e.getPlayer().sendMessage("爆弾コードをチャットに打て", "コード:"+Game.bombPlantCode);
    }
}
