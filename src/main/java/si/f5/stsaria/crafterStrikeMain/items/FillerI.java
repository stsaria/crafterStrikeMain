package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import si.f5.stsaria.crafterStrikeMain.AdvEnchantment;

import java.util.ArrayList;
import java.util.List;

public class FillerI extends BItem {

    @Override
    public Material MATERIAL() {
        return Material.STICK;
    }

    @Override
    String NAME() {
        return "このスロットは利用できません。";
    }

    @Override
    String ABOUT() {
        return "";
    }

    @Override
    ArrayList<AdvEnchantment> ADV_ENCHANTMENTS() {
        return new ArrayList<>(List.of(new AdvEnchantment(Enchantment.BINDING_CURSE, 1)));
    }
}
