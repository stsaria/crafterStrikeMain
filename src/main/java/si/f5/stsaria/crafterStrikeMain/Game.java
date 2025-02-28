package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import si.f5.stsaria.crafterStrikeMain.teams.AttackT;
import si.f5.stsaria.crafterStrikeMain.teams.DefenceT;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;


public class Game extends BukkitRunnable {

    private final JavaPlugin plugin;
    private Step step;
    private Timer timer;
    private AttackT attackTeam;
    private DefenceT defenceTeam;

    private boolean startedF = false;
    private boolean endF = false;

    public boolean startedF(){
        return startedF;
    }
    public void startedF(boolean b){
        this.startedF = b;
    }

    public boolean endF(){
        return endF;
    }
    public void endF(boolean b){
        this.endF = b;
    }

    public Game(JavaPlugin plugin){
        this.plugin = plugin;
        this.step = Step.WAITING_PLAYER;
        runTaskTimer(plugin,0,0);
    }

    @Override
    public void run(){
        if (this.step == Step.WAITING_PLAYER){
            this.step = Step.EMP;
            waitingAndStart();
        } else if (this.step == Step.BUY_TIME){
            this.step = Step.EMP;
            this.timer = new Timer(Constants.BUY_SECOND);
        } else if (this.step == Step.NORMAL_PLAY_TIME){
            this.step = Step.EMP;
            this.timer = new Timer(Constants.PLAY_SECOND);
        } else if (this.step == Step.BOMB_PLAY_TIME){
            this.step = Step.EMP;
            this.timer = new Timer(45);
        } else if (this.step == Step.TEAM_SWP){
            teamSwap();
            this.step = Step.BUY_TIME;
        } else if (this.step == Step.END){

        }
    }
    private void waitingAndStart(){
        CompletableFuture.supplyAsync(() -> {
            try {
                int i;
                for (i = 0; i < 60; i++) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle("Waiting join players...", null, 20, 60, 2);
                    }
                    Thread.sleep(1000);
                }
                if (Bukkit.getOnlinePlayers().size() < 2) {
                    Bukkit.getServer().shutdown();
                    return false;
                }
                for (i = 0; i < 15; i++) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(String.valueOf(14 - i), null, 20, 60, 2);
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.SEVERE, e.toString());
                return false;
            }
            step = Step.BUY_TIME;
            timer = new Timer(Constants.BUY_SECOND);
            return true;
        });
    }
    private void buyTime(){
        CompletableFuture.supplyAsync(() -> {
            try {

                while (timer.countDown()) {
                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                this.plugin.getLogger().log(Level.SEVERE, e.toString());
                return false;
            }
            return true;
        });
    }
    private void teamSwap(){
        ArrayList<Player> defencePlayers = this.defenceTeam.list();
        this.attackTeam.allRemove();
        this.attackTeam = new AttackT();
        this.attackTeam.allAdd(defencePlayers);

        ArrayList<Player> attackPlayers = this.attackTeam.list();
        this.defenceTeam.allRemove();
        this.defenceTeam = new DefenceT();
        this.defenceTeam.allAdd(attackPlayers);

        Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle("攻撃/守備 切り替え", null, 20, 60, 2));
    }
}
