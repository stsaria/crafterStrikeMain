package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import si.f5.stsaria.crafterStrikeMain.items.BombI;
import si.f5.stsaria.crafterStrikeMain.items.BuyMenuOpenerI;
import si.f5.stsaria.crafterStrikeMain.items.FillerI;
import si.f5.stsaria.crafterStrikeMain.teams.AttackT;
import si.f5.stsaria.crafterStrikeMain.teams.BTeam;
import si.f5.stsaria.crafterStrikeMain.teams.DefenceT;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;



public class Game extends BukkitRunnable implements Listener {

    private final JavaPlugin plugin;
    private static FileConfiguration config;
    private Step step;
    private Result result;
    private Timer timer;
    private AttackT attackTeam;
    private DefenceT defenceTeam;

    private boolean initialF = false;
    private boolean endF = false;

    private BTeam winTeam = null;
    private BTeam loseTeam = null;

    public static GamePlayer bombPlantPlayer = null;
    public static Location bombPlantLocation = null;
    public static String bombPlantCode = null;
    public static GamePlayer bombDefusePlayer = null;
    public static String bombDefuseCode = null;

    public boolean endF(){
        return endF;
    }
    public void endF(boolean b){
        this.endF = b;
    }

    public Game(JavaPlugin plugin){
        this.plugin = plugin;
        config = plugin.getConfig();
        this.step = Step.WAITING_PLAYER;

        this.plugin.getServer().getPluginManager().registerEvents(new BuyMenuOpenerI(), this.plugin);
        this.plugin.getServer().getPluginManager().registerEvents(new BuyGui(), this.plugin);

        this.plugin.getServer().setWhitelist(true);

        config.addDefault("mapName", "Hoge");
        config.addDefault("buySecond", 20);
        config.addDefault("playSecond", 120);
        config.addDefault("bombPlaySecond", 40);
        config.addDefault("inPlayEndSecond", 5);
        config.addDefault("moneyPerWin", 3000);
        config.addDefault("moneyPerLose", 2700);
        config.addDefault("moneyPerOneKill", 290);
        config.addDefault("moneyPerBombPlant", 500);
        config.addDefault("attackSpawnLocation", new int[]{0, 0, 0});
        config.addDefault("defenceSpawnLocation", new int[]{0, 0, 0});
        config.addDefault("spawnMovableBlockAreaXZ", new int[]{5, 5});
        config.addDefault("bombPlantLocation", new int[]{0, 0, 0});
        config.addDefault("bombPlantableBlockAreaXZ", new int[]{6, 6});
        config.addDefault("bombDeathBlockAreaXZ", new int[]{10, 10});
        config.addDefault("attackBombPlantMessage", "爆弾を設置した。");
        config.addDefault("attackBombedMessage", "C4が爆発した。");
        config.addDefault("defenceBombPlantMessage", ChatColor.RED+"爆弾が設置された！爆弾を解除しろ！");
        config.addDefault("defenceBombedMessage", "C4が爆発した。");
        config.addDefault("winEndMessage", "作戦は成功だ。");
        config.addDefault("loseEndMessage", "作成は失敗だ。");
        config.addDefault("timeOverMessage", "作戦時間終了！");
        config.addDefault("winAllKillMessage", "敵を殲滅した。");
        config.addDefault("loseAllKillMessage", "敵に殲滅された。");
        config.addDefault("teamSwapTitle", "攻撃/守備 切り替え");
        config.addDefault("bombPlantCodeMessage", "設置コードをチャットに打て\nコード:");
        config.addDefault("bombDefuseCodeMessage", "解除コードをチャットに打て\nコード:");
        config.options().copyDefaults(true);

        this.plugin.saveConfig();

        runTaskTimer(plugin,0,0);
    }

    public static String configGetString(String path){
        return config.getString(path);
    }
    public static int configGetInt(String path){
        return config.getInt(path);
    }
    public static List<Integer> configGetIntList(String path){
        return config.getIntegerList(path);
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
                    p.getInventory().setItem(i, fillerI.getItemStack());
                }
            }
            step = Step.BUY_TIME;
            plugin.getServer().setWhitelist(false);
            Bukkit.getOnlinePlayers().forEach(GamePlayers::add);
            this.initialF = true;
            return true;
        });
    }
    private void buy(){
        if (this.initialF){
            this.timer = new Timer(config.getInt("buySecond"));
            for (GamePlayer gP : GamePlayers.getAll()) {
                if (gP == null) continue;
                BuyMenuOpenerI openerI = new BuyMenuOpenerI();
                openerI.setMoney(gP);
                gP.getPlayer().getInventory().setItem(0, openerI.getItemStack());
            }
            this.initialF = false;
        }
        if (!this.timer.countDown()) {this.step = Step.NORMAL_PLAY_TIME; this.initialF = true;}
    }
    private void play(){
        if (this.initialF){
            this.timer = new Timer(config.getInt("playSecond"));
        }
        if (!this.timer.countDown()) {this.step = Step.IN_PLAY_END; this.initialF = true;}
    }
    private void bombPlay(){
        if (this.initialF){
            this.timer = new Timer(config.getInt("bombPlaySecond"));
        }
        if (!this.timer.countDown()) {
            this.step = Step.IN_PLAY_END;
            this.initialF = true;

            this.winTeam = this.attackTeam;

            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p.getGameMode() != GameMode.SURVIVAL) return;
                Location pL = p.getLocation();
                List<Integer> bombDeathAreaDistance = config.getIntegerList("bombDeathBlockAreaXZ");
                if (Calculator.isXZIncludeRange(
                        pL.getBlockX(),
                        pL.getBlockZ(),
                        bombPlantLocation.getBlockX(),
                        bombPlantLocation.getBlockZ(),
                        bombDeathAreaDistance.getFirst(),
                        bombDeathAreaDistance.getLast()
                    )
                ){
                    p.setHealth(0);
                }
            });
        }
    }
    private void inPlayEnd(){
        if (this.initialF){
            String winMessage = "";
            String loseMessage = "";

            if (result.equals(Result.BOMBED)){
                winMessage += config.getString("attackBombedMessage");
                loseMessage += config.getString("defenceBombedMessage");
            } else if (result.equals(Result.ALL_KILL)){
                winMessage += config.getString("winAllKillMessage");
                loseMessage += config.getString("loseAllKillMessage");
            } else if (result.equals(Result.TIME_OVER)){
                winMessage += config.getString("timeOverMessage");
                loseMessage += config.getString("timeOverMessage");
            }
            winMessage += config.getString("winEndMessage");
            loseMessage += config.getString("loseEndMessage");

            this.winTeam.message(winMessage);
            this.loseTeam.message(loseMessage);

            this.winTeam = null;
            this.loseTeam = null;

            bombPlantPlayer = null;
            bombPlantLocation = null;
            bombPlantCode = null;
            bombDefusePlayer = null;
            bombDefuseCode = null;
        }

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

        Players.title(config.getString("teamSwapTitle"));

        this.step = Step.BUY_TIME;
        this.initialF = true;
    }
    @EventHandler
    public void onKill(PlayerDeathEvent e){
        if (!(this.step.equals(Step.NORMAL_PLAY_TIME) || this.step.equals(Step.BOMB_PLAY_TIME))) return;
        GamePlayer dGP = GamePlayers.get(e.getEntity());
        GamePlayer kGP = GamePlayers.get(e.getEntity().getKiller());
        if (dGP == null || kGP == null) return;
        dGP.addDeath();
        kGP.addKill();
        kGP.addMoney(config.getInt("moneyPerOneKill"));
        kGP.getPlayer().setGameMode(GameMode.SPECTATOR);
        e.setDeathMessage(
            (attackTeam.contains(kGP.getPlayer()) ? attackTeam.COLOR() : defenceTeam.COLOR()) +
            kGP.getPlayer().getDisplayName() +
            " kill -> " +
            (attackTeam.contains(dGP.getPlayer()) ? attackTeam.COLOR() : defenceTeam.COLOR()) +
            kGP.getPlayer().getDisplayName()
        );

        if (this.winTeam != null) return;
        if (this.attackTeam.notSpectatorList().isEmpty()) this.winTeam = defenceTeam;
        else if (this.defenceTeam.notSpectatorList().isEmpty()) this.winTeam = attackTeam;
        this.step = Step.IN_PLAY_END;
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if (this.step.equals(Step.NORMAL_PLAY_TIME) && e.getMessage().equals(bombPlantCode) && e.getPlayer().equals(bombPlantPlayer.getPlayer())){
            Objects.requireNonNull(Bukkit.getWorld("world"))
            .getBlockAt(bombPlantLocation).setType(new BombI().MATERIAL());
            this.step = Step.BOMB_PLAY_TIME;
            this.initialF = true;
        }
        if (this.step.equals(Step.BOMB_PLAY_TIME) && e.getMessage().equals(bombDefuseCode) && e.getPlayer().equals(bombDefusePlayer.getPlayer())){
            this.step = Step.BOMB_PLAY_TIME;
            this.initialF = true;
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (!this.step.equals(Step.WAITING_PLAYER)) e.getPlayer().setGameMode(GameMode.SPECTATOR);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (!this.step.equals(Step.WAITING_PLAYER) && GamePlayers.get(e.getPlayer()) != null) {
            if (attackTeam.contains(e.getPlayer())) attackTeam.remove(e.getPlayer());
            if (defenceTeam.contains(e.getPlayer())) defenceTeam.remove(e.getPlayer());
        }
    }
}
