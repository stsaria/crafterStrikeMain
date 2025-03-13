package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import si.f5.stsaria.crafterStrikeMain.AdvEnchantment;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    ArrayList<AdvEnchantment> ADV_ENCHANTMENTS() {
        return new ArrayList<>(List.of());
    }
}
