package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3d;

public class Players {
    public static void setItem(int slot, ItemStack itemStack){
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().setItem(slot, itemStack);
        });
    }
    public static void distanceKill(int x, int y, int z, int d){
        Bukkit.getOnlinePlayers().forEach( player -> {
            Location l = player.getLocation();
            if (Math.abs(l.getX()-x) + Math.abs(l.getY()-y) + Math.abs(l.getZ()-z) <= d) player.setHealth(0);
        });
    }
}
