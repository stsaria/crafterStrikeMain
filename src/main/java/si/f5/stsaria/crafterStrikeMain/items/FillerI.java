package si.f5.stsaria.crafterStrikeMain.items;

import org.bukkit.Material;

public class FillerI extends BItem {

    @Override
    Material MATERIAL() {
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
}
