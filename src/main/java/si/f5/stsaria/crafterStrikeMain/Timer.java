package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public class Timer {
    private final BossBar bossBar;
    private final int second;
    private int restSecond;
    public Timer(int second) {
        this.second = second;
        this.restSecond = second;
        this.bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
    }
    public boolean countDown(){
        if (restSecond<1) return false;
        this.restSecond--;
        this.bossBar.setProgress((double) (second - restSecond) /restSecond);
        this.bossBar.setTitle("残り時間: "+second/60+"分"+second%60+"秒");
        return true;
    }
    public void removeAll() {
        bossBar.removeAll();
    }
}
