package ga.brunnofdc.uranking.hooks.placeholderapi;

import ga.brunnofdc.uranking.hooks.Hook;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaceholderAPIHook implements Hook {
    @Override
    public String getRelativePlugin() {
        return "PlaceholderAPI";
    }

    @Override
    public void setupHook(JavaPlugin plugin) {
        new PlaceholderAPIExpansion().register(plugin);
    }
}
