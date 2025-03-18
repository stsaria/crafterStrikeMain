package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Players {
    public static void message(String message){
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(message, null));
    }
    public static void sound(Sound sound, float volume, float pitch){
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p, sound, volume, pitch));
    }
    public static void stopAllSounds(){
        Bukkit.getOnlinePlayers().forEach(Player::stopAllSounds);
    }
}