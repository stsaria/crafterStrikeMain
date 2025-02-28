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
    ChatColor COLOR() {
        return ChatColor.BLUE;
    }

    @Override
    String MESSAGE_PLANT_BOMB() {
        return ChatColor.RED+"爆弾が設置された！爆弾を解除しろ！";
    }

    @Override
    String MESSAGE_BOMBED() {
        return "C4が爆発した。作戦は失敗だ。";
    }


}
