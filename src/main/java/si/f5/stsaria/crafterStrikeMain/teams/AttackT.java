package si.f5.stsaria.crafterStrikeMain.teams;

import org.bukkit.ChatColor;
import si.f5.stsaria.crafterStrikeMain.Game;

public class AttackT extends BTeam {
    @Override
    String NAME() {
        return "attack";
    }

    @Override
    String DISPLAY() {
        return Game.configGetString("wordAttackTeam");
    }

    @Override
    public ChatColor COLOR() {
        return ChatColor.RED;
    }
}
