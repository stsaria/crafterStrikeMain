package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import si.f5.stsaria.crafterStrikeMain.items.BuyMenuOpenerI;
import si.f5.stsaria.crafterStrikeMain.items.FillerI;
import si.f5.stsaria.crafterStrikeMain.teams.AttackT;
import si.f5.stsaria.crafterStrikeMain.teams.BTeam;
import si.f5.stsaria.crafterStrikeMain.teams.DefenceT;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;


public class Game extends BukkitRunnable implements Listener {

    private final JavaPlugin plugin;
    private Step step;
    private Timer timer;
    private AttackT attackTeam;
    private DefenceT defenceTeam;

    private boolean initialF = false;
    private boolean endF = false;

    private BTeam winTeam;

    public static GamePlayer bombPlantPlayerPlan;
    public static String bombPlantCode = null;
    public static String bombDefuseCode = null;

    public boolean endF(){
        return endF;
    }
    public void endF(boolean b){
        this.endF = b;
    }

    public Game(JavaPlugin plugin){
        this.plugin = plugin;
        this.step = Step.WAITING_PLAYER;

        plugin.getServer().getPluginManager().registerEvents(new BuyMenuOpenerI(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new BuyGui(), plugin);

        runTaskTimer(plugin,0,0);
    }

    @Override
    public void run(){
        if (this.step == Step.WAITING_PLAYER){
            this.step = Step.EMP;
            waitingAndStart();
        } else if (this.step == Step.BUY_TIME){
            this.buy();
        } else if (this.step == Step.NORMAL_PLAY_TIME){
            this.play();
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
                    Players.title("Waiting join players...");
                    Thread.sleep(1000);
                }
                if (Bukkit.getOnlinePlayers().size() < 2) {
                    Bukkit.getServer().shutdown();
                    return false;
                }
                for (i = 0; i < 15; i++) {
                    Players.title(String.valueOf(14 - i));
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.SEVERE, e.toString());
                return false;
            }
            for (Player p : Bukkit.getOnlinePlayers()){
                for (int i = 2; i < 36; i++){
                    if (i == 8) continue;
                    FillerI fillerI = new FillerI();
                    fillerI.bindingEnchant();
                    p.getInventory().setItem(i, fillerI.getItemStack());
                }
            }
            step = Step.BUY_TIME;
            timer = new Timer(Constants.BUY_SECOND);
            this.initialF = true;
            return true;
        });
    }
    private void buy(){
        if (this.initialF){
            for (Player p : Bukkit.getOnlinePlayers()) {
                GamePlayer gP = GamePlayers.get(p);
                if (gP == null) continue;
                BuyMenuOpenerI openerI = new BuyMenuOpenerI();
                openerI.setMoney(gP);
                openerI.bindingEnchant();
                p.getInventory().setItem(0, openerI.getItemStack());
            }
            this.initialF = false;
        }
        if (!timer.countDown()) this.step = Step.NORMAL_PLAY_TIME; this.initialF = true;
    }
    private void play(){
        if (!timer.countDown()) this.step = Step.IN_PLAY_END;
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

        Players.title("攻撃/守備 切り替え");
    }
    @EventHandler
    public void onKill(PlayerDeathEvent e){
        GamePlayer dGP = GamePlayers.get(e.getEntity());
        GamePlayer kGP = GamePlayers.get(e.getEntity().getKiller());
        if (dGP == null || kGP == null) return;
        dGP.addDeath();
        kGP.addKill();
        kGP.getPlayer().setGameMode(GameMode.SPECTATOR);
        e.setDeathMessage(
            (attackTeam.contains(kGP.getPlayer()) ? attackTeam.COLOR() : defenceTeam.COLOR()) +
            kGP.getPlayer().getDisplayName() +
            " kill -> " +
            (attackTeam.contains(dGP.getPlayer()) ? attackTeam.COLOR() : defenceTeam.COLOR()) +
            kGP.getPlayer().getDisplayName()
        );
        if (this.attackTeam.notSpectatorList().isEmpty()) this.winTeam = defenceTeam; this.step = Step.END;
        if (this.defenceTeam.notSpectatorList().isEmpty()) this.winTeam = attackTeam; this.step = Step.END;
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if (e.getMessage().equals(bombPlantCode)){

        }
    }
}
