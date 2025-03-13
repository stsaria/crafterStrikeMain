package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public class Timer {
    private final BossBar bossBar;
    private final int tick;
    private int restTick;
    public Timer(int second) {
        this.tick = second*20;
        this.restTick = second*20;
        this.bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
    }
    public boolean countDown(){
        if (restTick<1) return false;
        this.restTick--;
        this.bossBar.setProgress((double) (tick - restTick) /restTick);
        this.bossBar.setTitle("残り時間: "+restTick/1200+"分"+restTick%1200+"秒");
        return true;
    }
    public void removeAll() {
        bossBar.removeAll();
    }
}
