package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import si.f5.stsaria.crafterStrikeMain.items.BombI;
import si.f5.stsaria.crafterStrikeMain.items.BuyMenuOpenerI;
import si.f5.stsaria.crafterStrikeMain.items.FillerI;
import si.f5.stsaria.crafterStrikeMain.teams.AttackT;
import si.f5.stsaria.crafterStrikeMain.teams.BTeam;
import si.f5.stsaria.crafterStrikeMain.teams.DefenceT;

import java.util.*;


public class Game extends BukkitRunnable implements Listener {

    private final JavaPlugin plugin;
    private static FileConfiguration config;
    private static Step step;
    private Result result;
    private Timer timer;
    private AttackT attackTeam;
    private DefenceT defenceTeam;

    private BTeam winTeam = null;
    private BTeam loseTeam = null;

    public static GamePlayer bombPlantPlayer = null;
    public static Location bombPlantLocation = null;
    public static String bombPlantCode = null;
    public static GamePlayer bombDefusePlayer = null;
    public static String bombDefuseCode = null;

    public Game(JavaPlugin plugin){
        this.plugin = plugin;
        config = plugin.getConfig();
        step = Step.WAITING_PLAYER;

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        this.plugin.getServer().getPluginManager().registerEvents(new BombI(), this.plugin);
        this.plugin.getServer().getPluginManager().registerEvents(new BuyMenuOpenerI(), this.plugin);
        this.plugin.getServer().getPluginManager().registerEvents(new BuyGui(), this.plugin);

        this.plugin.getServer().setWhitelist(true);

        config.addDefault("waitingJoinPlayersSecond", 30);
        config.addDefault("buySecond", 20);
        config.addDefault("playSecond", 120);
        config.addDefault("bombPlaySecond", 40);
        config.addDefault("inPlayEndSecond", 5);
        config.addDefault("endSecond", 8);
        config.addDefault("moneyPerFirstStart", 850);
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
        config.addDefault("startGameMessage", "ゲームを開始します！");
        config.addDefault("cantBuyMessage", "お金が足りません！");
        config.addDefault("startRoundMessage", "ラウンド開始！！");
        config.addDefault("attackBombPlantMessage", "爆弾を設置した。");
        config.addDefault("attackBombedMessage", "C4が爆発した。");
        config.addDefault("defenceBombPlantMessage", ChatColor.RED+"爆弾が設置された！爆弾を解除しろ！");
        config.addDefault("defenceBombedMessage", "C4が爆発した。");
        config.addDefault("winEndMessage", "作戦は成功だ。");
        config.addDefault("loseEndMessage", "作成は失敗だ。");
        config.addDefault("timeOverMessage", "作戦時間終了！");
        config.addDefault("winAllKillMessage", "敵を殲滅した。");
        config.addDefault("loseAllKillMessage", "敵に殲滅された。");
        config.addDefault("teamSwapMessage", "攻撃/守備 切り替え");
        config.addDefault("bombPlantCodeMessage", "設置コードをチャットに打て\nコード:");
        config.addDefault("bombDefuseCodeMessage", "解除コードをチャットに打て\nコード:");
        config.addDefault("endWinTitle", ChatColor.AQUA+"勝利");
        config.addDefault("endLoseTitle", ChatColor.RED+"敗北");
        config.addDefault("bossBarTextWaitingJoinPlayers", "ゲーム開始まで（プレイヤー参加の待機中）");
        config.addDefault("bossBarTextBuy", "購入タイム");
        config.addDefault("bossBarTextPlay", "プレイ中");
        config.addDefault("bossBarTextInPlayEnd", "ラウンド終了");
        config.addDefault("bossBarTextEnd", "試合終了");
        config.addDefault("wordPrice", "値段");
        config.addDefault("wordPocket", "所持金");
        config.addDefault("wordShop", "ショップ");
        config.addDefault("worldName", "worldName");

        config.options().copyDefaults(true);

        this.plugin.saveConfig();

        Objects.requireNonNull(Bukkit.getScoreboardManager())
        .getMainScoreboard().getTeams().forEach(Team::unregister);

        attackTeam = new AttackT();
        defenceTeam = new DefenceT();

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
    public static Step getStep(){
        return step;
    }

    @Override
    public void run(){
        if (step == Step.WAITING_PLAYER){
            waitingJoinPlayers();
            return;
        } else if (step.equals(Step.START)){
            this.start();
            return;
        } else if (step.equals(Step.BUY_TIME)){
            this.buy();
        } else if (step.equals(Step.NORMAL_PLAY_TIME)){
            this.play();
        } else if (step.equals(Step.BOMB_PLAY_TIME)){
            this.bombPlay();
        } else if (step.equals(Step.TEAM_SWP)){
            this.teamSwap();
            return;
        } else if (step.equals(Step.IN_PLAY_END)){
            this.inPlayEnd();
        } else if (step.equals(Step.END)){
            this.end();
        }
        this.updateScore();

    }
    private void playEndSet(BTeam winTeam, BTeam loseTeam, Result result){
        this.winTeam = winTeam;
        this.loseTeam = loseTeam;
        this.result = result;
        step = Step.IN_PLAY_END;
        try {this.timer.removeAll();} catch (Exception ignore) {}
        this.timer = null;
    }
    private void updateScore(){
        this.attackTeam.actionbar(this.attackTeam.score()+"-"+this.defenceTeam.score());
        this.defenceTeam.actionbar(this.defenceTeam.score()+"-"+this.attackTeam.score());
    }
    private void waitingJoinPlayers(){
        if (this.timer == null){
            this.timer = new Timer(config.getInt("waitingJoinPlayersSecond"), config.getString("bossBarTextWaitingJoinPlayers"));
        }
        if (!this.timer.countDown()){
            Players.sound(Sound.BLOCK_ANVIL_PLACE, 100F, 2F);
            step = Step.START;
            this.timer.removeAll();
            this.timer = null;
        } else if (this.timer.getRestTick() % 20 == 0 && this.timer.getRestTick() < this.timer.getTick()/2) {
            Players.sound(Sound.BLOCK_STONE_BUTTON_CLICK_ON, 100F, 1F);
        }
    }
    private void start(){
        List<Player> shuffledPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(shuffledPlayers);
        for (Player p : shuffledPlayers){
            for (int i = 2; i < 40; i++){
                if (i == 8) continue;
                FillerI fillerI = new FillerI();
                p.getInventory().setItem(i, fillerI.getItemStack());
            }
            GamePlayers.add(p);
            Objects.requireNonNull(GamePlayers.get(p)).addMoney(config.getInt("moneyPerFirstStart"));
            if (shuffledPlayers.indexOf(p) % 2 == 0) attackTeam.add(p);
            else defenceTeam.add(p);
        }
        Players.message(config.getString("startGameMessage"));
        plugin.getServer().setWhitelist(false);
        step = Step.BUY_TIME;
    }
    private void buy(){
        if (this.timer == null){
            this.timer = new Timer(config.getInt("buySecond"), config.getString("bossBarTextBuy"));
            Location attackSpawnL = new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("worldName"))),
                config.getIntegerList("attackSpawnLocation").getFirst(),
                config.getIntegerList("attackSpawnLocation").get(1),
                config.getIntegerList("attackSpawnLocation").getLast()
            );
            Location defenceSpawnL = new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("worldName"))),
                config.getIntegerList("defenceSpawnLocation").getFirst(),
                config.getIntegerList("defenceSpawnLocation").get(1),
                config.getIntegerList("defenceSpawnLocation").getLast()
            );
            for (GamePlayer gP : GamePlayers.getAll()) {
                Player p = gP.getPlayer();
                p.setGameMode(GameMode.SURVIVAL);
                if (attackTeam.contains(p)) p.teleport(attackSpawnL);
                else if (defenceTeam.contains(p)) p.teleport(defenceSpawnL);
                BuyMenuOpenerI openerI = new BuyMenuOpenerI();
                ItemStack openerIS = openerI.getItemStack();
                ItemMeta openerIM = openerIS.getItemMeta();
                Objects.requireNonNull(openerIM).setDisplayName(
                    openerIM.getDisplayName()+" - "+config.getString("wordPocket")+":"+gP.getMoney()
                );
                openerIS.setItemMeta(openerIM);
                gP.getPlayer().getInventory().setItem(0, openerIS);
            }

        }
        if (!this.timer.countDown()) {
            Players.message(config.getString("startRoundMessage"));
            Players.sound(Sound.ITEM_GOAT_HORN_SOUND_0, 100F, 1F);
            step = Step.NORMAL_PLAY_TIME;
            this.timer.removeAll();
            this.timer = null;
        } else if (this.timer.getRestTick() % 20 == 0 && this.timer.getRestTick() < this.timer.getTick()/2) {
            if (this.timer.getRestTick() == this.timer.getTick()/2-1){
                Players.stopAllSounds();
            }
            Players.sound(Sound.BLOCK_STONE_BUTTON_CLICK_ON, 100F, 1F);
        }
    }
    private void play(){
        if (this.timer == null){
            Players.message(config.getString("startRoundMessage"));
            this.timer = new Timer(config.getInt("playSecond"), config.getString("bossBarTextPlay"));
            GamePlayers.setItem(0, new ItemStack(Material.BOW));
            GamePlayers.setItem(1, new ItemStack(Material.CROSSBOW));
        }
        if (this.attackTeam.notSpectatorList().isEmpty()) this.playEndSet(defenceTeam, attackTeam, Result.ALL_KILL);
        else if (this.defenceTeam.notSpectatorList().isEmpty()) this.playEndSet(attackTeam, defenceTeam, Result.ALL_KILL);
        else if (!this.timer.countDown()) this.playEndSet(defenceTeam, attackTeam, Result.TIME_OVER);
    }
    private void bombPlay(){
        if (this.timer == null){
            this.timer = new Timer(config.getInt("bombPlaySecond"), config.getString("bossBarTextPlay"));
            this.attackTeam.list().forEach(p -> {
                if (GamePlayers.get(p) != null){
                    Objects.requireNonNull(GamePlayers.get(p)).addMoney(config.getInt("moneyPerPlant"));
                }
            });
        }
        if (this.defenceTeam.notSpectatorList().isEmpty()) this.playEndSet(attackTeam, defenceTeam, Result.ALL_KILL);
        else if (!this.timer.countDown()) {
            this.playEndSet(attackTeam, defenceTeam, Result.BOMBED);
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p.getGameMode() != GameMode.SURVIVAL) return;
                World world = Objects.requireNonNull(Bukkit.getWorld(Objects.requireNonNull(config.getString("worldName"))));
                world.createExplosion(bombPlantLocation, 100F);
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
                    p.damage(p.getHealth());
                }
            });
        }
    }
    private void inPlayEnd(){
        if (this.timer == null){
            this.timer = new Timer(config.getInt("inPlayEndSecond"), config.getString("bossBarTextInPlayEnd"));

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
            winMessage += "\n"+config.getString("winEndMessage");
            loseMessage += "\n"+config.getString("loseEndMessage");

            this.winTeam.message(winMessage);
            this.loseTeam.message(loseMessage);

            winTeam.upScore();
        }
        if (!this.timer.countDown()) {
            step = Step.BUY_TIME;

            bombPlantPlayer = null;
            bombPlantLocation = null;
            bombPlantCode = null;
            bombDefusePlayer = null;
            bombDefuseCode = null;

            if (winTeam.score() >= config.getInt("winRounds")){
                step = Step.END;
                return;
            }
            this.winTeam = null;
            this.loseTeam = null;

            this.timer.removeAll();
            this.timer = null;
        }
    }
    private void end(){
        if (this.timer == null){
            this.timer = new Timer(config.getInt("endSecond"), config.getString("bossBarTextEnd"));

            this.winTeam.title(config.getString("endWinTitle"));
            this.loseTeam.title(config.getString("endLoseTitle"));

        }
        if (!this.timer.countDown()) {
            step = Step.EMP;
            plugin.getServer().shutdown();
        }
    }
    private void teamSwap(){
        Players.message(config.getString("teamSwapMessage"));

        ArrayList<Player> defencePlayers = this.defenceTeam.list();
        this.attackTeam.allRemove();
        this.attackTeam = new AttackT();
        this.attackTeam.allAdd(defencePlayers);

        ArrayList<Player> attackPlayers = this.attackTeam.list();
        this.defenceTeam.allRemove();
        this.defenceTeam = new DefenceT();
        this.defenceTeam.allAdd(attackPlayers);

        step = Step.BUY_TIME;
        this.timer = null;
    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        GamePlayer aGP;
        GamePlayer vGP;
        try {
            aGP = GamePlayers.get((Player) e.getDamager());
            vGP = GamePlayers.get((Player) e.getEntity());
            if (Objects.requireNonNull(vGP).getPlayer().getHealth() < 1){
                vGP.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
        } catch (Exception ignore) {return;}
        if (aGP == null) return;
        if (vGP.getPlayer().getHealth() < 1){
            e.setCancelled(true);
            aGP.addKill();
            vGP.addDeath();
        }
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(
            (attackTeam.contains(aGP.getPlayer()) ? attackTeam.COLOR() : defenceTeam.COLOR()) +
            aGP.getPlayer().getDisplayName() +
            " kill -> " +
            (attackTeam.contains(vGP.getPlayer()) ? attackTeam.COLOR() : defenceTeam.COLOR()) +
            vGP.getPlayer().getDisplayName()
        ));
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if (step.equals(Step.NORMAL_PLAY_TIME) && e.getMessage().equals(bombPlantCode) && e.getPlayer().equals(bombPlantPlayer.getPlayer())){
            Objects.requireNonNull(Bukkit.getWorld("world"))
            .getBlockAt(bombPlantLocation).setType(new BombI().MATERIAL());
            step = Step.BOMB_PLAY_TIME;
            this.timer = null;
        }
        if (step.equals(Step.BOMB_PLAY_TIME) && e.getMessage().equals(bombDefuseCode) && e.getPlayer().equals(bombDefusePlayer.getPlayer()) && this.defenceTeam.contains(bombDefusePlayer.getPlayer())){
            this.winTeam = this.defenceTeam;
            step = Step.IN_PLAY_END;
            this.timer = null;
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.getPlayer().getInventory().clear();
        if (!step.equals(Step.WAITING_PLAYER)) e.getPlayer().setGameMode(GameMode.SPECTATOR);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (!step.equals(Step.WAITING_PLAYER) && GamePlayers.get(e.getPlayer()) != null) {
            Player p = e.getPlayer();
            if (attackTeam.contains(e.getPlayer())) attackTeam.remove(p);
            if (defenceTeam.contains(e.getPlayer())) defenceTeam.remove(p);
            GamePlayers.remove(p);
        }
    }
}
