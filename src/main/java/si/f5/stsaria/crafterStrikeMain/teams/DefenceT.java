package si.f5.stsaria.crafterStrikeMain.teams;

import org.bukkit.ChatColor;

public class DefenceT extends BTeam {
    @Override
    String NAME() {
        return "defence";
    }

    @Override
    String DISPLAY() {
        return "防御";
    }

    @Override
    public ChatColor COLOR() {
        return ChatColor.BLUE;
    }
}
