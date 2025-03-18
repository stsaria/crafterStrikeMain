package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public class Timer {
    private final BossBar bossBar;
    private final String text;
    private final int tick;
    private int restTick;
    public Timer(int second, String text) {
        this.text = text;
        this.tick = second*20;
        this.restTick = this.tick;
        this.bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }
    public int getTick(){
        return this.tick;
    }
    public int getRestTick(){
        return this.restTick;
    }
    public boolean countDown(){
        if (restTick<1) return false;
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (!this.bossBar.getPlayers().contains(p)) this.addPlayer(p);
        });
        this.restTick--;
        this.bossBar.setProgress((double) restTick / tick);
        this.bossBar.setTitle(this.text+" - "+(restTick/1200 < 10 ? "0" : "")+restTick/1200+":"+(restTick/20%60 < 10 ? "0" : "")+restTick/20%60);
        return true;
    }
    public void addPlayer(Player player){
        bossBar.addPlayer(player);
    }
    public void removeAll() {
        bossBar.removeAll();
    }
}
