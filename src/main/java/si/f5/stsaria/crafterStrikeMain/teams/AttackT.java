package si.f5.stsaria.crafterStrikeMain.teams;

import org.bukkit.ChatColor;

public class AttackT extends BTeam {
    @Override
    String NAME() {
        return "attack";
    }

    @Override
    String DISPLAY() {
        return "攻撃";
    }

    @Override
    public ChatColor COLOR() {
        return ChatColor.RED;
    }
}
