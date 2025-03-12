package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
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
        config.addDefault("winRounds", 13);
        config.addDefault("attackSpawnLocation", new int[]{0, 0, 0});
        config.addDefault("defenceSpawnLocation", new int[]{0, 0, 0});
        config.addDefault("spawnMovableBlockAreaXZ", new int[]{5, 5});
        config.addDefault("bombPlantLocation", new int[]{0, 0, 0});
        config.addDefault("bombPlantableBlock", 6);
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
        } else if (this.step.equals(Step.BUY_TIME)){
            this.buy();
        } else if (this.step.equals(Step.NORMAL_PLAY_TIME)){
            this.play();
        } else if (this.step.equals(Step.BOMB_PLAY_TIME)){
            this.bombPlay();
        } else if (this.step.equals(Step.TEAM_SWP)){
            this.teamSwap();
        } else if (this.step.equals(Step.IN_PLAY_END)){
            this.inPlayEnd();
        } else if (this.step.equals(Step.END)){

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
            GamePlayers.setItem(0, new ItemStack(Material.BOW));
            GamePlayers.setItem(1, new ItemStack(Material.CROSSBOW));
        }
        if (!this.timer.countDown()) {
            this.step = Step.IN_PLAY_END;
            this.initialF = true;
            this.result = Result.TIME_OVER;

            this.winTeam = this.defenceTeam;
        }
    }
    private void bombPlay(){
        if (this.initialF){
            this.timer = new Timer(config.getInt("bombPlaySecond"));
            this.attackTeam.list().forEach(p -> {
                if (GamePlayers.get(p) != null){
                    Objects.requireNonNull(GamePlayers.get(p)).addMoney(config.getInt("moneyPerPlant"));
                }
            });
        }
        if (!this.timer.countDown()) {
            this.step = Step.IN_PLAY_END;
            this.initialF = true;

            this.winTeam = this.attackTeam;
            this.result = Result.BOMBED;

            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p.getGameMode() != GameMode.SURVIVAL) return;
                Location pL = p.getLocation();
                if (Calculator.isXYZIncludeRange(
                    pL.getBlockX(),
                    pL.getBlockY(),
                    pL.getBlockZ(),
                    bombPlantLocation.getBlockX(),
                    bombPlantLocation.getBlockY(),
                    bombPlantLocation.getBlockZ(),
                    config.getInt("bombDeathBlock")
                )){
                    p.setHealth(0);
                }
            });
        }
    }
    private void inPlayEnd(){
        if (this.initialF){
            this.timer = new Timer(config.getInt("inPlayEndSecond"));

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
        }
        if (!this.timer.countDown()) {
            this.step = Step.BUY_TIME;

            bombPlantPlayer = null;
            bombPlantLocation = null;
            bombPlantCode = null;
            bombDefusePlayer = null;
            bombDefuseCode = null;

            this.winTeam = null;
            this.loseTeam = null;
            this.initialF = true;
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
    public void onDamage(EntityDamageByEntityEvent e) {
        GamePlayer aGP;
        GamePlayer vGP;
        try {
            aGP = GamePlayers.get((Player) e.getDamager());
            vGP = GamePlayers.get((Player) e.getEntity());
        } catch (Exception ignore) {return;}
        if (aGP == null || vGP == null) return;
        if (vGP.getPlayer().getHealth() < 1){
            e.setCancelled(true);
            aGP.addKill();
            vGP.addDeath();
            vGP.getPlayer().setGameMode(GameMode.SPECTATOR);
        }

        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(
            (attackTeam.contains(aGP.getPlayer()) ? attackTeam.COLOR() : defenceTeam.COLOR()) +
            aGP.getPlayer().getDisplayName() +
            " kill -> " +
            (attackTeam.contains(vGP.getPlayer()) ? attackTeam.COLOR() : defenceTeam.COLOR()) +
            vGP.getPlayer().getDisplayName()
        ));

        if (this.winTeam != null) return;
        if (this.attackTeam.notSpectatorList().isEmpty()) this.winTeam = defenceTeam;
        else if (this.defenceTeam.notSpectatorList().isEmpty()) this.winTeam = attackTeam;
        if (this.winTeam != null) this.step = Step.IN_PLAY_END;
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if (this.step.equals(Step.NORMAL_PLAY_TIME) && e.getMessage().equals(bombPlantCode) && e.getPlayer().equals(bombPlantPlayer.getPlayer())){
            Objects.requireNonNull(Bukkit.getWorld("world"))
            .getBlockAt(bombPlantLocation).setType(new BombI().MATERIAL());
            this.step = Step.BOMB_PLAY_TIME;
            this.initialF = true;
        }
        if (this.step.equals(Step.BOMB_PLAY_TIME) && e.getMessage().equals(bombDefuseCode) && e.getPlayer().equals(bombDefusePlayer.getPlayer()) && this.defenceTeam.contains(bombDefusePlayer.getPlayer())){
            this.winTeam = this.defenceTeam;
            this.step = Step.IN_PLAY_END;
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
