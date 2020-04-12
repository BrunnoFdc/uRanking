package ga.brunnofdc.uranking.hooks;

import org.bukkit.plugin.java.JavaPlugin;

public interface Hook {

    public String getRelativePlugin();

    public void setupHook(JavaPlugin plugin);

}
