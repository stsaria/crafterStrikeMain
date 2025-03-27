package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import si.f5.stsaria.crafterStrikeMain.*;

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
        return Game.configGetString("wordBomb");
    }

    @Override
    String ABOUT() {
        return "";
    }

    @Override
    ArrayList<AdvEnchantment> ADV_ENCHANTMENTS() {
        return new ArrayList<>();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
        if (!e.getBlock().getType().equals(this.MATERIAL())) return;
        Location l = e.getBlock().getLocation();
        System.out.println(l.getBlockX()+" "+l.getBlockY()+" "+l.getBlockZ());
        if (Calculator.isIncludeRange(
            Game.configGetIntList("aBombPlantLocations").getFirst(),
            Game.configGetIntList("aBombPlantLocations").get(1),
            Game.configGetIntList("aBombPlantLocations").get(2),
            Game.configGetIntList("aBombPlantLocations").get(3),
            Game.configGetIntList("aBombPlantLocations").get(4),
            Game.configGetIntList("aBombPlantLocations").getLast(),
            l.getBlockX(),
            l.getBlockY(),
            l.getBlockZ()
        )) Game.bombPlantArea = BombArea.A;
        else if (Calculator.isIncludeRange(
            Game.configGetIntList("bBombPlantLocations").getFirst(),
            Game.configGetIntList("bBombPlantLocations").get(1),
            Game.configGetIntList("bBombPlantLocations").get(2),
            Game.configGetIntList("bBombPlantLocations").get(3),
            Game.configGetIntList("bBombPlantLocations").get(4),
            Game.configGetIntList("bBombPlantLocations").getLast(),
            l.getBlockX(),
            l.getBlockY(),
            l.getBlockZ()
        )) Game.bombPlantArea = BombArea.B;
        else return;
        if (Objects.requireNonNull(Bukkit.getWorld(Game.configGetString("worldName"))).getBlockAt(l.getBlockX(), l.getBlockY()-1, l.getBlockZ()).getType().equals(Material.AIR)) return;
        Game.bombPlantPlayer = GamePlayers.get(e.getPlayer());
        Game.bombPlantCode = String.valueOf(new Random().nextLong(Long.parseLong("99999999999999"))+Long.parseLong("10000000000000"));
        Game.bombPlantLocation = e.getBlock().getLocation();
        e.getPlayer().sendMessage(Game.configGetString("bombPlantCodeMessage")+Game.bombPlantCode);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(true);
        if (!e.getBlock().getDrops().contains(this.getItemStack())) return;
        else if (!e.getBlock().getLocation().equals(Game.bombPlantLocation)) return;
        Game.bombDefusePlayer = GamePlayers.get(e.getPlayer());
        Game.bombPlantCode = String.valueOf(new Random().nextLong(Long.parseLong("99999999999999"))+Long.parseLong("10000000000000"));
        e.getPlayer().sendMessage(Game.configGetString("bombDefuseCodeMessage")+Game.bombPlantCode);
    }
}
