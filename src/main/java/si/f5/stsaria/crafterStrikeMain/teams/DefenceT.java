package si.f5.stsaria.crafterStrikeMain.teams;

import org.bukkit.ChatColor;
import si.f5.stsaria.crafterStrikeMain.Game;

public class DefenceT extends BTeam {
    @Override
    String NAME() {
        return "defence";
    }

    @Override
    String DISPLAY() {
        return Game.configGetString("wordDefenceTeam");
    }

    @Override
    public ChatColor COLOR() {
        return ChatColor.BLUE;
    }
}
