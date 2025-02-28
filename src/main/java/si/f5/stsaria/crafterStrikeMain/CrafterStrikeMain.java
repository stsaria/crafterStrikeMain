package si.f5.stsaria.crafterStrikeMain;

import org.bukkit.plugin.java.JavaPlugin;

public final class CrafterStrikeMain extends JavaPlugin {

    @Override
    public void onEnable() {
        new Game(this);
    }

    @Override
    public void onDisable() {

    }
}
