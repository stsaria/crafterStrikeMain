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
    ChatColor COLOR() {
        return ChatColor.RED;
    }

    @Override
    String MESSAGE_PLANT_BOMB() {
        return "爆弾を設置した。";
    }

    @Override
    String MESSAGE_BOMBED() {
        return "C4が爆発した。作戦は成功だ。";
    }


}
