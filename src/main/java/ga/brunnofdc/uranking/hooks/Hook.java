package ga.brunnofdc.uranking.hooks;

import org.bukkit.plugin.java.JavaPlugin;

public interface Hook {

    String getRelativePlugin();

    void setupHook(JavaPlugin plugin);

}
