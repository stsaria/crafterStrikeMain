package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;

public class SandABI extends BBuyGameI {

    @Override
    public Material MATERIAL() {
        return Material.ARROW;
    }

    @Override
    String NAME() {
        return "サンド";
    }

    @Override
    int PRICE() {
        return 500;
    }
}
