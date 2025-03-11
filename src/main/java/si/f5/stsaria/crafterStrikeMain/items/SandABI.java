package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import si.f5.stsaria.crafterStrikeMain.AdvEnchantment;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    ArrayList<AdvEnchantment> ADV_ENCHANTMENTS() {
        return new ArrayList<>(List.of());
    }
}
