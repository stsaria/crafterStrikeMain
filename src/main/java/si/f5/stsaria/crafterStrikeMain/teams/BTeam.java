package si.f5.stsaria.crafterStrikeMain.teams;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Objects;

public abstract class BTeam {
    private final Team team;
    private int score = 0;
    abstract String NAME();
    abstract String DISPLAY();
    public abstract ChatColor COLOR();
    abstract String MESSAGE_PLANT_BOMB();
    abstract String MESSAGE_BOMBED();

    public BTeam() {
        this.team = Objects.requireNonNull(Bukkit.getScoreboardManager())
                .getMainScoreboard().registerNewTeam(this.NAME());
        this.team.setDisplayName(this.DISPLAY());
        this.team.setColor(this.COLOR());
        this.team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        this.team.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.ALWAYS);
        this.team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);
        this.team.setAllowFriendlyFire(false);
        this.team.setCanSeeFriendlyInvisibles(true);
    }
    public final void upScore(){
        this.score++;
    }
    public final boolean contains(Player player){
        return this.team.getEntries().contains(player.getName());
    }
    public final int score(){
        return this.score;
    }
    public final void allAdd(ArrayList<Player> players){
        players.forEach(n -> this.team.addEntry(n.getName()));
    }
    public final void allRemove(){
        this.team.getEntries().forEach(this.team::removeEntry);
    }
    public final void remove(Player player){
        this.team.removeEntry(player.getName());
    }
    public final ArrayList<Player> list(){
        ArrayList<Player> players = new ArrayList<>();
        this.team.getEntries().forEach(n -> players.add(Bukkit.getPlayer(n)));
        return players;
    }
    public final ArrayList<Player> notSpectatorList(){
        ArrayList<Player> players = new ArrayList<>();
        this.list().forEach(p -> {if (p.getGameMode() != GameMode.SPECTATOR) players.add(p);});
        return players;
    }
}
