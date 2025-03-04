package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;

public class Players {
    public static void title (String message){
        Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle(message, null, 20, 60, 2));
    }
}