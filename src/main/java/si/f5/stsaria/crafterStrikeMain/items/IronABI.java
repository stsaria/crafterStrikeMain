package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;

public class IronABI extends BBuyGameI {

    @Override
    public Material MATERIAL() {
        return Material.TIPPED_ARROW;
    }

    @Override
    String NAME() {
        return "アイアン";
    }

    @Override
    int PRICE() {
        return 1900;
    }
}
