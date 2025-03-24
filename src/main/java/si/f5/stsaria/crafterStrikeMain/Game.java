package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import si.f5.stsaria.crafterStrikeMain.items.BombI;
import si.f5.stsaria.crafterStrikeMain.items.BuyMenuOpenerI;
import si.f5.stsaria.crafterStrikeMain.items.FillerI;
import si.f5.stsaria.crafterStrikeMain.items.RadioChatOpenerI;
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
    private static AttackT attackTeam;
    private static DefenceT defenceTeam;

    private BTeam winTeam = null;
    private BTeam loseTeam = null;

    public static BombArea bombPlantArea = null;
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
        this.plugin.getServer().getPluginManager().registerEvents(new BuyMenuOpenerI(""), this.plugin);
        this.plugin.getServer().getPluginManager().registerEvents(new BuyGui(), this.plugin);
        this.plugin.getServer().getPluginManager().registerEvents(new RadioChatOpenerI(), this.plugin);
        this.plugin.getServer().getPluginManager().registerEvents(new RadioChatGui(), this.plugin);

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
        config.addDefault("attackSpawnLocations", new ArrayList<>(List.of(0, 0, 0, 0, 0, 0)));
        config.addDefault("defenceSpawnLocations", new ArrayList<>(List.of(0, 0, 0, 0, 0, 0)));
        config.addDefault("aBombPlantLocations", new ArrayList<>(List.of(0, 0, 0, 0, 0, 0)));
        config.addDefault("bBombPlantLocations", new ArrayList<>(List.of(0, 0, 0, 0, 0, 0)));
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
        config.addDefault("wordRadioChat", "ラジオチャット");
        config.addDefault("wordAttackTeam", "攻撃チーム");
        config.addDefault("wordDefenceTeam", "守備チーム");
        config.addDefault("wordBomb", "爆弾");
        config.addDefault("worldName", "world");
        config.addDefault("radioChatMessages", new ArrayList<>(List.of(
            "こんにちは！", "Aに敵を発見！", "中央に敵を発見！", "Bに敵を発見！", "Aに行こう", "Bに行こう", "はい", "いいえ"
        )));

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
    public static List<String> configGetStringList(String path){
        return config.getStringList(path);
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
    public static List<Player> getAttackTeamPlayers(){
        return new ArrayList<>(attackTeam.list());
    }
    public static List<Player> getDefenceTeamPlayers(){
        return new ArrayList<>(defenceTeam.list());
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
        attackTeam.actionbar(attackTeam.COLOR()+config.getString("wordAttackTeam")+" "+attackTeam.score()+ChatColor.WHITE+" - "+defenceTeam.COLOR()+config.getString("wordDefenceTeam")+" "+defenceTeam.score());
        defenceTeam.actionbar(defenceTeam.COLOR()+config.getString("wordDefenceTeam")+" "+defenceTeam.score()+ChatColor.WHITE+" - "+attackTeam.COLOR()+config.getString("wordAttackTeam")+" "+attackTeam.score());
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
            for (int i = 5; i < 40; i++){
                if (i == 8) continue;
                FillerI fillerI = new FillerI();
                p.getInventory().setItem(i, fillerI.getItemStack());
            }
            GamePlayer gP = GamePlayers.add(p);
            Objects.requireNonNull(GamePlayers.get(p)).addMoney(config.getInt("moneyPerFirstStart"));
            if (shuffledPlayers.indexOf(p) % 2 == 0) attackTeam.add(gP);
            else defenceTeam.add(gP);
            p.getInventory().setItem(3, new RadioChatOpenerI().getItemStack());
            p.getInventory().setItem(4, new ItemStack(Material.NETHERITE_AXE));
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
                config.getIntegerList("attackSpawnLocations").getFirst(),
                config.getIntegerList("attackSpawnLocations").get(1),
                config.getIntegerList("attackSpawnLocations").get(2)
            );
            Location defenceSpawnL = new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("worldName"))),
                config.getIntegerList("defenceSpawnLocations").getFirst(),
                config.getIntegerList("defenceSpawnLocations").get(1),
                config.getIntegerList("defenceSpawnLocations").get(2)
            );
            for (GamePlayer gP : GamePlayers.getAll()) {
                gP.setGameMode(GameMode.SURVIVAL);
                gP.setItem(1, new FillerI().getItemStack());
                gP.setHealth(gP.getMaxHealth());
                gP.setFoodLevel(20);
                gP.setSaturation(20);
                if (attackTeam.contains(gP)){
                    gP.teleport(attackSpawnL);
                    if (GamePlayers.getAll().indexOf(gP) == 0){
                        gP.setItem(2, new BombI().getItemStack());
                    }
                }
                else if (defenceTeam.contains(gP)) gP.teleport(defenceSpawnL);
                BuyMenuOpenerI openerI = new BuyMenuOpenerI(String.valueOf(gP.getMoney()));
                gP.getPlayer().getInventory().setItem(0, openerI.getItemStack());
            }
            Players.sound(Sound.MUSIC_DISC_BLOCKS, 100F, 1F);
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
            Players.sound(Sound.BLOCK_NOTE_BLOCK_BIT, 100F, 1F);
        }
    }
    private void play(){
        if (this.timer == null){
            Players.message(config.getString("startRoundMessage"));
            this.timer = new Timer(config.getInt("playSecond"), config.getString("bossBarTextPlay"));
            GamePlayers.setItem(0, new ItemStack(Material.BOW));
            GamePlayers.setItem(1, new ItemStack(Material.CROSSBOW));
        }
        if (attackTeam.notSpectatorList().isEmpty()) this.playEndSet(defenceTeam, attackTeam, Result.ALL_KILL);
        else if (defenceTeam.notSpectatorList().isEmpty()) this.playEndSet(attackTeam, defenceTeam, Result.ALL_KILL);
        else if (!this.timer.countDown()) this.playEndSet(defenceTeam, attackTeam, Result.TIME_OVER);
    }
    private void bombPlay(){
        if (this.timer == null){
            this.timer = new Timer(config.getInt("bombPlaySecond"), config.getString("bossBarTextPlay"));
            attackTeam.list().forEach(p -> {
                if (GamePlayers.get(p) != null){
                    Objects.requireNonNull(GamePlayers.get(p)).addMoney(config.getInt("moneyPerPlant"));
                }
            });
        }
        if (defenceTeam.notSpectatorList().isEmpty()) this.playEndSet(attackTeam, defenceTeam, Result.ALL_KILL);
        else if (!this.timer.countDown()) {
            this.playEndSet(attackTeam, defenceTeam, Result.BOMBED);
            GamePlayers.getAll().forEach(gP -> {
                if (gP.getGameMode() != GameMode.SURVIVAL) return;
                World world = Objects.requireNonNull(Bukkit.getWorld(Objects.requireNonNull(config.getString("worldName"))));
                world.createExplosion(bombPlantLocation, 100F);
                Location pL = gP.getLocation();
                String path = (bombPlantArea.equals(BombArea.A) ? "a" : "b")+"BombPlantLocations";
                if (Calculator.isIncludeRange(
                    config.getIntegerList(path).getFirst(),
                    config.getIntegerList(path).get(1),
                    config.getIntegerList(path).get(2),
                    config.getIntegerList(path).get(3),
                    config.getIntegerList(path).get(4),
                    config.getIntegerList(path).getLast(),
                    pL.getBlockX(),
                    pL.getBlockY(),
                    pL.getBlockZ()
                )){
                    gP.damage(gP.getMaxHealth());
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

        ArrayList<Player> defencePlayers = defenceTeam.list();
        int defenceScore = defenceTeam.score();
        attackTeam.allRemove();
        attackTeam = new AttackT();
        attackTeam.addAll(defencePlayers);
        for (int i = 0; i < defenceScore; i++){attackTeam.upScore();}

        ArrayList<Player> attackPlayers = attackTeam.list();
        int attackScore = attackTeam.score();
        defenceTeam.allRemove();
        defenceTeam = new DefenceT();
        defenceTeam.addAll(attackPlayers);
        for (int i = 0; i < attackScore; i++){defenceTeam.upScore();}

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
            (attackTeam.contains(aGP) ? attackTeam.COLOR() : defenceTeam.COLOR()) +
            aGP.getPlayer().getDisplayName() +
            " kill -> " +
            (attackTeam.contains(vGP) ? attackTeam.COLOR() : defenceTeam.COLOR()) +
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
        if (step.equals(Step.BOMB_PLAY_TIME) && e.getMessage().equals(bombDefuseCode) && e.getPlayer().equals(bombDefusePlayer.getPlayer()) && defenceTeam.contains(bombDefusePlayer.getPlayer())){
            this.winTeam = defenceTeam;
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
            else if (defenceTeam.contains(e.getPlayer())) defenceTeam.remove(p);
            GamePlayers.remove(p);
        }
    }
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getEnchantments().containsKey(Enchantment.BINDING_CURSE)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Location to = Objects.requireNonNull(e.getTo());
        if (step.equals(Step.BUY_TIME) && !(Calculator.isIncludeRange(
            config.getIntegerList("attackSpawnLocations").getFirst(),
            config.getIntegerList("attackSpawnLocations").get(1),
            config.getIntegerList("attackSpawnLocations").get(2),
            config.getIntegerList("attackSpawnLocations").get(3),
            config.getIntegerList("attackSpawnLocations").get(4),
            config.getIntegerList("attackSpawnLocations").getLast(),
            to.getBlockX(),
            to.getBlockY(),
            to.getBlockZ()
        ) || Calculator.isIncludeRange(
            config.getIntegerList("defenceSpawnLocations").getFirst(),
            config.getIntegerList("defenceSpawnLocations").get(1),
            config.getIntegerList("defenceSpawnLocations").get(2),
            config.getIntegerList("defenceSpawnLocations").get(3),
            config.getIntegerList("defenceSpawnLocations").get(4),
            config.getIntegerList("defenceSpawnLocations").getLast(),
            to.getBlockX(),
            to.getBlockY(),
            to.getBlockZ()
        ))){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }
}
