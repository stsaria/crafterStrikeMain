package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;

public class NetheriteA extends BBuyItem {

    @Override
    Material MATERIAL() {
        return Material.SPECTRAL_ARROW;
    }

    @Override
    String NAME() {
        return "ネザライト";
    }

    @Override
    int PRICE() {
        return 3000;
    }
}
