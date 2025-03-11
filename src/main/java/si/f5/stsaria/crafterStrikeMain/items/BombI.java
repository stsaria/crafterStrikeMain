package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import si.f5.stsaria.crafterStrikeMain.AdvEnchantment;
import si.f5.stsaria.crafterStrikeMain.Calculator;
import si.f5.stsaria.crafterStrikeMain.Game;
import si.f5.stsaria.crafterStrikeMain.GamePlayers;

import java.util.ArrayList;
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

    @Override
    ArrayList<AdvEnchantment> ADV_ENCHANTMENTS() {
        return new ArrayList<>();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
        if (!e.getBlock().getDrops().contains(this.getItemStack())) return;
        Location l = e.getBlock().getLocation();
        else if (!Calculator.isXZIncludeRange(
            Game.configGetIntList("bombPlantLocation").getFirst(),
            Game.configGetIntList("bombPlantLocation").get(1),
            (int) l.getX(),
            (int) l.getZ(),
            Game.configGetIntList("bombDeathBlockAreaXZ").getFirst(),
            Game.configGetIntList("bombDeathBlockAreaXZ").getLast()
        )) return;
        else if (Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt((int) l.getX(), (int) l.getY()-1, (int) l.getZ()).getType().equals(Material.AIR)) return;
        Game.bombPlantPlayer = GamePlayers.get(e.getPlayer());
        Game.bombPlantCode = String.valueOf(new Random().nextLong(Long.parseLong("99999999999999"))+Long.parseLong("10000000000000"));
        Game.bombPlantLocation = e.getBlock().getLocation();
        e.getPlayer().sendMessage(Game.configGetString("bombAttackCodeMessage")+Game.bombPlantCode);
    }

    @EventHandler
    public void onBlockBreak(BlockPlaceEvent e) {
        e.setCancelled(true);
        if (!e.getBlock().getDrops().contains(this.getItemStack())) return;
        else if (!e.getBlock().getLocation().equals(Game.bombPlantLocation)) return;
        Game.bombDefusePlayer = GamePlayers.get(e.getPlayer());
        Game.bombPlantCode = String.valueOf(new Random().nextLong(Long.parseLong("99999999999999"))+Long.parseLong("10000000000000"));
        e.getPlayer().sendMessage(Game.configGetString("bombDefuseCodeMessage")+Game.bombPlantCode);
    }
}
